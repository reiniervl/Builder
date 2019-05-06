package com.rvlstudio;

import com.rvlstudio.annotation.Builder;
import com.rvlstudio.annotation.BuilderField;

@Builder
public class Test {
	@BuilderField
	private String var1;

	@BuilderField(required = true)
	private int var2;

	@BuilderField
	private int var3;
	
	@BuilderField(required=true)
	private double var4;

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

	/**
	 * @return the var3
	 */
	public int getVar3() {
		return var3;
	}

	/**
	 * @param var3 the var3 to set
	 */
	public void setVar3(int var3) {
		this.var3 = var3;
	}

	/**
	 * @return the var4
	 */
	public double getVar4() {
		return var4;
	}

	/**
	 * @param var4 the var4 to set
	 */
	public void setVar4(double var4) {
		this.var4 = var4;
	}
}