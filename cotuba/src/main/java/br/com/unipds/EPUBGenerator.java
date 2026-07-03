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
    public void generateEPUB(List<String> htmlList, Path outputFile) {
        try {
            var epub = new Book();

            // TODO: definir título e autor para o livro
            epub.getMetadata().addTitle("Livro");
            epub.getMetadata().addAuthor(new Author("Autor"));

            boolean[] isFirstChapter = { true };

            htmlList.forEach(html -> {
                // TODO: usar título do capítulo
                String epubHtml = """
                          <html xmlns="http://www.w3.org/1999/xhtml">
                            <head>
                              <title>Capítulo</title>
                            </head>
                            <body>
                              %s
                            </body>
                          </html>
                        """.formatted(html);
                var chapter = new Resource(epubHtml.getBytes(), MediatypeService.XHTML);
                epub.addSection("Capítulo", chapter);

                if (isFirstChapter[0]) {
                    epub.getGuide().addReference(new GuideReference(chapter, "text", "Start Reading"));
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
