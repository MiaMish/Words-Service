package com.mia.decool.service.words.mappers;

import com.mia.decool.service.words.dto.WordsDto;
import com.mia.decool.service.words.dto.WordsStatisticsDto;
import com.mia.decool.service.words.persistence.entities.WordsStatisticsEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WordsMapperImplTest {

    private WordsMapper wordsMapper;

    @BeforeEach
    void setUp() {
        wordsMapper = new WordsMapperImpl();
    }

    @Test
    void testWords() {
        assertThat(wordsMapper.words(null)).isEmpty();
        assertThat(wordsMapper.words(WordsDto.builder().build())).isEmpty();
        assertThat(wordsMapper.words(WordsDto.builder().words("word1, word2").build())).containsExactly("word1", "word2");
        assertThat(wordsMapper.words(WordsDto.builder().words("word1,word2").build())).containsExactly("word1", "word2");
    }

    @Test
    void statistics() {
        assertThat(wordsMapper.statistics(null)).isNotNull().isEqualTo(WordsStatisticsDto.builder().build());
        assertThat(wordsMapper.statistics(WordsStatisticsEntity.builder().build())).isNotNull().isEqualTo(WordsStatisticsDto.builder().build());
        assertThat(wordsMapper.statistics(WordsStatisticsEntity.builder().least(1).median(2).top5(null).build())).isNotNull().isEqualTo(WordsStatisticsDto.builder().least(1).median(2).top5(null).build());
    }

}