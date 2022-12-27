/*
데이터베이스, 비지니스 객체, 뷰 객체에서 가져온 값을 저장하거나 
데이터베이스, 비지니스 객체, 뷰 객체에 보낼 값을 저장하는 객체를
도메인 객체(Domain Object) 
또는 도메인 모델(Domain Model)이라 한다.
 */
package com.naver.myhome4.domain;

public class Member {
	private String  id      ;  
	
	//member_joinForm.jsp에서 비밀번호 name속성 값을 확인해 주세요 
	private String  password;  
	private String  name    ;  
	private int     age     ;   
	private String  gender  ; 
	private String  email   ;
	
	private String auth="ROLE_MEMBER";
	
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	} 
	
}
