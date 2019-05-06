package com.rvlstudio;

import com.rvlstudio.annotation.Builder;
import com.rvlstudio.annotation.BuilderField;

@Builder
public class Test {
	@BuilderField
	private String var1;
	@BuilderField
	private int var2;

	/**
	 * @return the var1
	 */
	public String getVar1() {
		return var1;
	}

	/**
	 * @param var1 the var1 to set
	 */
	public void setVar1(String var1) {
		this.var1 = var1;
	}

	/**
	 * @return the var2
	 */
	public int getVar2() {
		return var2;
	}

	/**
	 * @param var2 the var2 to set
	 */
	public void setVar2(int var2) {
		this.var2 = var2;
	}

	
}