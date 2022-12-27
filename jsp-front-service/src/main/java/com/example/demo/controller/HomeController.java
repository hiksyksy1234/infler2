package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
	
	
	@GetMapping("login")
	public String login() {
		return "/member/login.html";
	}
	
	
	@GetMapping("join")
	public String join() {
		return "/member/join.html";
	}
	
	
	@GetMapping("update")
	public String update() {
		return "/member/update.html";
	}
	
	@GetMapping("/user/list")
	public String user_list() {
		return "/member/member_list.html";
	}
	
	
	@GetMapping("/board/list")
	public String board() {
		System.out.print("list");
		return "/board/board_list.html";
	}
	
	@GetMapping("/board/{num}/detail")
	public String detail(@PathVariable int num) {
		return "/board/board_detail.html";
	}
	
	@GetMapping("/board/write")
	public String write() {
		return "/board/board_write.html";
	}
	
	@GetMapping("/board/{num}/edit")
	public String modify(@PathVariable int num) {
		return "/board/board_modify.html";
	}
	
	@GetMapping("/board/{num}/reply")
	public String reply(@PathVariable int num) {
		return "/board/board_reply.html";
	}
	
	
	

}
