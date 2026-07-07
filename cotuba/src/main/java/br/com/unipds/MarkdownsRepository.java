package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

import org.jmolecules.ddd.annotation.Repository;

@Repository
public interface MarkdownsRepository {
    List<Markdown> find(Path mdFilePath);
}