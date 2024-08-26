package com.finalproject.triprecord.admin.model.service;

import java.util.ArrayList;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.admin.model.dao.AdminMapper;
import com.finalproject.triprecord.admin.model.vo.RequestGrade;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.Question;
import com.finalproject.triprecord.common.model.vo.Cancel;
import com.finalproject.triprecord.common.model.vo.Content;
import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.common.model.vo.Point;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.model.vo.Calculate;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.plan.model.vo.Plan;

@Service
public class AdminServiceImpl implements AdminService{
	
	@Autowired
	private AdminMapper aMapper;
	
	@Override
	public int insertHashTag(HashTag hashTag) {
		return aMapper.insertHashTag(hashTag);
	}
	
	@Override
	public ArrayList<HashTag> selectHashTagList() {
		return aMapper.selectHashTagList();
	}
	
	@Override
	public ArrayList<HashTag> selectTopHashTagList(String tagType) {
		return aMapper.selectTopHashtagList(tagType);
	}
	
	@Override
	public int deleteHashTag(int tagNo) {
		return aMapper.deleteHashTag(tagNo);
	}
	
	@Override
	public int getTotalCount() {
		return aMapper.getTotalCount();
	}
	
	@Override
	public Integer getTypeCount(String generalType) {
		return aMapper.getTypeCount(generalType);
	}
	
	@Override
	public ArrayList<Board> selectBoardList(PageInfo pi, String search) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return aMapper.selectBoardList(search, rowBounds);
	}
	
	@Override
	public int deleteBoard(int boardNo) {
		return aMapper.deleteBoard(boardNo);
	}
	
	@Override
	public int updatePoint(Point p) {
		return aMapper.updatePoint(p);
	}
	
	@Override
	public ArrayList<Point> selectPointList() {
		return aMapper.selectPointList();
	}
	
	@Override
	public int changePointStatus(Point p) {
		return aMapper.changePointStatus(p);
	}
	
	@Override
	public int insertPoint(Point p) {
		return aMapper.insertPoint(p);
	}
	
	@Override
	public Point selectTotalPoint() {
		return aMapper.selectTotalPoint();
	}
	
	@Override
	public ArrayList<Payment> selectTopTen() {
		return aMapper.selectTopTen();
	}
	
	@Override
	public ArrayList<Payment> selectPaymentList() {
		return aMapper.selectPaymentList();
	}
	
	@Override
	public int insertNotice(Board b) {
		return aMapper.insertNotice(b);
	}
	
	@Override
	public ArrayList<Board> selectNoticeList(PageInfo pi, String boardType) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return aMapper.selectNoticeList(rowBounds, boardType);
	}
	
	@Override
	public Board selectNotice(int boardNo) {
		return aMapper.selectNotice(boardNo);
	}
	
	@Override
	public int updateNotice(Board b) {
		return aMapper.updateNotice(b);
	}
	
	@Override
	public int deleteNotice(int boardNo) {
		return aMapper.deleteNotice(boardNo);
	}
	
	@Override
	public ArrayList<Member> selectPlannerList() {
		return aMapper.selectPlannerList();
	}
	
	@Override
	public ArrayList<Member> selectAdminList() {
		return aMapper.selectAdminList();
	}
	
	@Override
	public ArrayList<RequestGrade> selectRequestGradeList() {
		return aMapper.selectRequestGradeList();
	}
	
	@Override
	public int updateGrade(int mNo) {
		return aMapper.updateGrade(mNo);
	}
	
	@Override
	public void deleteGrade(int mNo) {
		aMapper.deleteGrade(mNo);
	}
	
	@Override
	public int gradeDownSuccess(int mNo) {
		return aMapper.gradeDownSuccess(mNo);
	}
	
	@Override
	public void deleteRequestGrade(int mNo) {
		aMapper.deleteRequestGrade(mNo);
	}
	
	@Override
	public RequestGrade selectRequestGrade(int mNo) {
		return aMapper.selectRequestGrade(mNo);
	}
	
	@Override
	public int gradeSuccess(Member request) {
		return aMapper.gradeSuccess(request);
	}
	
	@Override
	public void insertPlanner(RequestGrade rg) {
		aMapper.insertPlanner(rg);
	}
	
	@Override
	public ArrayList<Question> selectQuestionList(PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return aMapper.selectQuestionList(rowBounds);
	}
	
	@Override
	public int selectQuestionTotalCount() {
		return aMapper.selectQuestionTotalCount();
	}
	
	@Override
	public int selectQuestionAnswerCount(String questionSuccess) {
		return aMapper.selectQuestionAnswerCount(questionSuccess);
	}
	
	@Override
	public Question selectQuestion(int qNo) {
		return aMapper.selectQuestion(qNo);
	}
	
	@Override
	public int insertAnswer(Question q) {
		return aMapper.insertAnswer(q);
	}
	
	@Override
	public int selectMemberTotalCount() {
		return aMapper.selectMemberTotalCount();
	}
	
	@Override
	public int selectMemberGradeCount(String grade) {
		return aMapper.selectMemberGradeCount(grade);
	}
	
	@Override
	public ArrayList<Member> selectMemberList(PageInfo pi) {
		
		if(pi != null) {
			int offset = (pi.getCurrentPage() - 1)*pi.getBoardLimit();
			RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
			
			return aMapper.selectMemberList(rowBounds);
		} else {
			return aMapper.selectMemberList();
		}
				
	}
	
	@Override
	public int changeMemberStatus(Member m) {
		return aMapper.changeMemberStatus(m);
	}
	
	@Override
	public ArrayList<Content> selectPlacesCount() {
		return aMapper.selectPlacesCount();
	}
	
	@Override
	public ArrayList<Payment> selectMonthStats() {
		return aMapper.selectMonthStats();
	}
	
	@Override
	public int selectGradeCount() {
		return aMapper.selectGradeCount();
	}
	
	@Override
	public ArrayList<Image> selectLocalImage() {
		return aMapper.selectLocalImage();
	}
	
	@Override
	public int insertImg(Image a) {
		return aMapper.insertImg(a);
	}
	
	@Override
	public void deleteImg(Image i) {
		aMapper.deleteImg(i);
	}
	
	@Override
	public int getListCount(String search) {
		return aMapper.getListCount(search);
	}
	
	@Override
	public int getNoticeListCount(String search) {
		return aMapper.getNoticeListCount(search);
	}
	
	@Override
	public int getQuestListCount() {
		return aMapper.getQuestListCount();
	}
	
	@Override
	public int getMemberListCount() {
		return aMapper.getMemberListCount();
	}
	
	@Override
	public Image selectPlanImage() {
		return aMapper.selectPlanImage();
	}
	
	@Override
	public ArrayList<Image> selectMemberProfileList() {
		return aMapper.selectMemberProfileList();
	}
	
	@Override
	public int deleteMemberProfile(int MemberNo) {
		return aMapper.deleteMemberProfile(MemberNo);
	}
	
	@Override
	public Member selectMember(String memberId) {
		return aMapper.selectMember(memberId);
	}
	
	@Override
	public Image selectMemberImage(int memberNo) {
		return aMapper.selectMemberImage(memberNo);
	}
	
	@Override
	public ArrayList<Image> selectBoardImageList() {
		return aMapper.selectBoardImageList();
	}
	
	@Override
	public ArrayList<Cancel> selectCancelList(PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1)*pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return aMapper.selectCancelList(rowBounds);
	}
	
	@Override
	public int getCancelListCount() {
		return aMapper.getCancelListCount();
	}
	
	@Override
	public ArrayList<Calculate> selectCalculateList() {
		return aMapper.selectCalculateList();
	}
	
	@Override
	public int getCalculateListCount() {
		return aMapper.getCalculateListCount();
	}
	
	@Override
	public ArrayList<Calculate> selectCalculatePageList(PageInfo cPi) {
		int offset = (cPi.getCurrentPage() - 1) * cPi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, cPi.getBoardLimit());
		
		return aMapper.selectCalculatePageList(rowBounds);
	}
	
	@Override
	public int updateMemberPoint(Member m) {
		return aMapper.updateMemberPoint(m);
	}
	
	@Override
	public ArrayList<ReqSchedule> selectReqScheduleList(PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1) * pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return aMapper.selectReqScheduleList(rowBounds);
	}
	
	@Override
	public int getReqScheduleListCount() {
		return aMapper.getReqScheduleListCount();
	}
	
	@Override
	public ArrayList<Plan> selectPlan(int scNo) {
		return aMapper.selectPlan(scNo);
	}
	
	@Override
	public ReqSchedule selectReqSchedule(int scNo) {
		return aMapper.selectReqSchedule(scNo);
	}
	
}
