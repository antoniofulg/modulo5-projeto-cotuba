package br.com.unipds;

import java.nio.file.Path;

public record CortubaParameters(
        Path mdFilePath,
        EbookFormat format,
        Path outputFile,
        boolean verboseMode) {

    public CortubaParameters(Path mdFilePath, EbookFormat format, Path outputFile) {
        this(mdFilePath, format, outputFile, false);
    }
}