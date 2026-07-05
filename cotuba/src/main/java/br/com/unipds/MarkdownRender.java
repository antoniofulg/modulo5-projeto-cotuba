package br.com.unipds;

import java.util.List;

public interface MarkdownRender {

    List<Chapter> render(List<Markdown> chapters);

}