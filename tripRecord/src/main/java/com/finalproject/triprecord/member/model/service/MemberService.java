package com.finalproject.triprecord.member.model.service;

import com.finalproject.triprecord.member.model.vo.Member;

public interface MemberService {

	int enrollMember(Member m);

	Member login(Member m);

}
