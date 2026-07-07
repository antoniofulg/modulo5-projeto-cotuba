package br.com.unipds;

import org.jmolecules.ddd.annotation.Entity;
import org.jmolecules.ddd.annotation.Identity;

import io.soabase.recordbuilder.core.RecordBuilder;

@Entity
@RecordBuilder
public record Chapter(@Identity String title, Markdown markdown, String html) {
}
