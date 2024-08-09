package com.finalproject.triprecord.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.admin.model.vo.RequestGrade;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.common.model.vo.Point;
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
	
	@Override
	public Member findId(Member m) {
		return mMapper.findId(m);
	}
	
	@Override
	public Member findPwd(Member m) {
		return mMapper.findPwd(m);
	}
	
	@Override
	public int updatePwd(Member m) {
		return mMapper.updatePwd(m);
	}
	
	//마이페이지------------------------------------------
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
	//요청 사항 확인
	@Override
	public RequestGrade checkRequest(int memberNo) {
		return mMapper.checkRequest(memberNo);
	}
	//회원 탈퇴
	@Override
	public int deleteMember(Member m) {
		return mMapper.deleteMember(m);
	}
	//회원 탈퇴 시 게시글 삭제
	@Override
	public int deleteBoard(Member m) {
		return mMapper.deleteBoard(m);
	}
	//회원 탈퇴 시 댓글삭제
	@Override
	public int deleteReply(Member m) {
		return mMapper.deleteReply(m);
	}
	//회원 탈퇴 시 일정삭제
	@Override
	public int deleteSchedule(Member m) {
		return mMapper.deleteSchedule(m);
	}
	//회원 탈퇴 시 리뷰삭제
	@Override
	public int deleteReview(Member m) {
		return mMapper.deleteReview(m);
	}
	//회원 탈퇴시 요청 일정 삭제
	@Override
	public int deleteReqSchedule(Member m) {
		return mMapper.deleteReqSchedule(m);
	}
	//회원 탈퇴시 요청 전환 요청 삭제
	@Override
	public int deleteReqGrade(Member m) {
		return mMapper.deleteReqGrade(m);
	}
	//프로필 이미지 유무 확인
	@Override
	public int checkProfile(int memberNo) {
		return mMapper.checkProfile(memberNo);
	}
	//프로필 이미지 삽입
	@Override
	public int insertProfile(Image i) {
		return mMapper.insertProfile(i);
	}
	//기존 프로필 이미지 삭제
	@Override
	public int deleteProfile(int memberNo) {
		return mMapper.deleteProfile(memberNo);
	}
	//기존 프로필 이미지 이름 가져오기
	@Override
	public Image existFileId(int memberNo) {
		return mMapper.existFileId(memberNo);
	}
	//포인트 목록 불러오기
	@Override
	public ArrayList<Point> selectPointList() {
		return mMapper.selectPointList();
	}
	//결제 내역 불러오기
	@Override
	public ArrayList<Payment> getPaymentList(PageInfo pi,int memberNo) {
		int offset = (pi.getCurrentPage()-1)*pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return mMapper.getPaymentList(rowBounds, memberNo);
	}
	//결제 내역 수 불러오기
	@Override
	public int pmListCount(int memberNo) {
		return mMapper.pmListCount(memberNo);
	}
	//----------------------------------------------------------------

	


	

}
