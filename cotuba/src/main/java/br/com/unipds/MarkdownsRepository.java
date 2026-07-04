package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public interface MarkdownsRepository {

    List<Chapter> find(Path mdFilePath);

}