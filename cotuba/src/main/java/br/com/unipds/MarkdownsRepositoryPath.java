package br.com.unipds;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MarkdownsRepositoryPath implements MarkdownsRepository {
    @Override
    public List<Chapter> find(Path mdFilePath) {
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.md");
        try (Stream<Path> mdStream = Files.list(mdFilePath)) {
            List<Path> mdFiles = mdStream
                    .filter(matcher::matches)
                    .sorted()
                    .toList();

            if (mdFiles.isEmpty()) {
                throw new IllegalStateException(
                        "Não foram encontrados capítulos (arquivos .md) no diretório: " + mdFilePath.toAbsolutePath());
            }

            return mdFiles.stream().map(mdFile -> {
                try {

                    var chapter = new Chapter();
                    String markdown = Files.readString(mdFile);
                    chapter.setMarkdown(markdown);
                    chapter.setMarkdownPath(mdFile);

                    return chapter;
                } catch (IOException e) {
                    throw new IllegalStateException("Erro ao ler arquivo: " + mdFile, e);
                }
            }).toList();
        } catch (IOException ex) {
            throw new IllegalStateException("Erro tentando encontrar arquivos .md em " + mdFilePath.toAbsolutePath(),
                    ex);
        }
    }
}
