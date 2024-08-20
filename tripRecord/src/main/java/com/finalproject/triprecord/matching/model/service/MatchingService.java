package com.finalproject.triprecord.matching.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.model.vo.Planner;
import com.finalproject.triprecord.plan.model.vo.Schedule;

public interface MatchingService {

	int getPlannerListCount(int localNo);
	
	ArrayList<Planner> getPlannerList(PageInfo pi, int localNo);

	int countLikes(int pNo);
	
	int checkLikes(HashMap<String, Integer> likemap);
	
	String selectLocalName(int pNo);

	Planner selectPlanner(int pNo);

	int insertLikes(HashMap<String, Integer> map);

	int deleteLikes(HashMap<String, Integer> map);

	Double AverageStar(int pNo);

	int getReviewListCount(int pNo);

	ArrayList<Review> getReviewList(PageInfo pi, int pNo);

	ArrayList<Local> selectLocalList();

	int insertReview(Review r);

	int insertReviewImage(ArrayList<Image> list);

	ArrayList<Image> selectrImgList();
	
	ArrayList<Image> selectrImgList(int rNo);
	
	ArrayList<Image> selectiImgList(int pNo);

	Review selectReview(int rNo);
	
	int deletePlannerReview(int rNo);
	
	int deleteReviewImage(ArrayList<String> deleteImg);
	
	int insertSchedule(Schedule schedule);

	int insertReqSchedule(ReqSchedule reqSchedule);

	ArrayList<Planner> topPlannerList();

	ArrayList<HashTag> selectTagList(int pNo);

	int checkPoint(HashMap<String, Integer> map);

	int requestPayment(HashMap<String, Integer> map);

	

}
