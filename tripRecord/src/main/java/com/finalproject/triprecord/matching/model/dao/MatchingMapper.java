package com.finalproject.triprecord.matching.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.finalproject.triprecord.matching.model.vo.Planner;

@Mapper
public interface MatchingMapper {

	int getPlannerListCount(int localNo);

	ArrayList<Planner> getPlannerList(int localNo, RowBounds rowBounds);

	int countLikes(int pNo);

	String selectLocalName(int pNo);

	Planner selectPlanner(int pNo);

	int insertBLikes(HashMap<String, Integer> map);

	int deleteBLikes(HashMap<String, Integer> map);
}
