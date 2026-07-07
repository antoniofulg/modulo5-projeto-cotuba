package br.com.unipds;

import java.nio.file.Path;

public interface EbookGenerator {

    void generate(Ebook ebook, Path outputFile);

}