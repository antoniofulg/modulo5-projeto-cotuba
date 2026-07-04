package br.com.unipds;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

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

                StringWriter stringWriter = new StringWriter();
                try {
                    XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
                    XMLStreamWriter xmlWriter = xmlOutputFactory.createXMLStreamWriter(stringWriter);

                    xmlWriter.writeStartDocument("UTF-8", "1.0");
                    xmlWriter.writeStartElement("html");
                    xmlWriter.writeDefaultNamespace("http://www.w3.org/1999/xhtml");

                    xmlWriter.writeStartElement("head");
                    xmlWriter.writeStartElement("title");
                    xmlWriter.writeCharacters(title);
                    xmlWriter.writeEndElement();
                    xmlWriter.writeEndElement();

                    xmlWriter.writeStartElement("body");
                    // writeCharacters would escape HTML tags; flush and write the fragment as-is
                    xmlWriter.writeCharacters("");
                    xmlWriter.flush();
                    stringWriter.write(html);
                    xmlWriter.writeEndElement();

                    xmlWriter.writeEndElement();
                    xmlWriter.writeEndDocument();
                    xmlWriter.close();
                } catch (XMLStreamException e) {
                    throw new IllegalStateException("Erro ao gerar XML do capítulo: " + title, e);
                }

                var epubChapter = new Resource(
                        stringWriter.toString().getBytes(StandardCharsets.UTF_8),
                        MediatypeService.XHTML);
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
