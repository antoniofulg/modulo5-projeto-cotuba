package br.com.unipds;

import jakarta.enterprise.util.AnnotationLiteral;

public class EbookFormatFilter extends AnnotationLiteral<EbookFormatQualifier> implements EbookFormatQualifier {

    private final EbookFormat format;

    private EbookFormatFilter(EbookFormat format) {
        this.format = format;
    }

    @Override
    public EbookFormat value() {
        return format;
    }

    static EbookFormatFilter of(EbookFormat format) {
        return new EbookFormatFilter(format);
    }
}
