package com.finalproject.triprecord.matching.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.matching.model.dao.MatchingMapper;
import com.finalproject.triprecord.matching.model.vo.Planner;

@Service
public class MatchingServiceImpl implements MatchingService {
	
	@Autowired
	private MatchingMapper matMapper;
	
	@Override
	public int getPlannerListCount(int localNo) {
		return matMapper.getPlannerListCount(localNo);
	}

	@Override
	public int countLikes(int pNo) {
		return matMapper.countLikes(pNo);
	}

	@Override
	public String selectLocalName(int pNo) {
		return matMapper.selectLocalName(pNo);
	}

	@Override
	public Planner selectPlanner(int pNo) {
		return matMapper.selectPlanner(pNo);
	}

	@Override
	public ArrayList<Planner> getPlannerList(PageInfo pi, int localNo) {
		
		int offset = (pi.getCurrentPage()-1)*pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return matMapper.getPlannerList(localNo, rowBounds);
	}

	@Override
	public int insertBLikes(HashMap<String, Integer> map) {
		return matMapper.insertBLikes(map);
	}

	@Override
	public int deleteBLikes(HashMap<String, Integer> map) {
		return matMapper.deleteBLikes(map);
	}
}