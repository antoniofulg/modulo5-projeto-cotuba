package br.com.unipds.statistics;

import java.util.Map;
import java.util.TreeMap;

public class WordCounter extends TreeMap<String, Integer> {

    public void addWord(String word) {
        if (word == null || word.isBlank()) {
            return;
        }
        merge(word, 1, Integer::sum);
    }
}
