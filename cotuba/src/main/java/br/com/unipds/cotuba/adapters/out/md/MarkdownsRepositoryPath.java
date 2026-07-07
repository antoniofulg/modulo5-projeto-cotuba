package br.com.unipds.cotuba.adapters.out.md;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;
import java.util.stream.Stream;

import br.com.unipds.cotuba.domain.Markdown;
import br.com.unipds.cotuba.ports.out.MarkdownsRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MarkdownsRepositoryPath implements MarkdownsRepository {

    @Override
    public List<Markdown> find(Path mdFilePath) {
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
                    String content = Files.readString(mdFile);
                    String name = mdFile.getFileName().toString();
                    return new Markdown(name, content);
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
