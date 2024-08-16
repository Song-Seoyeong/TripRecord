package com.finalproject.triprecord.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.admin.model.vo.RequestGrade;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.Question;
import com.finalproject.triprecord.common.model.vo.Cancel;
import com.finalproject.triprecord.common.model.vo.FeedBack;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.common.model.vo.Point;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.model.dao.MemberMapper;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;
import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

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
	//회원 정보 불러오기
	@Override
	public Member getMember(int memberNo) {
		return mMapper.getMember(memberNo);
	}
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
	//게시글 목록수 불러오기
	@Override
	public int getListCount(HashMap<String, Object> map) {
		return mMapper.getListCount(map);
	}
	//자기 문의글 불러오기
	@Override
	public ArrayList<Board> getBoardList(HashMap<String, Object> map, PageInfo pi) {
		int offset = (pi.getCurrentPage() -1)*pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return mMapper.getBoardList(map, rb);
	}
	//문의 사항 데이터
	@Override
	public ArrayList<Question> getQuestionList(HashMap<String, Object> map) {
		return mMapper.getQuestionList(map);
	}
	// 일정 요청 리스트 수
	@Override
	public int getReqListCount(int memberNo) {
		return mMapper.getReqListCount(memberNo);
	}
	// 일정 요청 리스트
	@Override
	public ArrayList<ReqSchedule> getReqList(PageInfo pi, int memberNo) {
		int offset = (pi.getCurrentPage() -1)*pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return mMapper.getReqList(memberNo, rb);
	}
	// 일정 요청 플래너
	@Override
	public Planner getReqPlanner(int reqPlaNo) {
		return mMapper.getReqPlanner(reqPlaNo);
	}
	// 일정 상세 조회
	@Override
	public ReqSchedule getReqSchedule(int reqNo) {
		return mMapper.getReqSchedule(reqNo);
	}
	
	@Override
	public Schedule getSchedule(int scheNo) {
		return mMapper.getSchedule(scheNo);
	}
	
	@Override
	public int updateReqState(ReqSchedule req) {
		return mMapper.updateReqState(req);
	}
	
	@Override
	public ArrayList<Plan> getPlanList(int scheNo) {
		return mMapper.getPlanList(scheNo);
	}
	
	//----------------------------------------------------------------
	//플래너페이지
	//플래너 불러오기
	@Override
	public Planner getPalnner(int memberNo) {
		return mMapper.getPlanner(memberNo);
	}
	//플래너 지역이름 불러오기
	@Override
	public Local getLocalName(int memberNo) {
		return mMapper.getLocalName(memberNo);
	}
	//플래너 좋아요 불러오기
	@Override
	public int countLikes(int memberNo) {
		return mMapper.countLikes(memberNo);
	}
	//별점 평균
	@Override
	public Double averageStar(int memberNo) {
		return mMapper.averageStar(memberNo);
	}
	//후기 수
	@Override
	public int getReviewListCount(int memberNo) {
		return mMapper.getReviewListCount(memberNo);
	}
	//후기 불러오기
	@Override
	public ArrayList<Review> getReviewList(PageInfo pi, int memberNo) {
		int offset = (pi.getCurrentPage() -1)*pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return mMapper.getReviewList(memberNo, rb);
	}
	//플래너 프로필이미지 불러오기
	@Override
	public Image getImgRename(HashMap<String, Object> map) {
		return mMapper.getImgRename(map);
	}
	//플래너 취소
	@Override
	public int canclePlanner(HashMap<String, Object> map) {
		return mMapper.canclePlanner(map);
	}
	//----------------------------------------------------------------

	@Override
	public Cancel checkCancel(int reqNo) {
		return mMapper.checkCancel(reqNo);
	}

	@Override
	public int insertCancel(Cancel c) {
		return mMapper.insertCancel(c);
	}

	@Override
	public ArrayList<ReqSchedule> getReqTotalList() {
		return mMapper.getReqTotalList();
	}

	@Override
	public void refundPoint(ReqSchedule r) {
		mMapper.refundPoint(r);
	}

	@Override
	public void updateWarning(ReqSchedule r) {
		mMapper.updateWarning(r);
	}

	@Override
	public void insertCalcultate(ReqSchedule r) {
		mMapper.insertCalcultate(r);
	}

	@Override
	public void updateReqConfirmDate(ReqSchedule r) {
		mMapper.updateReqConfirmDate(r);
	}

	@Override
	public void updatePayPoint(ReqSchedule r) {
		mMapper.updatePayPoint(r);
	}
	
	@Override
	public int getRequestListCount(int pNo) {
		return mMapper.getRequestListCount(pNo);
	}

	@Override
	public ArrayList<ReqSchedule> selectRequestList(int pNo, PageInfo pi) {
		
		int offset = (pi.getCurrentPage()-1)*pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return mMapper.selectRequestList(pNo, rowBounds);
	}
	
	@Override
	public int checkSevenSchedule(Member i) {
		return mMapper.checkSevenSchedule(i);
	}

	

}
