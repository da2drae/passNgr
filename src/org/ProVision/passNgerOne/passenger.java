package org.ProVision.passNgerOne;

public class passenger {
	//Attributes of the Passenger
	private String lname, fname, dob, sex, docnum, nat;	
	private String add_str, add_city, add_stat, add_post;
	private String add_country;	
	
	//Constructor
	public passenger(){
		lname = "";
		fname = "";
		dob = "";
		sex = "";
		docnum = "";
		nat = "";
		add_str = "";
		add_city = "";
		add_stat = "";
		add_post = "";
		add_country = "";
	}
	
	public void setLname(String s){
		lname = s;
	}
	
	public String getLname(){
		return lname;
	}
	
	public void setFname(String s){
		fname = s;
	}
	
	public String getFname(){
		return fname;
	}
	
	public void setDOB(String s){
		dob = s;
	}	
	
	public String getDOB(){
		return dob;
	}
	
	public void setSex(String s){
		sex = s;
	}
	
	public String getSex(){
		return sex;
	}
	
	public void setDocNum(String s){
		docnum = s;
	}
	
	public String getDocNum(){
		return docnum;
	}
	
	public void setNat(String s){
		nat = s;
	}
	
	public String getNat(){
		return nat;
	}
	
	public void setAddStr(String s){
		add_str = s;
	}
	
	public String getAddStr(){
		return add_str;
	}
	
	public void setAddCity(String s){
		add_city = s;
	}
	
	public String getAddCity(){
		return add_city;
	}
	
	public void setAddStat(String s){
		add_stat = s;
	}
	
	public String getAddStat(){
		return add_stat;
	}
	
	public void setAddPost(String s){
		add_post = s;
	}
	
	public String getAddPost(){
		return add_post;
	}
	
	public void setAddCountry(String s){
		add_country = s;
	}
	
	public String getAddCountry(){
		return add_country;
	}
}
