package br.com.unipds;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.enterprise.context.ApplicationScoped;

@EbookFormatQualifier(EbookFormat.HTML)
@ApplicationScoped
public class HTMLGenerator implements EbookGenerator {

    @Override
    public void generate(Ebook ebook, Path outputFile) {
        try {
            Path htmlDirectory = Files.createDirectory(outputFile);

            int i = 1;
            Map<Chapter, Path> chapterHtmlFile = new LinkedHashMap<>();
            for (Chapter chapter : ebook.chapters()) {
                String htmlFileName = getHTMLFileNameChapter(i, chapter);
                Path htmlFile = htmlDirectory.resolve(htmlFileName);
                chapterHtmlFile.put(chapter, htmlFile);
                writeHTMLFile(chapter, htmlFile);
                i++;
            }

            writeSummary(ebook, htmlDirectory, chapterHtmlFile);
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao criar ebook HTML: " + outputFile, e);
        }
    }

    private void writeSummary(Ebook ebook, Path htmlDirectory, Map<Chapter, Path> chapterHtmlFile) throws IOException {
        String summaryListItems = ebook.chapters().stream().map(chapter -> """
                    <li>
                        <a href="%s">%s</a>
                    </li>
                """.formatted(chapterHtmlFile.get(chapter).getFileName(), chapter.title()))
                .collect(Collectors.joining());

        String htmlSummary = """
                    <!DOCTYPE html>
                    <html lang="pt-BR">
                    <head>
                        <meta charset="UTF-8">
                        <title>%s</title>
                    </head>
                    <body>
                        <h1>%s</h1>
                        <h2>Por: %s</h2>
                        <h3>Sumário</h3>
                        <ul>
                            %s
                        </ul>
                    </body>
                    </html>
                """.formatted(ebook.title(), ebook.title(), ebook.author(), summaryListItems);
        Path indexFile = htmlDirectory.resolve("index.html");
        ;
        Files.writeString(indexFile, htmlSummary, StandardCharsets.UTF_8);
    }

    private void writeHTMLFile(Chapter chapter, Path htmlFile) throws IOException {
        String html = """
                    <!DOCTYPE html>
                    <html lang="pt-BR">
                    <head>
                        <meta charset="UTF-8">
                        <title>%s</title>
                    </head>
                    <body>
                        %s
                    </body>
                    </html>
                """.formatted(chapter.title(), chapter.html());
        Files.writeString(htmlFile, html, StandardCharsets.UTF_8);
    }

    private String getHTMLFileNameChapter(int i, Chapter chapter) {
        String cleanTitle = chapter.title().toLowerCase().replaceAll("\\W", "");
        return "%02d-%s.html".formatted(i, cleanTitle);
    }

}
