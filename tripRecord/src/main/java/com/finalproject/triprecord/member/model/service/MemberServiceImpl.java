package com.finalproject.triprecord.member.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.member.model.dao.MemberMapper;
import com.finalproject.triprecord.member.model.vo.Member;

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

}
