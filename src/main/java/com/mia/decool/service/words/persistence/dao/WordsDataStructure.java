package com.mia.decool.service.words.persistence.dao;

import com.mia.decool.service.words.persistence.entities.WordFrequency;

import java.util.List;

/**
 * A data structure that holds words and their frequencies.
 * Note: This data structure is not thread-safe.
 */
public interface WordsDataStructure {

    void append(String word);

    List<String> mostFrequent(int numOfWordsToReturn);

    int leastFrequent();

    int size();

    WordFrequency median(WordFrequency medianCandidate);

}
