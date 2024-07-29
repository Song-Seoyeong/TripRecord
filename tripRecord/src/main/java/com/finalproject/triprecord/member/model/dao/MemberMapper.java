package com.finalproject.triprecord.member.model.dao;

import org.apache.ibatis.annotations.Mapper;

import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;

@Mapper
public interface MemberMapper {

	int enrollMember(Member m);

	Member login(Member m);

	int enrollPlanner(Planner p);

	int checkId(String id);

}
