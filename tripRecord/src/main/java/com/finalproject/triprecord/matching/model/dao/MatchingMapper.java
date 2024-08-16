package com.finalproject.triprecord.matching.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.model.vo.Planner;
import com.finalproject.triprecord.plan.model.vo.Schedule;

@Mapper
public interface MatchingMapper {

	int getPlannerListCount(int localNo);

	ArrayList<Planner> getPlannerList(int localNo, RowBounds rowBounds);

	int countLikes(int pNo);
	
	int checkLikes(HashMap<String, Integer> likemap);

	String selectLocalName();
	
	String selectLocalName(int pNo);

	Planner selectPlanner(int pNo);

	int insertLikes(HashMap<String, Integer> map);

	int deleteLikes(HashMap<String, Integer> map);

	Double AverageStar(int pNo);

	int getReviewListCount(int pNo);

	ArrayList<Review> getReviewList(int pNo, RowBounds rowBounds);

	ArrayList<Local> selectLocalList();

	int insertReview(Review r);

	int insertReviewImage(ArrayList<Image> list);

	ArrayList<Image> selectrImgList();
	
	ArrayList<Image> selectrImgList(int rNo);

	Review selectReview(int rNo);

	int deletePlannerReview(int rNo);

	int insertReqSchedule(ReqSchedule reqSchedule);

	int insertSchedule(Schedule schedule);

	int deleteReviewImage(ArrayList<String> deleteImg);

	ArrayList<Planner> topPlannerList();

	ArrayList<HashTag> selectTagList(int pNo);

}
