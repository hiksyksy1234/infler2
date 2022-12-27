package com.naver.myhome4.mybatis.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.naver.myhome4.domain.Member;
@Mapper
public interface MemberMapper {

	public Member isId(String id);

	public int insert(Member m);

	public int update(Member m);

	public int delete(String id);

	public int getSearchListCount(Map<String, Object> map);

	public List<Member> getSearchList(Map<String, Object> map);

	

}