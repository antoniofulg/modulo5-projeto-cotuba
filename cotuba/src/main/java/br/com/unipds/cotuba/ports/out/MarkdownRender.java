package br.com.unipds.cotuba.ports.out;

import java.util.List;

import br.com.unipds.cotuba.domain.Chapter;
import br.com.unipds.cotuba.domain.Markdown;

public interface MarkdownRender {

    List<Chapter> render(List<Markdown> chapters);

}