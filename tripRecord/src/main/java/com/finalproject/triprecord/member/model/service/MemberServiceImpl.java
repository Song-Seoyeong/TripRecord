package com.finalproject.triprecord.member.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.admin.model.vo.RequestGrade;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.Question;
import com.finalproject.triprecord.common.model.vo.Cancel;
import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.common.model.vo.Point;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.model.dao.MemberMapper;
import com.finalproject.triprecord.member.model.vo.Calculate;
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
	public int getReqListCount(ReqSchedule rs) {
		return mMapper.getReqListCount(rs);
	}
	// 일정 요청 리스트
	@Override
	public ArrayList<ReqSchedule> getReqList(PageInfo pi, ReqSchedule rs) {
		int offset = (pi.getCurrentPage() -1)*pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return mMapper.getReqList(rs, rb);
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
	public Planner getPlanner(int memberNo) {
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
	public int cancelPlanner(HashMap<String, Object> map) {
		return mMapper.cancelPlanner(map);
	}
	//----------------------------------------------------------------

	@Override
	public Cancel checkCancel(ReqSchedule rs) {
		return mMapper.checkCancel(rs);
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

	// 플래너의 일정 요청 상세 불러오기
	@Override
	public ReqSchedule detailRequest(HashMap<String, Integer> map) {
		return mMapper.detailRequest(map);
	}

	@Override
	public void updateSchedule(ReqSchedule r) {
		mMapper.updateSchedule(r);
	}
	
	@Override
	public Schedule detailSchedule(int scheNo) {
		return mMapper.detailSchedule(scheNo);
	}

	@Override
	public int reqPlanInsUpd(ArrayList<Plan> plList) {
		int result = mMapper.reqPlanInsert(plList); // plan Insert
		if(result > 0) {
			return mMapper.reqScheUpdate(plList.get(0).getScNo()); // 진행상태 2
		} else {
			return 0;
		}
	}

	@Override
	public int cancleRequest(int scNo) {
		int result = mMapper.cancleRequest(scNo); // 진행상태 4
		if(result > 0) {
			return mMapper.scDelStaUpdate(scNo); // 삭제
		} else {
			return 0;
			
		}
	}
	
	// 본인 작성 리뷰 전체
	@Override
	public int getPlaceReviewListCount(int memberNo) {
		return mMapper.getPlaceReviewListCount(memberNo);
	}
	@Override
	public ArrayList<Review> getPlaceReviewList(int memberNo, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return mMapper.getPlaceReviewList(memberNo, rb);
	}
	//플래너 후기 수 불러오기
	@Override
	public int getPlannerReviewListCount(int memberNo) {
		return mMapper.getPlannerReviewListCount(memberNo);
	}
	//플래너 후기 불러오기
	@Override
	public ArrayList<Review> getPlannerReviewList(int memberNo, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return mMapper.getPlannerReviewList(memberNo, rb);
	}
	
	// 해쉬태그 불러오기
	@Override
	public ArrayList<HashTag> getHashTag(int memberNo) {
		return mMapper.getHashTag(memberNo);
	}

	// 플래너 인트로 업데이트
	@Override
	public int updatePlannerIntro(Planner planner) {
		return mMapper.updatePlannerIntro(planner);
	}

	// 소개 사진 유무 확인
	@Override
	public int checkIntroImg(int memberNo) {
		return mMapper.checkIntroImg(memberNo);
	}

	// 플래너 이전 사진 rename 가져오기
	@Override
	public Image existPlannerFileId(int memberNo) {
		return mMapper.existPlannerFileId(memberNo);
	}

	// 플래너 이전 사진 데이터 db 삭제
	@Override
	public int deletePlannerProfile(int memberNo) {
		return mMapper.deletePlannerProfile(memberNo);
	}

	@Override
	public ArrayList<HashTag> getTags() {
		return mMapper.getTags();
	}

	// 해쉬태그 업데이트
	@Override
	public int updateTag(HashMap<String, Object> pMap) {
		return mMapper.updateTag(pMap);
	}

	@Override
	public int deleteTag(int memberNo) {
		return mMapper.deleteTag(memberNo);
	}

	// 은행 수정
	@Override
	public int updateAccount(HashMap<String, Object> pMap) {
		return mMapper.updateAccount(pMap);
	}

	// 정산 리스트 불러오기
	@Override
	public ArrayList<Calculate> getCalcList(PageInfo pi, int memberNo) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rb = new RowBounds(offset, pi.getBoardLimit());
		return mMapper.getCalcList(memberNo, rb);
	}

	// 정산 리스트 수
	@Override
	public int calcListCount(int memberNo) {
		return mMapper.calcListCount(memberNo);
	}

	@Override
	public int updateScheduleWriter(ReqSchedule r) {
		//System.out.println(r);
		return mMapper.updateScheduleWriter(r);
	}
	
	// Plan 넣기
	@Override
	public int reqPlanInsert(ArrayList<Plan> plList) {
		return mMapper.reqPlanInsert(plList); // plan Insert
	}

	// 메모 넣기, 진행 상태, 날짜 변경
	@Override
	public int reqScheUpdate(ReqSchedule rs) {
		return mMapper.reqScheUpdate(rs);
	}
	
	// del_status N 변경
	@Override
	public int scDelStaUpdate(int scheNo) {
		return mMapper.scDelStaUpdate(scheNo);
	}
	
	// 마이페이지 -> 내 일정 상세 보기 -> 지역 수정
	@Override
	public int updateTripLocal(Properties prop) {
		return mMapper.updateTripLocal(prop);
	}

	@Override
	public int checkReviewCount(ReqSchedule rs) {
		return mMapper.checkReviewCount(rs);
	}
	
	// 마이페이지 -> 내 일정 상세 보기 -> 새로운 일정 추가
	@Override
	public int insertPlan(Plan p) {
		return mMapper.insertPlan(p);
	}
	
	//플래너 탈퇴 시 삭제
	@Override
	public int deletePlanner(Member m) {
		return mMapper.deletePlanner(m);
	}
	
	// 플래너 페이지 -> 요청 상세 -> 요청 취소일 시 사유 가지고 오기
	@Override
	public Cancel cancelReqSelect(int reqNo) {
		return mMapper.cancelReqSelect(reqNo);
	}
}
