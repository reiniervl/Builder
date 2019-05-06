package com.rvlstudio.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Fields annotated whit this annotation, enclosed by a class that is annotated
 * with the {@code Builder} annotation will be used to create the builder.
 */
@Documented
@Target({ FIELD })
@Retention(SOURCE)
public @interface BuilderField {
}
