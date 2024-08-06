package com.finalproject.triprecord.matching.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.member.model.vo.Planner;

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



}
