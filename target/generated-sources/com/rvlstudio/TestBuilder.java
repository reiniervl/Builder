package com.rvlstudio;

public class TestBuilder {
	Test result = new Test();

	public TestBuilder withVar1(java.lang.String var1) {
		this.result.setVar1(var1);
		return this;
	}

	public TestBuilder withVar2(int var2) {
		this.result.setVar2(var2);
		return this;
	}

	public Test build() {
		return result;
	}
}