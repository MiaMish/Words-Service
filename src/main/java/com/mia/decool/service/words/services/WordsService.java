package com.mia.decool.service.words.services;

import com.mia.decool.service.words.dto.WordsDto;
import com.mia.decool.service.words.dto.WordsStatisticsDto;

public interface WordsService {
    void append(WordsDto wordsDto);

    WordsStatisticsDto getStatistics();

}
