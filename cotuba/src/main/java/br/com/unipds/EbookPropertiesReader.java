package br.com.unipds;

import java.nio.file.Path;

public interface EbookPropertiesReader {

    void read(Path mdFilePath, Ebook ebook);

}