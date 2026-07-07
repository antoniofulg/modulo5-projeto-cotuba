package br.com.unipds.cotuba.adapters.in.cli;

import br.com.unipds.cotuba.ports.in.CotubaUseCase;
import br.com.unipds.cotuba.dto.CotubaParameters;
import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;

public class Main {

    void main(String[] args) {
        int exitCode = execute(args);
        if (exitCode != 0) {
            System.exit(exitCode);
        }
    }

    int execute(String[] args) {
        boolean verboseMode = true;

        try (SeContainer container = SeContainerInitializer.newInstance().initialize()) {
            var cliOptionsReader = container.select(CLIOptionsReader.class).get();
            CotubaParameters cotubaParameters = cliOptionsReader.read(args);

            verboseMode = cotubaParameters.verboseMode();

            CotubaUseCase cotubaUseCase = container.select(CotubaUseCase.class).get();
            cotubaUseCase.execute(cotubaParameters);

            System.out.println("Arquivo gerado com sucesso: " + cotubaParameters.outputFile());
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