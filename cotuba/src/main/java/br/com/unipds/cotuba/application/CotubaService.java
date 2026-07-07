package br.com.unipds.cotuba.application;

import java.nio.file.Path;
import java.util.List;

import org.jmolecules.ddd.annotation.Service;

import br.com.unipds.cotuba.domain.Chapter;
import br.com.unipds.cotuba.domain.Ebook;
import br.com.unipds.cotuba.domain.EbookBuilder;
import br.com.unipds.cotuba.domain.EbookFormat;
import br.com.unipds.cotuba.domain.Markdown;
import br.com.unipds.cotuba.dto.CotubaParameters;
import br.com.unipds.cotuba.dto.EbookProperties;
import br.com.unipds.cotuba.ports.in.CotubaUseCase;
import br.com.unipds.cotuba.ports.out.EbookGenerator;
import br.com.unipds.cotuba.ports.out.EbookPropertiesReader;
import br.com.unipds.cotuba.ports.out.MarkdownRender;
import br.com.unipds.cotuba.ports.out.MarkdownsRepository;
import br.com.unipds.cotuba.support.EbookFormatFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;

@Service
@ApplicationScoped
public class CotubaService implements CotubaUseCase {

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

    @Override
    public void execute(CotubaParameters cotubaParameters) {

        Path mdFilePath = cotubaParameters.mdFilePath();

        List<Markdown> markdowns = markdownsRepository.find(mdFilePath);

        List<Chapter> chapters = markdownRender.render(markdowns);

        EbookFormat format = cotubaParameters.format();

        EbookProperties ebookProperties = ebookPropertiesReader.read(cotubaParameters.mdFilePath());

        Ebook ebook = EbookBuilder.builder().chapters(chapters).format(format).title(ebookProperties.title())
                .author(ebookProperties.author()).build();

        EbookGenerator ebookGenerator = ebookGenerators.select(EbookFormatFilter.of(ebook.format())).get();

        ebookGenerator.generate(ebook, cotubaParameters.outputFile());
    }
}
