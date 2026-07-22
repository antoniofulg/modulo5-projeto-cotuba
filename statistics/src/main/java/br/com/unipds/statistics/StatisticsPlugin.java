package br.com.unipds.statistics;

import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import br.com.unipds.cotuba.domain.Chapter;
import br.com.unipds.cotuba.domain.Ebook;
import br.com.unipds.cotuba.plugins.CotubaPlugin;

public class StatisticsPlugin implements CotubaPlugin {

    @Override
    public String afterRendering(String html) {
        return "";
    }

    @Override
    public void afterGeneration(Ebook ebook) {
        var wordCounter = new WordCounter();

        for (Chapter chapter : ebook.chapters()) {
            String html = chapter.html();
            Document doc = Jsoup.parseBodyFragment(html);
            String chapterText = doc.text().toLowerCase();
            chapterText = chapterText.replaceAll("\\p{Punct}", "");
            String[] words = chapterText.split("\\s+");
            for (String word : words) {
                wordCounter.addWord(word);
            }
        }

        for (Map.Entry<String, Integer> count : wordCounter.entrySet()) {
            String word = count.getKey();
            Integer occurrences = count.getValue();
            System.out.printf("'%s': %d\n", word, occurrences);
        }
    }
}
