package com.rvlstudio.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * <p>
 * Instance variabelen worden gebruikt om een builder class te maken. De class
 * die wordt gegenereerd is de naam van de originele class + 'Builder'. 
 * Standaard worden alleen de properties die geannoteerd zijn met: 
 * {@code BuilderField} worden gebruikt om de builder class te genereren.
 * Als het argument {@code all=true} wordt gegeven, worden alle properties
 * gebruikt om een builder te maken.
 * </p>
 *
 * @author Reinier van Leussen
 * @version 0.9.8
 */
@Target({ TYPE })
@Retention(SOURCE)
public @interface Builder {
	/**
	 * Alle properties/fields worden gebruikt om een builder te maken als
	 * de waarde {@code true} wordt megegeven. Standaard waarde is {@code false}
	 * 
	 * @return gebruik alle fields
	 */
	boolean all() default false;
}
