package br.com.unipds;

import java.nio.file.Path;

public record CotubaParameters(
        Path mdFilePath,
        EbookFormat format,
        Path outputFile,
        boolean verboseMode) {

    public CotubaParameters(Path mdFilePath, EbookFormat format, Path outputFile) {
        this(mdFilePath, format, outputFile, false);
    }
}