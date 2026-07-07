package br.com.unipds.cotuba.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import br.com.unipds.cotuba.domain.EbookFormat;
import jakarta.inject.Qualifier;

@Qualifier
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EbookFormatQualifier {
    EbookFormat value();
}
