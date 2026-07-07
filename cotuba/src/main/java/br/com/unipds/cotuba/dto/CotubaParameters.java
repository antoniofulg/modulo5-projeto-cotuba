package br.com.unipds.cotuba.dto;

import java.nio.file.Path;

import br.com.unipds.cotuba.domain.EbookFormat;

public record CotubaParameters(
        Path mdFilePath,
        EbookFormat format,
        Path outputFile,
        boolean verboseMode) {

    public CotubaParameters(Path mdFilePath, EbookFormat format, Path outputFile) {
        this(mdFilePath, format, outputFile, false);
    }
}