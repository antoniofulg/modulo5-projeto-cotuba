package br.com.unipds;

import java.nio.file.Path;

public interface EbookPropertiesReader {
    EbookProperties read(Path mdFilePath);
}