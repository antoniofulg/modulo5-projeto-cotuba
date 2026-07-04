package br.com.unipds;

import java.util.List;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommonmarkMarkdownRender implements MarkdownRender {

    @Override
    public void render(List<Chapter> chapters) {

        chapters.forEach(chapter -> {

            Parser parser = Parser.builder().build();
            Node document = null;
            try {

                String markdown = chapter.getMarkdown();

                document = parser.parse(markdown);
                document.accept(new AbstractVisitor() {
                    @Override
                    public void visit(Heading heading) {
                        if (heading.getLevel() == 1) {
                            // capítulo
                            String chapterTitle = ((Text) heading.getFirstChild()).getLiteral();
                            chapter.setTitle(chapterTitle);
                        } else if (heading.getLevel() == 2) {
                            // seção
                        } else if (heading.getLevel() == 3) {
                            // título
                        }
                    }

                });
            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao fazer parse do arquivo " + chapter.getMarkdownPath(), ex);
            }

            try {
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                String html = renderer.render(document);
                ;

                chapter.setHtml(html);
            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + chapter.getMarkdownPath(),
                        ex);
            }
        });

    }
}
