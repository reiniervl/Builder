package com.rvlstudio.annotation;

import java.util.List;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

public class BuilderClass {
	private Element enclosing;
	private String accessModifier;
	private String className;
	private String simpleName;
	private List<BuilderElement> elements;

	public BuilderClass(Element enclosingElement) {
		this.enclosing = enclosingElement;
		this.accessModifier = "public";
		this.simpleName = enclosingElement.getSimpleName().toString();
		this.className = this.simpleName + "Builder";
		this.elements = new ArrayList<BuilderElement>();
	}

	public BuilderClass(Element enclosingElement, List<BuilderElement> elements) {
		this(enclosingElement);
		this.elements = elements;
	}

	public void addElement(BuilderElement element) {
		this.elements.add(element);
	}

	public void write(ProcessingEnvironment environment) {
		String packageName = environment.getElementUtils().getPackageOf(enclosing).toString();
		try {
			JavaFileObject srcFile = environment.getFiler().createSourceFile(packageName + "." + this.className);
			try (Writer writer = srcFile.openWriter()) {
				writer.write("package " + packageName + ";\n\n");
				writer.write(this.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(String.format("%s class %s {\n", this.accessModifier, this.className));
		sb.append(String.format("\t%s result = new %s();\n\n", this.simpleName, this.simpleName));

		for (BuilderElement element : this.elements) {
			sb.append(element.toString()).append("\n\n");
		}

		sb.append(String.format("\tpublic %s build() {\n\t\treturn result;\n\t}\n", this.simpleName));

		sb.append("}");

		return sb.toString();
	}
}