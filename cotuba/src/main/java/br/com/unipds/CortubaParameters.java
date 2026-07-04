package br.com.unipds;

import java.nio.file.Path;

public class CortubaParameters {
    private Path mdFilePath;
    private EbookFormat format;
    private Path outputFile;
    private boolean verboseMode = false;

    public Path getMdFilePath() {
        return mdFilePath;
    }

    public void setMdFilePath(Path mdFilePath) {
        this.mdFilePath = mdFilePath;
    }

    public EbookFormat getFormat() {
        return format;
    }

    public void setFormat(EbookFormat format) {
        this.format = format;
    }

    public Path getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(Path outputFile) {
        this.outputFile = outputFile;
    }

    public boolean isVerboseMode() {
        return verboseMode;
    }

    public void setVerboseMode(boolean verboseMode) {
        this.verboseMode = verboseMode;
    }

}
