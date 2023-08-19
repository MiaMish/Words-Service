package com.mia.decool.service.words.services;

import com.mia.decool.service.words.dto.WordsDto;
import com.mia.decool.service.words.dto.WordsStatisticsDto;
import com.mia.decool.service.words.mappers.WordsMapper;
import com.mia.decool.service.words.persistence.dao.WordsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WordsServiceImpl implements WordsService {

    private static final Logger logger = LoggerFactory.getLogger(WordsServiceImpl.class);
    private final WordsDao wordsDao;
    private final WordsMapper wordsMapper;

    public WordsServiceImpl(WordsDao wordsDao, WordsMapper wordsMapper) {
        this.wordsDao = wordsDao;
        this.wordsMapper = wordsMapper;
    }

    @Override
    public void append(WordsDto wordsDto) {
        List<String> words = wordsMapper.words(wordsDto);
        validateWords(words);
        logger.info("Starting to append {} words...", words.size());
        // Adding the words from the input one by one and not in bulk for resilience.
        // If one word fails, the others will still be added.
        for (String word : words) {
            // Since append() is an async method, the next line only adds the word to the queue, meaning it works in O(1).
            // => The time complexity of the entire method is O(<number of words in input>) and the mem complexity is O(1).
            wordsDao.append(word);
            logger.debug("Added word {} to the queue.", word);
        }
        logger.info("Finished appending {} words.", words.size());
    }

    private void validateWords(List<String> words) {
        // Currently, no validation is needed.
        // However, we should consider checking the length of the words in the list, to avoid very long words.
    }

    @Override
    public WordsStatisticsDto getStatistics() {
        logger.info("getStatistics() method called");
        return wordsMapper.statistics(wordsDao.getStatistics());
    }
}
