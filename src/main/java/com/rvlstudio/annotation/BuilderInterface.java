package com.rvlstudio.annotation;

import java.util.List;
import java.util.ArrayList;

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