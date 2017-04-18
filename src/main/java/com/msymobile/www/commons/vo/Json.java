package com.msymobile.www.commons.vo;

/**
 * 返回json结果对象封装类
 * @author mamm
 *
 */
public class Json {
	private boolean success;
	private String message;
	private Object obj;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	
	public Json() {
		super();
	}
	
	public Json(boolean success, String message, Object obj) {
		super();
		this.success = success;
		this.message = message;
		this.obj = obj;
	}
	
	@Override
	public String toString() {
		return "Json [success=" + success + ", message=" + message + ", obj=" + obj + "]";
	}
	
}
