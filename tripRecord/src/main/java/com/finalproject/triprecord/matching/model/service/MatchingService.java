package com.finalproject.triprecord.matching.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.matching.model.vo.Planner;

public interface MatchingService {

	int getPlannerListCount(int localNo);

	int countLikes(int pNo);

	String selectLocalName(int pNo);

	Planner selectPlanner(int pNo);

	ArrayList<Planner> getPlannerList(PageInfo pi, int localNo);

	int insertBLikes(HashMap<String, Integer> map);

	int deleteBLikes(HashMap<String, Integer> map);


}
