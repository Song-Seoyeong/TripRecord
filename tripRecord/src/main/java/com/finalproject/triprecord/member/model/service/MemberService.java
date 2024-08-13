package com.finalproject.triprecord.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.finalproject.triprecord.admin.model.vo.RequestGrade;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.Question;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.common.model.vo.Point;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;
import com.finalproject.triprecord.plan.model.vo.Schedule;

public interface MemberService {

	int enrollMember(Member m);

	Member login(Member m);

	int enrollPlanner(Planner p);

	int checkId(String id);
	
	Member findId(Member m);
	
	Member findPwd(Member m);
	
	int updatePwd(Member m);
	
	//마이페이지------------------------------------
	//회원 정보 불러오기 
	Member getMember(int memberNo);
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
	//포인트 목록 불러오기
	ArrayList<Point> selectPointList();
	//결제 내역 불러오기
	ArrayList<Payment> getPaymentList(PageInfo pi, int memberNo);
	//결제 내역 수 불러오기
	int pmListCount(int memberNo);
	//게시글 목록 수 불러오기
	int getListCount(HashMap<String, Object> map);
	//자기 문의글 불러오기
	ArrayList<Board> getBoardList(HashMap<String, Object> map, PageInfo pi);
	//문의 사항 데이터
	ArrayList<Question> getQuestionList(HashMap<String, Object> map);
	//일정요청 리스트 수
	int getReqListCount(int memberNo);
	// 일정 리스트
	ArrayList<ReqSchedule> getReqList(PageInfo pi, int memberNo);
	// 일정 요청 플래너 리스트
	Planner getReqPlanner(int reqPlaNo);
	// 일정 상세
	ReqSchedule getReqSchedule(int reqNo);

	Schedule getSchedule(int scheNo);

	int updateReqState(ReqSchedule req);
	//-------------------------------------------------------------
	//플래너페이지
	//플래너 불러오기
	Planner getPalnner(int memberNo);
	//플래너 지역 이름 불러오기
	Local getLocalName(int memberNo);
	//플래너 좋아요 불러오기
	int countLikes(int memberNo);
	//별정 평균
	Double averageStar(int memberNo);
	//후기 수 
	int getReviewListCount(int memberNo);
	//후기 불러오기
	ArrayList<Review> getReviewList(PageInfo pi, int memberNo);
	//플래너 프로필 이미지 불러오기
	Image getImgRename(HashMap<String, Object> map);
	//플래너 취소
	int canclePlanner(HashMap<String, Object> map);




	
	

	
	
	//-------------------------------------------------------------
	

	


	

}
