package com.rvlstudio.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Fields geannoteerd met deze annotatie worden gebruikt om een builder te
 * maken, tenzij de waarde {@code all=true} wordt meegegeven aan de
 * Builder annotatie.
 */
@Documented
@Target({ FIELD })
@Retention(SOURCE)
public @interface BuilderField {
	/**
	 * Standaard waarde is {@code false}. Als de waarde {@code true} wordt
	 * gegeven, dan wordt van de field naam en interface gegenereerd,
	 * dat geimplementeerd wordt door de builder-class.
	 * 
	 * @return field moet een waarde hebben
	 */
	boolean required() default false;
}
