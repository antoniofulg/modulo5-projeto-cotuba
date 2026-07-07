package br.com.unipds.cotuba.ports.out;

import java.nio.file.Path;

import br.com.unipds.cotuba.domain.Ebook;

public interface EbookGenerator {

    void generate(Ebook ebook, Path outputFile);

}