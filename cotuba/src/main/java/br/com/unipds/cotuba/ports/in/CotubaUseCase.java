package br.com.unipds.cotuba.ports.in;

import br.com.unipds.cotuba.dto.CotubaParameters;

public interface CotubaUseCase {

    void execute(CotubaParameters cotubaParameters);

}