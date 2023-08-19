package com.mia.decool.service.words.persistence.dao;

import com.mia.decool.service.words.persistence.entities.WordFrequency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class WordsDataStructureImplTest {

    private WordsDataStructure wordsDataStructure;

    @BeforeEach
    void setUp() {
        wordsDataStructure = new WordsDataStructureImpl();
    }

    @Test
    void testInitialState() {
        assertThat(wordsDataStructure.size()).isEqualTo(0);
        assertThat(wordsDataStructure.mostFrequent(1)).isEmpty();
        assertThat(wordsDataStructure.leastFrequent()).isEqualTo(0);
        assertThat(wordsDataStructure.median(null)).isNull();
    }

    @Test
    void appendSingleElement() {
        wordsDataStructure.append("a");
        assertThat(wordsDataStructure.size()).isEqualTo(1);
        assertThat(wordsDataStructure.mostFrequent(1)).containsExactly("a");
        assertThat(wordsDataStructure.leastFrequent()).isEqualTo(1);
        assertThat(wordsDataStructure.median(null)).isEqualTo(new WordFrequency("a", 1));
    }

    @Test
    void appendSingleElementMultipleTimes() {
        wordsDataStructure.append("a");
        wordsDataStructure.append("a");
        wordsDataStructure.append("a");
        assertThat(wordsDataStructure.size()).isEqualTo(1);
        assertThat(wordsDataStructure.mostFrequent(1)).containsExactly("a");
        assertThat(wordsDataStructure.mostFrequent(3)).containsExactly("a");
        assertThat(wordsDataStructure.leastFrequent()).isEqualTo(3);
        assertThat(wordsDataStructure.median(null)).isEqualTo(new WordFrequency("a", 3));
    }

    @Test
    void appendMultipleElements() {
        wordsDataStructure.append("a");
        wordsDataStructure.append("b");
        wordsDataStructure.append("c");
        wordsDataStructure.append("a");
        wordsDataStructure.append("b");
        wordsDataStructure.append("a");
        assertThat(wordsDataStructure.size()).isEqualTo(3);
        assertThat(wordsDataStructure.mostFrequent(1)).containsExactly("a");
        assertThat(wordsDataStructure.leastFrequent()).isEqualTo(1);
        assertThat(wordsDataStructure.median(null)).isEqualTo(new WordFrequency("b", 2));
    }

}