package com.rvlstudio.annotation;

import static javax.lang.model.element.Modifier.STATIC;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
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

@SupportedAnnotationTypes("com.rvlstudio.annotation.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BuilderProcessor extends AbstractProcessor {
	private Map<String, BuilderClass> classMap = new HashMap<>();

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
			for(Entry<String, BuilderClass> entry : classMap.entrySet()) {
				System.out.println("Entry: "+ entry.getKey());
				if(entry.getValue().containsFluent()) {
					entry.getValue().getFluentElements().forEach((e) -> {
						System.out.println("fieldType: " + e.getFieldType());
					});
				}
			}
			for(Entry<String, BuilderClass> entry : classMap.entrySet()) {
				
				entry.getValue().write(processingEnv);
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
					System.out.println("Kind: " + f.asType().getKind());
				if(f.asType().getKind() == TypeKind.DECLARED &&	elements.getTypeElement(f.asType().toString()).getAnnotation(Builder.class) != null) {
					System.out.println("Builder: " + elements.getTypeElement(f.asType() + "Builder"));
					return new BuilderElement(f, f.asType() + "Builder");
				}
					return new BuilderElement(f);
				})
				.collect(Collectors.toList());
		}
		BuilderClass b = new BuilderClass(enclosing, be);
		System.out.println("[proc ] Parsed Class: " + b.getSimpleName());
		classMap.put(b.getSimpleName(), b);
	}
}
