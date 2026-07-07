package br.com.unipds.cotuba.adapters.in.cli;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.help.HelpFormatter;

import br.com.unipds.cotuba.domain.EbookFormat;
import br.com.unipds.cotuba.dto.CotubaParameters;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CLIOptionsReader {
    public CotubaParameters read(String[] args) {
        var options = new Options();

        var mdFilePathOption = new Option("d", "dir", true,
                "Diretório que contém os arquivos md. Default: diretório atual.");
        options.addOption(mdFilePathOption);

        var fileFormatOption = new Option("f", "format", true,
                "Formato de saída do ebook. Pode ser: pdf ou epub. Default: pdf");
        options.addOption(fileFormatOption);

        var outputFileOption = new Option("o", "output", true,
                "Arquivo de saída do ebook. Default: book.{format}.");
        options.addOption(outputFileOption);

        var verboseModeOption = new Option("v", "verbose", false,
                "Habilita modo verboso.");
        options.addOption(verboseModeOption);

        CommandLineParser cmdParser = new DefaultParser();
        var help = HelpFormatter.builder().get();
        CommandLine cmd;

        try {
            cmd = cmdParser.parse(options, args);
        } catch (ParseException e) {
            try {
                help.printHelp("cotuba", null, options, null, true);
            } catch (IOException ioe) {
                e.addSuppressed(ioe);
            }
            throw new IllegalStateException(e);
        }

        try {
            Path mdFilePath;
            EbookFormat format;
            Path outputFile;
            boolean verboseMode = true;

            String mdPathName = cmd.getOptionValue("dir");

            if (mdPathName != null) {
                mdFilePath = Paths.get(mdPathName);
                if (!Files.isDirectory(mdFilePath)) {
                    throw new IllegalArgumentException(mdPathName + " não é um diretório.");
                }
            } else {
                Path currentPath = Paths.get("");
                mdFilePath = currentPath;
            }

            String ebookFormat = cmd.getOptionValue("format");

            if (ebookFormat != null) {
                try {
                    format = EbookFormat.valueOf(ebookFormat.toUpperCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Formato do ebook inválido: " + ebookFormat, e);
                }
            } else {
                format = EbookFormat.PDF;
            }

            String ebookOutputFileName = cmd.getOptionValue("output");
            if (ebookOutputFileName != null) {
                outputFile = Paths.get(ebookOutputFileName);
            } else {
                outputFile = Paths.get("book." + format.name().toLowerCase());
            }
            try {
                if (Files.isDirectory(outputFile)) {
                    // deleta arquivos do diretório recursivamente
                    Files.walk(outputFile).sorted(Comparator.reverseOrder())
                            .map(Path::toFile).forEach(File::delete);
                } else {
                    Files.deleteIfExists(outputFile);
                }
            } catch (IOException e) {
                throw new IllegalStateException("Erro ao preparar arquivo de saída: " + outputFile.toAbsolutePath(), e);
            }

            verboseMode = cmd.hasOption("verbose");

            return new CotubaParameters(mdFilePath, format, outputFile, verboseMode);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
