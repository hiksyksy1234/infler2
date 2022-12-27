package com.naver.myhome4.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.naver.myhome4.domain.Member;
import com.naver.myhome4.mybatis.mapper.MemberMapper;

@Service
public class MemberServiceImpl implements MemberService {
	
	private static final Logger logger  = LoggerFactory.getLogger(MemberServiceImpl.class);	
    private MemberMapper dao;
	
	@Autowired
	public MemberServiceImpl(MemberMapper dao) {
		this.dao = dao;
	}
    
    @Override
	public int isId(String id) {
		Member rmember = dao.isId(id);
		return (rmember==null) ? -1 : 1; //-1은 아이디가 존재하지 않는 경우
		                                 //1은   아이디가 존재하는 경우
	}  
    
    
	@Override
	public int insert(Member m) {
		return dao.insert(m);
	}
	
	@Override
	public int isId(String id, String password) {
        Member rmember = dao.isId(id);
        int result=-1; //아이디가 존재하지 않는 경우- rmember가 null인 경우
        if(rmember!=null) { //아이디가 존재하는 경우
        	if(rmember.getPassword().equals(password)) {
        		result=1; //아이디와 비밀번호가 일치하는 경우
        	}else
        		result=0; //아이디는 존재하지만 비밀번호가 일치하지 않는 경우
        }
		return result;
	}
	

	@Override
	public Member member_info(String id) {
		return dao.isId(id);
	}
	
	@Override
	public int delete(String id) {
		return dao.delete(id);
	}

	@Override
	public int update(Member m) {
		return dao.update(m);
	}
	

	@Override
	public List<Member> getSearchList(String index, String search_word, int page, int limit) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if(!index.equals("")) {
		  String[] search_field =index.split("");
		  logger.info("search_field.length:"+search_field.length);
		  for(int i=0;i<search_field.length;i++) {
			  logger.info(search_field[i]);
		  }
		  map.put("search_field", search_field);
		  map.put("search_word", "%" + search_word + "%");
		}
		int startrow=(page-1)*limit+1; 
		int endrow=startrow+limit-1; 		
		map.put("start", startrow);
		map.put("end", endrow);
		return dao.getSearchList(map);
	}

	@Override
	public int getSearchListCount(String index, String search_word) {
		HashMap<String,Object> map = new HashMap<String, Object>();
		if(!index.equals("")) {
			  String[] search_field =index.split("");
			  logger.info("search_field.length:"+search_field.length);
			  for(int i=0;i<search_field.length;i++) {
				  logger.info(search_field[i]);
			  }
			  map.put("search_field", search_field);
			  map.put("search_word", "%" + search_word + "%");
		}
		return dao.getSearchListCount(map);
	}

	
	

	
	
}






