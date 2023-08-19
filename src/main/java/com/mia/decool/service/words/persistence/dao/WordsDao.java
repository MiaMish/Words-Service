package com.mia.decool.service.words.persistence.dao;

import com.mia.decool.service.words.persistence.entities.WordsStatisticsEntity;

public interface WordsDao {

    void append(String word);

    WordsStatisticsEntity getStatistics();

}
