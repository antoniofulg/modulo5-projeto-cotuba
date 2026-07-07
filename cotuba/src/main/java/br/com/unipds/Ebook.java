package br.com.unipds;

import java.util.List;

import org.jmolecules.ddd.annotation.AggregateRoot;
import org.jmolecules.ddd.annotation.Identity;

import io.soabase.recordbuilder.core.RecordBuilder;

@AggregateRoot
@RecordBuilder
public record Ebook(
        @Identity String title,
        String author,
        EbookFormat format,
        List<Chapter> chapters) {
}
