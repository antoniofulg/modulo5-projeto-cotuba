package br.com.unipds;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EbookFilePropertiesReader implements EbookPropertiesReader {

    @Override
    public void read(Path mdFilePath, Ebook ebook) {

        Path propertiesFile = mdFilePath.resolve("ebook.properties");

        if (!Files.exists(propertiesFile)) {
            throw new IllegalStateException("Arquivo ebook.properties não encontrado em: " + mdFilePath);
        }

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(propertiesFile)) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler arquivo: " + propertiesFile, e);
        }

        String titleProperty = "cortuba.ebook.title";
        String title = properties.getProperty(titleProperty);
        validateProperty(title, titleProperty);

        String authorProperty = "cortuba.ebook.author";
        String author = properties.getProperty(authorProperty);
        validateProperty(author, authorProperty);

        ebook.setTitle(title);
        ebook.setAuthor(author);
    }

    private void validateProperty(String field, String property) {
        if (field == null || field.isBlank()) {
            throw new IllegalArgumentException("Propriedade inválida: " + property);
        }
    }

}
