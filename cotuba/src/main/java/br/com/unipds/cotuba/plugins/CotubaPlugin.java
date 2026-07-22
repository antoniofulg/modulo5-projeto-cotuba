package br.com.unipds.cotuba.plugins;

import br.com.unipds.cotuba.domain.Ebook;

public interface CotubaPlugin {
    String afterRendering(String html);

    void afterGeneration(Ebook ebook);
}
