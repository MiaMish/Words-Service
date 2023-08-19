package com.mia.decool.service.words.mappers;

import com.mia.decool.service.words.dto.WordsDto;
import com.mia.decool.service.words.dto.WordsStatisticsDto;
import com.mia.decool.service.words.persistence.entities.WordsStatisticsEntity;

import java.util.List;

public interface WordsMapper {

    List<String> words(WordsDto wordsDto);

    WordsStatisticsDto statistics(WordsStatisticsEntity statistics);

}
