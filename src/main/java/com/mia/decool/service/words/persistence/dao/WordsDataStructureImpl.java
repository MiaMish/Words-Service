package com.mia.decool.service.words.persistence.dao;

import com.mia.decool.service.words.persistence.entities.WordFrequency;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class WordsDataStructureImpl implements WordsDataStructure {

    private final NavigableSet<WordFrequency> wordsByFrequency;
    private final Map<String, WordFrequency> wordsMap;

    public WordsDataStructureImpl() {
        // Working under the assumption that TreeSet is a double red-black tree.
        this.wordsByFrequency = Collections.synchronizedNavigableSet(new TreeSet<>((o1, o2) -> {
            if (o1.getFrequency() == o2.getFrequency()) {
                return o1.getWord().compareTo(o2.getWord());
            }
            return o1.getFrequency() - o2.getFrequency();
        }));

        // The words map is used to allow O(1) access to the word's frequency.
        this.wordsMap = Collections.synchronizedMap(new HashMap<>());
    }

    /**
     * Appends a word to the data structure.
     * If the word already exists, it will increment the frequency of the word.
     * If the word does not exist, it will add the word to the data structure with a frequency of 1.
     * Complexity: O(log(n))
     * @param word the word to append to the data structure
     */
    public void append(String word) {
        WordFrequency oldValue = this.wordsMap.get(word);
        WordFrequency newValue = WordFrequency.builder().word(word).frequency(Optional.ofNullable(oldValue).map(WordFrequency::getFrequency).orElse(0) + 1).build();
        if (oldValue != null) {
            this.wordsByFrequency.remove(oldValue);
        }
        this.wordsByFrequency.add(newValue);
        this.wordsMap.put(word, newValue);
    }

    /**
     * Returns the most frequent words in the data structure.
     * Complexity: O(numOfWordsToReturn)
     * @param numOfWordsToReturn the number of words to return
     * @return the most frequent words in the data structure
     */
    public List<String> mostFrequent(int numOfWordsToReturn) {
        // Complexity drill down:
        // Both descendingSet() and stream() are O(1), since they are lazy and do not iterate over the entire set.
        // limit() is O(numOfWordsToReturn), since it iterates over the stream and stops after numOfWordsToReturn.
        return this.wordsByFrequency.descendingSet().stream().limit(numOfWordsToReturn).map(WordFrequency::getWord).collect(Collectors.toList());
    }

    /**
     * Returns the least frequent word in the data structure.
     * Complexity: O(1)
     * @return the least frequent word in the data structure
     */
    public int leastFrequent() {
        try {
            return this.wordsByFrequency.first().getFrequency();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    /**
     * Returns the size of the data structure.
     * Complexity: O(1)
     * @return the size of the data structure
     */
    public int size() {
        return this.wordsByFrequency.size();
    }

    /**
     * Returns the word at the given index in the data structure.
     * Complexity: O(distance between the candidate and index)
     * @param index the index of the word to return
     * @param candidate the candidate word to compare against
     * @return the word at the given index in the data structure
     */
    public WordFrequency get(int index, WordFrequency candidate) {
        if (this.wordsByFrequency.size() <= index) {
            return null;
        }
        if (candidate == null) {
            return this.get(index, this.wordsByFrequency.first());
        }
        NavigableSet<WordFrequency> headMap = this.wordsByFrequency.headSet(candidate, false);
        if (headMap.size() == index) {
            return candidate;
        }
        if (headMap.size() > index) {
            return this.get(index, headMap.last());
        }
        return this.get(index, this.wordsByFrequency.higher(candidate));
    }

    /**
     * Complexity: O(distance between the candidate and median)
     * @param medianCandidate the candidate word to compare against
     * @return the median word in the data structure
     */
    public WordFrequency median(WordFrequency medianCandidate) {
        return this.get(this.size() / 2, medianCandidate);
    }
}
