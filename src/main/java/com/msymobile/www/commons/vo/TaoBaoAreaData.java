package com.msymobile.www.commons.vo;

import com.msymobile.www.commons.model.master.TaoBaoArea;

public class TaoBaoAreaData {
	private int code;
	private TaoBaoArea taoBaoArea;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public TaoBaoArea getTaoBaoArea() {
		return taoBaoArea;
	}
	public void setTaoBaoArea(TaoBaoArea taoBaoArea) {
		this.taoBaoArea = taoBaoArea;
	}
	@Override
	public String toString() {
		return "TaoBaoAreaData [code=" + code + ", taoBaoArea=" + taoBaoArea + "]";
	}
}
