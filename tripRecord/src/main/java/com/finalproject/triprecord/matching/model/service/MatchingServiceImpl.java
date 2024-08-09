package com.finalproject.triprecord.matching.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.matching.model.dao.MatchingMapper;
import com.finalproject.triprecord.member.model.vo.Planner;

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
	public int checkLikes(HashMap<String, Integer> likemap) {
		return matMapper.checkLikes(likemap);
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
	public int insertLikes(HashMap<String, Integer> map) {
		return matMapper.insertLikes(map);
	}

	@Override
	public int deleteLikes(HashMap<String, Integer> map) {
		return matMapper.deleteLikes(map);
	}

	@Override
	public Double AverageStar(int pNo) {
		return matMapper.AverageStar(pNo);
	}

	@Override
	public int getReviewListCount(int pNo) {
		return matMapper.getReviewListCount(pNo);
	}

	@Override
	public ArrayList<Review> getReviewList(PageInfo pi, int pNo) {
		
		int offset = (pi.getCurrentPage()-1)*pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		
		return matMapper.getReviewList(pNo, rowBounds);
	}

	@Override
	public ArrayList<Local> selectLocalList() {
		return matMapper.selectLocalList();
	}

	@Override
	public int insertReview(Review r) {
		return matMapper.insertReview(r);
	}

	@Override
	public int insertImage(ArrayList<Image> list) {
		return matMapper.insertImage(list);
	}


}
