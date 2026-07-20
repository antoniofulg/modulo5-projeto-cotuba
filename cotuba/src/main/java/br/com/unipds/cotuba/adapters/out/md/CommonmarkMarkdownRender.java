package br.com.unipds.cotuba.adapters.out.md;

import java.util.List;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Heading;
import org.commonmark.node.Node;
import org.commonmark.node.Text;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import br.com.unipds.cotuba.domain.ChapterBuilder;
import br.com.unipds.cotuba.domain.Chapter;
import br.com.unipds.cotuba.domain.Markdown;
import br.com.unipds.cotuba.ports.out.MarkdownRender;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CommonmarkMarkdownRender implements MarkdownRender {

    @Override
    public List<Chapter> render(List<Markdown> markdowns) {

        return markdowns.stream().map(markdown -> {

            var chapterBuilder = ChapterBuilder.builder();
            chapterBuilder.markdown(markdown);

            Parser parser = Parser.builder().build();
            Node document = null;
            try {

                document = parser.parse(markdown.content());
                document.accept(new AbstractVisitor() {
                    @Override
                    public void visit(Heading heading) {
                        if (heading.getLevel() == 1) {
                            // capítulo
                            String chapterTitle = ((Text) heading.getFirstChild()).getLiteral();
                            chapterBuilder.title(chapterTitle);
                        } else if (heading.getLevel() == 2) {
                            // seção
                        } else if (heading.getLevel() == 3) {
                            // título
                        }
                    }

                });
            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao fazer parse do arquivo " + markdown.name(), ex);
            }

            try {
                HtmlRenderer renderer = HtmlRenderer.builder().build();
                String html = renderer.render(document);
                ;

                chapterBuilder.html(html);
            } catch (Exception ex) {
                throw new IllegalStateException("Erro ao renderizar para HTML o arquivo " + markdown.name(),
                        ex);
            }

            return chapterBuilder.build();
        }).toList();

    }
}
