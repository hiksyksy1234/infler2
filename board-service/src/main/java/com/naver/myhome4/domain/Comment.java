package com.naver.myhome4.domain;

public class Comment {
	private int num;
	private String id;
	private String content;
	private String reg_date;//2020-12-24 16:08:35
	private int board_num;
	
	
	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	
	
	
	
	
	/*
	private java.util.Date reg_date;
	//1577171315000
	public java.util.Date getReg_date() {
		return reg_date;
	}

	public void setReg_date(java.util.Date reg_date) {
		this.reg_date = reg_date;
	}
    */
	

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	

	public int getBoard_num() {
		return board_num;
	}

	public void setBoard_num(int board_num) {
		this.board_num = board_num;
	}

}
