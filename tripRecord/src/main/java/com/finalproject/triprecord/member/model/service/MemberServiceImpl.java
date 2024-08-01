package com.finalproject.triprecord.member.model.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.member.model.dao.MemberMapper;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;

@Service
public class MemberServiceImpl implements MemberService{

	@Autowired
	private MemberMapper mMapper;
	
	@Override
	public int enrollMember(Member m) {
		return mMapper.enrollMember(m);
	}

	@Override
	public Member login(Member m) {
		return mMapper.login(m);
	}

	@Override
	public int enrollPlanner(Planner p) {
		return mMapper.enrollPlanner(p);
	}

	@Override
	public int checkId(String id) {
		return mMapper.checkId(id);
	}
	
	//마이페이지
	//내 정보 수정
	@Override
	public int updateMember(HashMap<String, String> map) {
		return mMapper.updateMember(map);
	}
	//내 비번 수정
	@Override
	public int updatPwdOfMe(HashMap<String, String> map) {
		return mMapper.updatePwdOfMe(map);
	}
	//플래너 요청
	@Override
	public int reqPromote(HashMap<String, Object> map) {
		return mMapper.reqPromote(map);
	}

}
