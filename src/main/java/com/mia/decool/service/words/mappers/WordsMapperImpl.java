package com.mia.decool.service.words.mappers;

import com.mia.decool.service.words.dto.WordsDto;
import com.mia.decool.service.words.dto.WordsStatisticsDto;
import com.mia.decool.service.words.persistence.entities.WordsStatisticsEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class WordsMapperImpl implements WordsMapper {
    @Override
    public List<String> words(WordsDto wordsDto) {
        return Optional.ofNullable(wordsDto)
                .map(WordsDto::getWords)
                .map(words -> Arrays.stream(words.split(",")).map(String::trim).toList())
                .orElseGet(List::of);
    }

    @Override
    public WordsStatisticsDto statistics(WordsStatisticsEntity statistics) {
        if (statistics == null) {
            return WordsStatisticsDto.builder().build();
        }
        return WordsStatisticsDto.builder()
                .least(statistics.getLeast())
                .median(statistics.getMedian())
                .top5(statistics.getTop5())
                .build();
    }

}
