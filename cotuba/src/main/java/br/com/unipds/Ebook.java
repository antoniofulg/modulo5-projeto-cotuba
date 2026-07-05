package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Ebook(
        String title,
        String author,
        EbookFormat format,
        List<Chapter> chapters,
        Path outputFile) {
}
