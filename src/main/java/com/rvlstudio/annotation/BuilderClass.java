package com.rvlstudio.annotation;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

class BuilderClass {
	private Element enclosing;
	private String accessModifier;
	private String className;
	private String simpleName;
	private List<BuilderElement> elements;
	private List<BuilderInterface> interfaces;

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

		sortFields();
		this.interfaces = BuilderInterface.getInterfaces(this.elements);
		for(BuilderInterface iface : this.interfaces) {
			try {
				JavaFileObject srcFile = environment.getFiler().createSourceFile(packageName + "." + iface.getiName());
				try(Writer writer = srcFile.openWriter()) {
					writer.write("package " + packageName + ";\n\n");
					writer.write(iface.toString());
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		try {
			JavaFileObject srcFile = environment.getFiler().createSourceFile(packageName + "." + this.className);
			try (Writer writer = srcFile.openWriter()) {
				writer.write("package " + packageName + ";\n\n");
				writer.write(this.generate(environment));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String generate(ProcessingEnvironment environment) {
		StringBuilder sb = new StringBuilder();
		String staticReturnType = this.interfaces.size() > 0 ? interfaces.get(0).getiName() : this.className;

		sb.append(String.format("%s class %s%s {\n", 
			this.accessModifier,
			this.className, 
			interfacesToString()))
		.append(String.format("\tprivate %s result = new %s();\n\n",
			this.simpleName,
			this.simpleName))
		.append(String.format("\tprivate %s() { }\n\n",
			this.className))
		.append(String.format("\tpublic static %s builder() {\n\t\treturn new %s();\n\t}\n\n",
			staticReturnType,
			this.className));

		boolean allSetters = true;
		for (BuilderElement element : this.elements) {
			System.out.println(environment.getElementUtils().getTypeElement(element.getFieldType()));
			sb.append(element.toString()).append("\n\n");
			if(allSetters) allSetters = element.hasSetter();
		}

		sb.append(String.format("\tpublic %s build() {\n\t\treturn result;\n\t}\n\n", this.simpleName));

		if(!allSetters) {
			sb.append("\tprivate void setField(Object obj, Object value, String fieldName) {\n")
				.append("\t\t	try {\n")
				.append("\t\t\tjava.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);\n")
				.append("\t\t\tfield.setAccessible(true);\n")
				.append("\t\t\tfield.set(obj, value);\n")
				.append("\t\t} catch(NoSuchFieldException | SecurityException | IllegalAccessException e) {\n")
				.append("\t\t\te.printStackTrace();\n")
				.append("\t\t}\n\t}\n");
		}

		sb.append("}");

		return sb.toString();
	}

	private String interfacesToString() {
		StringBuilder impl = new StringBuilder("");
		if (interfaces != null && interfaces.size() > 0) {
			impl.append(" implements ");
			for (int i = 0; i < interfaces.size(); i++) {
				impl.append(interfaces.get(i).getiName());
				if (i + 1 < interfaces.size()) impl.append(", ");
			}
		}
		return impl.toString();
	}

	private void sortFields() {
		elements.sort((e1, e2) -> e1.isRequired() ? -1 : e2.isRequired() ? 1 : 0);
	}
}