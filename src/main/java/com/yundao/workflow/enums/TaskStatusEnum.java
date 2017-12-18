package com.yundao.workflow.enums;


/**
 * 
 * date: 2017年7月15日 下午2:10:01
 * @author:wucq
 * @description:
 */
public enum TaskStatusEnum {
	/**
	 * 待办
	 */
	TODO("TODO","待办"), 
	/**
	 * 已完成
	 */
	DONE("DONE","已完成");
	
	private TaskStatusEnum(String value,String desc) {
		this.value = value;
	}
	private String value;
	private String desc;
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDesc() {
	
		return desc;
	}
	public void setDesc(String desc) {
	
		this.desc = desc;
	}
	
}
