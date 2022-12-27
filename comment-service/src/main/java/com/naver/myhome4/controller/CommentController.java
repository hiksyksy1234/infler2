package com.naver.myhome4.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.naver.myhome4.domain.Comment;
import com.naver.myhome4.service.CommentService;
/**
 * CORS란? CORS(Cross-origin resource sharing)이란, 
 * 웹 페이지의 제한된 자원을 외부 도메인에서 접근을
 * 허용해주는 메커니즘입니다.
 */
//@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class CommentController {

	private CommentService commentService;
	
	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@GetMapping(value = "/comments")
	public ResponseEntity<Map<String, Object>> CommentList(
			@RequestParam int board_num, @RequestParam int page) {
		List<Comment> list = commentService.getCommentList(board_num, page);
		int listcount = commentService.getListCount(board_num);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);
		map.put("listcount", listcount);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}

	@PostMapping(value = "/comments/new")
	public ResponseEntity<Integer> CommentAdd(Comment co)  {
		int result=  commentService.commentsInsert(co);
		if(result==1) {
			return new ResponseEntity<>(result, HttpStatus.CREATED);
		}else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "/comments/{num}")
	public ResponseEntity<Integer> CommentDelete(@PathVariable int num)  {
		int result=   commentService.commentsDelete(num);
		if(result==1) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PatchMapping(value = "/comments")
	public ResponseEntity<Integer>  CommentUpdate(Comment co)  {
		int result=  commentService.commentsUpdate(co);
		if(result==1) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		}else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
