package com.naver.myhome4.mybatis.mapper;

import java.util.HashMap;
//    <mybatis-spring:scan base-package="com.naver.myhome4.mapper"/>
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.naver.myhome4.domain.Board;

@Mapper
public interface BoardMapper {

	// 글의 갯수 구하기
	public int getListCount();

	public List<Board> getBoardList(HashMap<String, Integer> map);
	
	// 글 내용 보기
	public Board getDetail(int num);

	// 글 답변
	public int boardReply(Board board);

	// 글 수정
	public int boardModify(Board modifyboard);

	// 글 삭제
	public int boardDelete(Board board);

	// 조회수 업데이트
	public int setReadCountUpdate(int num);
	
	//글쓴이인지 확인
	public Board isBoardWriter(HashMap<String, Object> map);
	
	// 글 등록하기
	public void insertBoard(Board board) ;

    //BOARD_RE_SEQ값 수정
	public int boardReplyUpdate(Board board);


	
}
