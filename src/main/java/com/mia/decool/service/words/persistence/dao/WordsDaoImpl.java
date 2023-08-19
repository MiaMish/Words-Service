package com.mia.decool.service.words.persistence.dao;

import com.mia.decool.service.words.persistence.entities.WordFrequency;
import com.mia.decool.service.words.persistence.entities.WordsStatisticsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class WordsDaoImpl implements WordsDao {

    private static final Logger logger = LoggerFactory.getLogger(WordsDaoImpl.class);
    private final WordsDataStructureImpl wordsDataStructure;
    private WordFrequency medianCandidate;
    private final AtomicInteger appendCounter;
    private final LocksHelper locksHelper;

    private final int numOfIterationsBetweenMedianCandidateUpdate;

    public WordsDaoImpl(@Value("${words.update-median.every:100}") int numOfIterationsBetweenMedianCandidateUpdate,
                        LocksHelper locksHelper, WordsDataStructureImpl wordsDataStructureImpl) {
        this.numOfIterationsBetweenMedianCandidateUpdate = numOfIterationsBetweenMedianCandidateUpdate;
        this.locksHelper = locksHelper;
        this.wordsDataStructure = wordsDataStructureImpl;
        this.medianCandidate = null;
        this.appendCounter = new AtomicInteger(1);
    }

    @Override
    // Note: the @Async annotation is crucial for this service to function efficiently.
    // It allows the method to be executed in a separate thread, so that the main thread can continue to process other requests.
    // Without this annotation, the method will be executed in the main thread, and the main thread will be blocked until the method returns.
    // The thead pool is configured in the SpringAsyncConfig class.
    @Async("appendWordsThreadPoolTaskExecutor")
    public void append(String word) {
        try {
            this.appendWord(word);
        } catch (Throwable t) {
            // Currently, we do not preform retries in case of failure, we simply log the error and continue.
            logger.error(String.format("Error appending word \"%s\"", word), t);
        }
    }

    private void appendWord(String word) throws InterruptedException {
        locksHelper.runWithLockOnWord(word, () -> {
            this.wordsDataStructure.append(word);
            return null;
        });
        this.updateMedianCandidateIfNeeded();
    }

    private void updateMedianCandidateIfNeeded() {
        // Every <numOfIterationsBetweenMedianCandidateUpdate> we want to update the median candidate.
        // This will make the median calculation more efficient, because we will not need to iterate over the entire data structure every time we get the statistics.
        int currentAppendCounter = appendCounter.addAndGet(1);
        if (currentAppendCounter % this.numOfIterationsBetweenMedianCandidateUpdate == 0) {
            updateMedianCandidate();
        }
    }

    private void updateMedianCandidate() {
        // Note: we use a write-lock here, because we want to lock the data structure for updating the median candidate.
        // We do not want to allow any other thread to change the data structure while we are getting the statistics.
        this.medianCandidate = this.locksHelper.runWithLockOnDataStructure(() -> this.wordsDataStructure.median(this.medianCandidate));
    }

    @Override
    public WordsStatisticsEntity getStatistics() {
        // When we get the statistics, we want to lock the data structure entirely, so that no other thread will be able to change it.
        // In this way, we can be sure that the statistics we get are consistent.
        return this.locksHelper.runWithLockOnDataStructure(() -> WordsStatisticsEntity.builder()
                .top5(this.wordsDataStructure.mostFrequent(5))
                .least(this.wordsDataStructure.leastFrequent())
                .median(Optional.ofNullable(this.wordsDataStructure.median(this.medianCandidate)).map(WordFrequency::getFrequency).orElse(0))
                .build());
    }

}
