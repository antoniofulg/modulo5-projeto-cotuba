package br.com.unipds;

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
            CortubaParameters cortubaParameters = cliOptionsReader.read(args);

            verboseMode = cortubaParameters.isVerboseMode();

            CortubaService cortubaService = new CortubaService();
            cortubaService.execute(cortubaParameters);

            System.out.println("Arquivo gerado com sucesso: " + cortubaParameters.getOutputFile());
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