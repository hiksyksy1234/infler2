package com.naver.myhome4.client;

import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(name="comment-service")//마이크로 서비스 이름
public interface CommentServiceClient {
	
	@GetMapping(value = "/comments")
	Map<String, Object> getCommentList_Count(@RequestParam int board_num, @RequestParam int page);

}
