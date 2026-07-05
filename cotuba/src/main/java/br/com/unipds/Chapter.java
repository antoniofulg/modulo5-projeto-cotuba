package br.com.unipds;

import io.soabase.recordbuilder.core.RecordBuilder;

@RecordBuilder
public record Chapter(String title, Markdown markdown, String html) {
}
