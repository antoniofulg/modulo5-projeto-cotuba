package br.com.unipds.cotuba.ports.out;

import java.nio.file.Path;
import java.util.List;

import org.jmolecules.ddd.annotation.Repository;

import br.com.unipds.cotuba.domain.Markdown;

@Repository
public interface MarkdownsRepository {
    List<Markdown> find(Path mdFilePath);
}