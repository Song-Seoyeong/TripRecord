package com.finalproject.triprecord.member.model.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;

@Mapper
public interface MemberMapper {

	int enrollMember(Member m);

	Member login(Member m);

	int enrollPlanner(Planner p);

	int checkId(String id);
	
	//마이페이지
	//내 정보 수정
	int updateMember(HashMap<String, String> map);
	//내 비번 수정
	int updatePwdOfMe(HashMap<String, String> map);
	//플래너 요청
	int reqPromote(HashMap<String, Object> map);
}
