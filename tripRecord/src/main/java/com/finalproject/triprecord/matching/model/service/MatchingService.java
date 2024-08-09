package com.finalproject.triprecord.matching.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.member.model.vo.Planner;

public interface MatchingService {

	int getPlannerListCount(int localNo);

	int countLikes(int pNo);
	
	int checkLikes(HashMap<String, Integer> likemap);
	
	String selectLocalName(int pNo);

	Planner selectPlanner(int pNo);

	ArrayList<Planner> getPlannerList(PageInfo pi, int localNo);

	int insertLikes(HashMap<String, Integer> map);

	int deleteLikes(HashMap<String, Integer> map);

	Double AverageStar(int pNo);

	int getReviewListCount(int pNo);

	ArrayList<Review> getReviewList(PageInfo pi, int pNo);

	ArrayList<Local> selectLocalList();

	int insertReview(Review r);

	int insertImage(ArrayList<Image> list);

	


}
