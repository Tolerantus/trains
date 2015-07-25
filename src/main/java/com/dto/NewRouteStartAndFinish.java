package com.dto;



public class NewRouteStartAndFinish {
	private String typed_dep;
	private String typed_arr;
	private String select_dep; 
	private String select_arr;
	public NewRouteStartAndFinish(String typed_dep, String typed_arr,
			String select_dep, String select_arr) {
		super();
		this.typed_dep = typed_dep;
		this.typed_arr = typed_arr;
		this.select_dep = select_dep;
		this.select_arr = select_arr;
	}
	public String getTyped_dep() {
		return typed_dep;
	}
	public void setTyped_dep(String typed_dep) {
		this.typed_dep = typed_dep;
	}
	public String getTyped_arr() {
		return typed_arr;
	}
	public void setTyped_arr(String typed_arr) {
		this.typed_arr = typed_arr;
	}
	public String getSelect_dep() {
		return select_dep;
	}
	public void setSelect_dep(String select_dep) {
		this.select_dep = select_dep;
	}
	public String getSelect_arr() {
		return select_arr;
	}
	public void setSelect_arr(String select_arr) {
		this.select_arr = select_arr;
	}
	@Override
	public String toString() {
		return "NewRouteStartAndFinish [typed_dep=" + typed_dep
				+ ", typed_arr=" + typed_arr + ", select_dep=" + select_dep
				+ ", select_arr=" + select_arr + "]";
	}
	
	
	
}
