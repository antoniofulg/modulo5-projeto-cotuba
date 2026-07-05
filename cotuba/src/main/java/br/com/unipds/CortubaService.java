package br.com.unipds;

import java.nio.file.Path;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@ApplicationScoped
public class CortubaService {

    private final MarkdownRender markdownRender;
    private final EbookPropertiesReader ebookPropertiesReader;
    private final MarkdownsRepository markdownsRepository;
    private final Instance<EbookGenerator> ebookGenerators;

    @Inject
    public CortubaService(MarkdownRender markdownRender, EbookPropertiesReader ebookPropertiesReader,
            MarkdownsRepository markdownsRepository, @Any Instance<EbookGenerator> ebookGenerators) {
        this.markdownRender = markdownRender;
        this.ebookPropertiesReader = ebookPropertiesReader;
        this.markdownsRepository = markdownsRepository;
        this.ebookGenerators = ebookGenerators;
    }

    public void execute(CortubaParameters cortubaParameters) {

        Path mdFilePath = cortubaParameters.getMdFilePath();

        List<Chapter> chapters = markdownsRepository.find(mdFilePath);

        markdownRender.render(chapters);

        EbookFormat format = cortubaParameters.getFormat();

        Ebook ebook = new Ebook();

        ebookPropertiesReader.read(cortubaParameters.getMdFilePath(), ebook);

        ebook.setChapters(chapters);
        ebook.setFormat(format);
        ebook.setOutputFile(cortubaParameters.getOutputFile());

        EbookGenerator ebookGenerator = ebookGenerators.select(EbookFormatFilter.of(format)).get();

        ebookGenerator.generate(ebook);
    }
}
