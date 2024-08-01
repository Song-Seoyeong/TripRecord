package com.finalproject.triprecord.member.model.service;

import java.util.HashMap;

import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;

public interface MemberService {

	int enrollMember(Member m);

	Member login(Member m);

	int enrollPlanner(Planner p);

	int checkId(String id);
	
	//내 정보 수정
	int updateMember(HashMap<String, String> map);
	//내 비번 수정
	int updatPwdOfMe(HashMap<String, String> map);
	//플래너 요청
	int reqPromote(HashMap<String, Object> map);


}
