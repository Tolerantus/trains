package com.dto;

public class StationsForSheduling {
private String singleStation;
private String st_dep;
private String st_arr;
private String date;
public StationsForSheduling(String singleStation, String st_dep, String st_arr,
		String date) {
	super();
	this.singleStation = singleStation;
	this.st_dep = st_dep;
	this.st_arr = st_arr;
	this.date = date;
}
public String getSingleStation() {
	return singleStation;
}
public void setSingleStation(String singleStation) {
	this.singleStation = singleStation;
}
public String getSt_dep() {
	return st_dep;
}
public void setSt_dep(String st_dep) {
	this.st_dep = st_dep;
}
public String getSt_arr() {
	return st_arr;
}
public void setSt_arr(String st_arr) {
	this.st_arr = st_arr;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
@Override
public String toString() {
	return "StationsForSheduling [singleStation=" + singleStation + ", st_dep="
			+ st_dep + ", st_arr=" + st_arr + ", date=" + date + "]";
}


}
