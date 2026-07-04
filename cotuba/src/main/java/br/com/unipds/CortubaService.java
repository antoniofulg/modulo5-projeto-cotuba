package br.com.unipds;

import java.util.List;

public class CortubaService {
    public void execute(CortubaParameters cortubaParameters) {
        var markdownRender = new MarkdownRender();
        List<Chapter> chapters = markdownRender.render(cortubaParameters.getMdFilePath());

        EbookFormat format = cortubaParameters.getFormat();

        Ebook ebook = new Ebook();

        EbookPropertiesReader ebookPropertiesReader = new EbookPropertiesReader();
        ebookPropertiesReader.read(cortubaParameters.getMdFilePath(), ebook);

        ebook.setChapters(chapters);
        ebook.setFormat(format);
        ebook.setOutputFile(cortubaParameters.getOutputFile());

        if (EbookFormat.PDF.equals(format)) {
            var pdfGenerator = new PDFGenerator();
            pdfGenerator.generatePDF(ebook);
        } else if (EbookFormat.EPUB.equals(format)) {
            var epubGenerator = new EPUBGenerator();
            epubGenerator.generateEPUB(ebook);
        } else {
            throw new IllegalArgumentException("Formato do ebook inválido: " + format);
        }

    }
}
