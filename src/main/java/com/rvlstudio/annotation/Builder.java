package com.rvlstudio.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * Instance variabelen worden gebruikt om een builder class te maken. De class
 * die wordt gegenereerd is de naam van de originele class + 'Builder'. Alleen
 * de variablen die geannoteerd zijn met: {@code BuilderField} worden gebruikt
 * om de builder class te genereren.
 * </p>
 *
 * @author Reinier van Leussen
 * @version 0.8.2
 */
@Target({ TYPE })
@Retention(SOURCE)
public @interface Builder {
}
