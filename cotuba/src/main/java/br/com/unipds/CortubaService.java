package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
public class CortubaService {

    private final MarkdownRender markdownRender;
    private final EbookPropertiesReader ebookPropertiesReader;
    private final MarkdownsRepository markdownsRepository;
    private final EbookGenerator epubGenerator;
    private final EbookGenerator pdfGenerator;

    @Inject
    public CortubaService(MarkdownRender markdownRender, EbookPropertiesReader ebookPropertiesReader,
            MarkdownsRepository markdownsRepository,
            @Named("EPUBGenerator") EbookGenerator epubGenerator,
            @Named("PDFGenerator") EbookGenerator pdfGenerator) {
        this.markdownRender = markdownRender;
        this.ebookPropertiesReader = ebookPropertiesReader;
        this.markdownsRepository = markdownsRepository;
        this.epubGenerator = epubGenerator;
        this.pdfGenerator = pdfGenerator;

    }

    public void execute(CortubaParameters cortubaParameters) {

        Path mdFilePath = cortubaParameters.getMdFilePath();

        List<Chapter> chapters = markdownsRepository.find(mdFilePath);

        markdownRender.render(chapters);

        EbookFormat format = cortubaParameters.getFormat();

        Ebook ebook = new Ebook();

        ebookPropertiesReader.read(cortubaParameters.getMdFilePath(), ebook);

        ebook.setChapters(chapters);
        ebook.setFormat(format);
        ebook.setOutputFile(cortubaParameters.getOutputFile());

        EbookGenerator ebookGenerator;

        if (EbookFormat.PDF.equals(format)) {
            ebookGenerator = pdfGenerator;
        } else if (EbookFormat.EPUB.equals(format)) {
            ebookGenerator = epubGenerator;
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + format);
        }

        ebookGenerator.generate(ebook);

    }
}
