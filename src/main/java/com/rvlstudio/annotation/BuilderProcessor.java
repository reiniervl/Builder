package com.rvlstudio.annotation;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import static javax.lang.model.element.Modifier.STATIC;

@SupportedAnnotationTypes("com.rvlstudio.annotation.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BuilderProcessor extends AbstractProcessor {
	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
				List<Element> enclosedFields = element.getEnclosedElements()
					.stream()
					.filter((e) -> e.getKind().isField() && !e.getModifiers()
						.stream()
						.anyMatch((m) -> m.equals(STATIC)))
					.collect(Collectors.toList());
				this.parseBuilderClass(element, enclosedFields);
			}
		}
		return false;
	}

	private void parseBuilderClass(Element enclosing, List<Element> fields) {
		BuilderClass builderClass;

		if(enclosing.getAnnotation(Builder.class).all()) {
			List<BuilderElement> be = fields.stream().map((f) -> new BuilderElement(f)).collect(Collectors.toList());
			builderClass = new BuilderClass(enclosing, be);
		} else {
			List<BuilderElement> be = fields.stream()
				.filter((f) -> f.getAnnotation(BuilderField.class) != null)
				.map((f) -> new BuilderElement(f))
				.collect(Collectors.toList());
			builderClass = new BuilderClass(enclosing, be);
		}

		builderClass.write(processingEnv);
	}
}
