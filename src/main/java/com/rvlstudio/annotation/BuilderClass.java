package com.rvlstudio.annotation;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.tools.JavaFileObject;

public class BuilderClass {
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
				writer.write(this.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String generate() {
		StringBuilder sb = new StringBuilder();

		StringBuilder impl = new StringBuilder("");
		if (interfaces != null && interfaces.size() > 0) {
			impl.append(" implements ");
			for (int i = 0; i < interfaces.size(); i++) {
				impl.append(interfaces.get(i).getiName());
				if (i + 1 < interfaces.size())
					impl.append(", ");
			}
		}

		sb.append(String.format("%s class %s%s {\n", this.accessModifier, this.className, impl));
		sb.append(String.format("\t%s result = new %s();\n\n", this.simpleName, this.simpleName));

		String staticMethodName = this.simpleName.substring(0, 1).toLowerCase();
		if(this.simpleName.length() > 1) staticMethodName += this.simpleName.substring(1);
		String staticReturnType = this.interfaces.size() > 0 ? interfaces.get(0).getiName() : this.className;
		sb.append(String.format("\tpublic static %s %s() {\n\t\treturn new %s();\n\t}\n\n", staticReturnType, staticMethodName, this.className));

		for (BuilderElement element : this.elements) {
			sb.append(element.toString()).append("\n\n");
		}

		sb.append(String.format("\tpublic %s build() {\n\t\treturn result;\n\t}\n", this.simpleName));
		sb.append("}");

		return sb.toString();
	}

	private void sortFields() {
		elements.sort((e1, e2) -> e1.isRequired() ? -1 : e2.isRequired() ? 1 : 0);
	}

	@Override
	public String toString() {
		return this.generate();
	}
}

class BuilderInterface {
	private String iName;
	private String methodName;
	private String parameterType;
	private String parameterName;
	private String returnType;

	public static List<BuilderInterface> getInterfaces(List<BuilderElement> elements) {
		List<BuilderInterface> ifaces = new ArrayList<>();

		for(int i = 0; i < elements.size(); i++) {
			BuilderElement e = elements.get(i);
			if(e.isRequired()) {
				if(i+1 < elements.size()) {
					if(elements.get(i+1).isRequired()) e.setReturnType(elements.get(i+1).getFieldNameCapatalized() + "Builder");
					ifaces.add(
						new BuilderInterface(
							e.getFieldNameCapatalized() + "Builder",
							"with" + e.getFieldNameCapatalized(),
							e.getReturnType(),
							e.getFieldType(),
							e.getFieldName()
						)
					);
				}
			}
		}

		return ifaces;
	}

	public BuilderInterface(String iName, String methodName, String returnType, String parameterType, String parameterName) {
		this.iName = iName;
		this.methodName = methodName;
		this.returnType = returnType;
		this.parameterType = parameterType;
		this.parameterName = parameterName;
	}

	public String getiName() {
		return iName;
	}
	public void setiName(String iName) {
		this.iName = iName;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public String getReturnType() {
		return returnType;
	}
	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}
	public String getParameterType() {
		return parameterType;
	}
	public void setParameterType(String parameterType) {
		this.parameterType = parameterType;
	}
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("public interface ")
			.append(this.iName).append(" {\n\t")
			.append(returnType).append(" ")
			.append(methodName).append("(")
			.append(parameterType).append(" ")
			.append(parameterName).append(");\n}");
		return sb.toString();
	}
}