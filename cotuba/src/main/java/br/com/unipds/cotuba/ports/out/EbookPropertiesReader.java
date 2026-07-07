package br.com.unipds.cotuba.ports.out;

import java.nio.file.Path;

import br.com.unipds.cotuba.dto.EbookProperties;

public interface EbookPropertiesReader {
    EbookProperties read(Path mdFilePath);
}