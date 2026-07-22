package br.com.unipds.csstheme;

import br.com.unipds.cotuba.domain.Ebook;
import br.com.unipds.cotuba.plugins.CotubaPlugin;

public class CssThemePlugin implements CotubaPlugin {

    @Override
    public String afterRendering(String html) {
        return """
                <style>
                        h1 {
                            border-bottom: 1px dashed black;
                            font-size: 3rem;
                            font-weight: bolder;
                            font-variant-caps: small-caps;
                        }
                        h2 {
                            border-left: 1px solid black;
                            border-bottom: 1px solid black;
                            padding-left: .5rem;
                        }
                </style>
                %s
                        """.formatted(html);
    }

    @Override
    public void afterGeneration(Ebook ebook) {
        // nada a fazer após a geração
    }
}
