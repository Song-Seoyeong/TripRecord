package com.finalproject.triprecord.member.model.service;

import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;

public interface MemberService {

	int enrollMember(Member m);

	Member login(Member m);

	int enrollPlanner(Planner p);

}
