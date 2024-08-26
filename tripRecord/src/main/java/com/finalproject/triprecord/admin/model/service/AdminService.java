package com.finalproject.triprecord.admin.model.service;

import java.util.ArrayList;

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

public interface AdminService {

	int insertHashTag(HashTag hashTag);

	ArrayList<HashTag> selectHashTagList();

	ArrayList<HashTag> selectTopHashTagList(String tagType);

	int deleteHashTag(int tagNo);

	int getTotalCount();

	Integer getTypeCount(String generalType);

	ArrayList<Board> selectBoardList(PageInfo pi, String search);

	int deleteBoard(int boardNo);

	int updatePoint(Point p);

	ArrayList<Point> selectPointList();

	int changePointStatus(Point p);

	int insertPoint(Point p);

	Point selectTotalPoint();

	ArrayList<Payment> selectTopTen();

	ArrayList<Payment> selectPaymentList();

	int insertNotice(Board b);

	ArrayList<Board> selectNoticeList(PageInfo pi, String boardType);

	Board selectNotice(int boardNo);

	int updateNotice(Board b);

	int deleteNotice(int boardNo);

	ArrayList<Member> selectPlannerList();

	ArrayList<Member> selectAdminList();

	ArrayList<RequestGrade> selectRequestGradeList();

	int updateGrade(int mNo);
	
	void deleteGrade(int mNo);
	
	int gradeDownSuccess(int mNo);

	void deleteRequestGrade(int mNo);

	RequestGrade selectRequestGrade(int mNo);

	int gradeSuccess(Member request);

	void insertPlanner(RequestGrade rg);

	ArrayList<Question> selectQuestionList(PageInfo pi);

	int selectQuestionTotalCount();

	int selectQuestionAnswerCount(String questionSuccess);

	Question selectQuestion(int qNo);

	int insertAnswer(Question q);

	int selectMemberTotalCount();

	int selectMemberGradeCount(String string);

	ArrayList<Member> selectMemberList(PageInfo pi);

	int changeMemberStatus(Member m);

	ArrayList<Content> selectPlacesCount();

	ArrayList<Payment> selectMonthStats();

	int selectGradeCount();

	ArrayList<Image> selectLocalImage();

	int insertImg(Image a);

	void deleteImg(Image i);

	int getListCount(String search);

	int getNoticeListCount(String search);

	int getQuestListCount();

	int getMemberListCount();

	Image selectPlanImage();

	ArrayList<Image> selectMemberProfileList();

	int deleteMemberProfile(int memberNo);

	Member selectMember(String memberId);

	Image selectMemberImage(int memberNo);

	ArrayList<Image> selectBoardImageList();

	ArrayList<Cancel> selectCancelList(PageInfo pi);

	int getCancelListCount();

	ArrayList<Calculate> selectCalculateList();

	int getCalculateListCount();

	ArrayList<Calculate> selectCalculatePageList(PageInfo cPi);

	int updateMemberPoint(Member m);

	ArrayList<ReqSchedule> selectReqScheduleList(PageInfo pi);

	int getReqScheduleListCount();

	ArrayList<Plan> selectPlan(int scNo);

	ReqSchedule selectReqSchedule(int scNo);



}
