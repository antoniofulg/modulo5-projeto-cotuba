package br.com.unipds;

import java.nio.file.Path;

public class Chapter {
    private String title;
    private String markdown;
    private Path markdownPath;
    private String html;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public Path getMarkdownPath() {
        return markdownPath;
    }

    public void setMarkdownPath(Path markdownPath) {
        this.markdownPath = markdownPath;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

}
