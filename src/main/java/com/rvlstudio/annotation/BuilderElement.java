package com.rvlstudio.annotation;

import javax.lang.model.element.Element;

public class BuilderElement {
	private String accessModifier;
	private String fieldName;
	private String fieldNameCapatalized;
	private String fieldType;
	private String returnType;

	public BuilderElement(Element element) {
		this(element, element.getEnclosingElement().getSimpleName() + "Builder");
	}

	public BuilderElement(Element element, String returnType) {
		this.accessModifier = "public";
		this.fieldName = element.getSimpleName().toString();
		this.fieldNameCapatalized = BuilderElement.capatalize(this.fieldName);
		this.fieldType = element.asType().toString();
		this.returnType = returnType;
	}

	public BuilderElement(String accessModifier, String returnType, String fieldName, String fieldType) {
		this.accessModifier = accessModifier;
		this.returnType = returnType;
		this.fieldName = fieldName;
		this.fieldNameCapatalized = BuilderElement.capatalize(fieldName);
		this.fieldType = fieldType;
	}
	
	public String generate() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("\t%s %s with%s(%s %s) {\n", 
			this.accessModifier,
			this.returnType,
			this.fieldNameCapatalized,
			this.fieldType,
			this.fieldName));
		
		sb.append(String.format("\t\tthis.result.set%s(%s);\n", 
			this.fieldNameCapatalized, 
			this.fieldName));

		sb.append(String.format("\t\treturn %s;\n", "this"));
		sb.append("\t}");

		return sb.toString();
	}

	@Override
	public String toString() {
		return this.generate();
	}

	private static String capatalize(String var) {
		String result = "";
		if(var != null && var.length() > 0) {
			result = var.substring(0, 1).toUpperCase() + var.substring(1);
		}
		return result;
	}
}