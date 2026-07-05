package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class CotubaService {

    private final MarkdownRender markdownRender;
    private final EbookPropertiesReader ebookPropertiesReader;
    private final MarkdownsRepository markdownsRepository;
    private final Instance<EbookGenerator> ebookGenerators;

    @Inject
    public CotubaService(MarkdownRender markdownRender, EbookPropertiesReader ebookPropertiesReader,
            MarkdownsRepository markdownsRepository, @Any Instance<EbookGenerator> ebookGenerators) {
        this.markdownRender = markdownRender;
        this.ebookPropertiesReader = ebookPropertiesReader;
        this.markdownsRepository = markdownsRepository;
        this.ebookGenerators = ebookGenerators;
    }

    public void execute(CotubaParameters cotubaParameters) {

        Path mdFilePath = cotubaParameters.mdFilePath();

        List<Markdown> markdowns = markdownsRepository.find(mdFilePath);

        List<Chapter> chapters = markdownRender.render(markdowns);

        EbookFormat format = cotubaParameters.format();

        EbookProperties ebookProperties = ebookPropertiesReader.read(cotubaParameters.mdFilePath());

        Ebook ebook = EbookBuilder.builder().chapters(chapters).format(format)
                .outputFile(cotubaParameters.outputFile()).title(ebookProperties.title())
                .author(ebookProperties.author()).build();

        EbookGenerator ebookGenerator = ebookGenerators.select(EbookFormatFilter.of(ebook.format())).get();

        ebookGenerator.generate(ebook);
    }
}
