package com.rvlstudio.annotation;

import static javax.lang.model.element.Modifier.STATIC;

import java.util.ArrayList;
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
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic.Kind;

@SupportedAnnotationTypes("com.rvlstudio.annotation.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BuilderProcessor extends AbstractProcessor {
	private ArrayList<BuilderClass> bc = new ArrayList<>();

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
		if(roundEnv.processingOver()) {
			for(BuilderClass b : bc) {
				b.write(processingEnv);
			}
		}
		return false;
	}

	private void parseBuilderClass(Element enclosing, List<Element> fields) {
		List<BuilderElement> be;
		if(enclosing.getAnnotation(Builder.class).all()) {
			be = fields.stream().map((f) -> new BuilderElement(f)).collect(Collectors.toList());
		} else {
			Elements elements = processingEnv.getElementUtils();
			be = fields.stream()
				.filter((f) -> f.getAnnotation(BuilderField.class) != null)
				.map((f) -> {
				if(f.asType().getKind() == TypeKind.DECLARED &&	elements.getTypeElement(f.asType().toString()).getAnnotation(Builder.class) != null) {
					System.out.println(elements.getTypeElement(f.asType() + "Builder"));
					return new BuilderElement(f, f.asType() + "Builder");
				}
					return new BuilderElement(f);
				})
				.collect(Collectors.toList());
		}
		processingEnv.getMessager().printMessage(Kind.NOTE, "Parsed Class: " + enclosing.getSimpleName());
		bc.add(new BuilderClass(enclosing, be));
	}
}
