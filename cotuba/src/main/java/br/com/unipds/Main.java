package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

public class Main {

    void main(String[] args) {
        int exitCode = executar(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int executar(String[] args) {
        boolean verboseMode = false;

        try {
            var cliOptionsReader = new CLIOptionsReader();
            cliOptionsReader.read(args);

            Path mdFilePath = cliOptionsReader.getMdFilePath();
            EbookFormat format = cliOptionsReader.getFormat();
            Path outputFile = cliOptionsReader.getOutputFile();
            verboseMode = cliOptionsReader.isVerboseMode();

            var markdownRender = new MarkdownRender();
            List<String> htmlList = markdownRender.render(mdFilePath);

            if (EbookFormat.PDF.equals(format)) {

                var pdfGenerator = new PDFGenerator();
                pdfGenerator.generatePDF(htmlList, outputFile);

            } else if (EbookFormat.EPUB.equals(format)) {

                var epubGenerator = new EPUBGenerator();
                epubGenerator.generateEPUB(htmlList, outputFile);
            } else {
                throw new IllegalArgumentException("Formato do ebook inválido: " + format);
            }

            System.out.println("Arquivo gerado com sucesso: " + outputFile);
            return 0;

        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            if (verboseMode) {
                System.err.println();
                ex.printStackTrace();
            }
            return 1;
        }
    }

}