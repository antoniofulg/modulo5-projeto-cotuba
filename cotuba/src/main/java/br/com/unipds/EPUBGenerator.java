package br.com.unipds;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.GuideReference;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.epub.EpubWriter;
import nl.siegmann.epublib.service.MediatypeService;

public class EPUBGenerator {
    public void generateEPUB(Ebook ebook) {
        List<Chapter> chapters = ebook.getChapters();
        Path outputFile = ebook.getOutputFile();

        try {
            var epub = new Book();

            epub.getMetadata().addTitle(ebook.getTitle());
            epub.getMetadata().addAuthor(new Author(ebook.getAuthor()));

            boolean[] isFirstChapter = { true };

            chapters.forEach(chapter -> {
                String html = chapter.getHtml();
                String title = chapter.getTitle();
                String epubHtml = """
                          <html xmlns="http://www.w3.org/1999/xhtml">
                            <head>
                              <title>%s</title>
                            </head>
                            <body>
                              %s
                            </body>
                          </html>
                        """.formatted(title, html);
                var epubChapter = new Resource(epubHtml.getBytes(), MediatypeService.XHTML);
                epub.addSection("Capítulo", epubChapter);

                if (isFirstChapter[0]) {
                    epub.getGuide().addReference(new GuideReference(epubChapter, "text", "Start Reading"));
                    isFirstChapter[0] = false;
                }
            });

            var epubWriter = new EpubWriter();

            try {
                epubWriter.write(epub, Files.newOutputStream(outputFile));
            } catch (IOException ex) {
                throw new IllegalStateException("Erro ao criar arquivo EPUB: " + outputFile.toAbsolutePath(), ex);
            }

        } catch (Exception ex) {
            throw new IllegalStateException("Erro ao gerar EPUB: " + outputFile.toAbsolutePath(), ex);
        }
    }
}
