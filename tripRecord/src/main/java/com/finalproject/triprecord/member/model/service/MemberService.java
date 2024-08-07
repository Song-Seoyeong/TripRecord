package com.finalproject.triprecord.member.model.service;

import java.util.HashMap;

import com.finalproject.triprecord.admin.model.vo.RequestGrade;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;

public interface MemberService {

	int enrollMember(Member m);

	Member login(Member m);

	int enrollPlanner(Planner p);

	int checkId(String id);
	
	Member findId(Member m);
	
	Member findPwd(Member m);
	
	int updatePwd(Member m);
	
	//마이페이지
	//내 정보 수정
	int updateMember(HashMap<String, String> map);
	//내 비번 수정
	int updatPwdOfMe(HashMap<String, String> map);
	//플래너 요청
	int reqPromote(HashMap<String, Object> map);
	//요청 사항 확인
	RequestGrade checkRequest(int memberNo);
	//회원 탈퇴
	int deleteMember(Member m);
	//회원 탈퇴 시 게시글 삭제
	int deleteBoard(Member m);
	//회원 탈퇴 시 댓글삭제
	int deleteReply(Member m);
	//회원 탈퇴 시 일정삭제
	int deleteSchedule(Member m);
	//회원 탈퇴 시 리뷰삭제
	int deleteReview(Member m);
	//회원 탈퇴시 요청 일정 삭제
	int deleteReqSchedule(Member m);
	//회원 탈퇴시 요청 전환 요청 삭제
	int deleteReqGrade(Member m);
	//프로필 이미지 유무 확인
	int checkProfile(int memberNo);
	//프로필 이미지 삽입
	int insertProfile(Image i);
	//기존 프로필 이미지 삭제
	int deleteProfile(int memberNo);
	//기존 프로필 이미지 이름 가져오기
	Image existFileId(int memberNo);

	


	

}
