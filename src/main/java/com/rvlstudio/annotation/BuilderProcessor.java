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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

@SupportedAnnotationTypes("com.rvlstudio.annotation.Builder")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class BuilderProcessor extends AbstractProcessor {
	@Override
	public boolean process(final Set<? extends TypeElement> annotations, final RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
				if (element.getKind() != ElementKind.CLASS) {
					continue;
				}
				List<Element> enclosedElements = element.getEnclosedElements().stream()
						.filter((e) -> e.getKind() == ElementKind.FIELD).collect(Collectors.toList());
				this.parseBuilderClass(element, enclosedElements);
			}
		}
		return false;
	}

	private void parseBuilderClass(Element enclosing, List<Element> fields) {
		BuilderClass builderClass = new BuilderClass(enclosing);

		for(Element field : fields) {
			if (field.getAnnotation(BuilderField.class) == null) continue;
			builderClass.addElement(new BuilderElement(field));
		}

		builderClass.write(processingEnv);
	}
}
