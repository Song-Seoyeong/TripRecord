package com.finalproject.triprecord.place.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.place.model.dao.PlaceMapper;
import com.finalproject.triprecord.place.model.vo.Place;

@Service
public class PlaceServiceImpl implements PlaceService {
	
	@Autowired
	private PlaceMapper pMapper;

	@Override
	public ArrayList<Local> getLocalList() {
		return pMapper.getLocalList();
	}

	@Override
	public int checkPlace(HashMap<String, Integer> map) {
		return pMapper.checkPlace(map);
	}

	@Override
	public Place selectPlace(HashMap<String, Integer> map) {
		 Place p =pMapper.selectPlace(map);
		 pMapper.updatePlaceCount(map);
		 p.setPlaceCount(p.getPlaceCount() + 1);
		 return p;
	}

	@Override
	public int insertPlace(HashMap<String, Integer> map) {
		return pMapper.insertPlace(map);
	}

	@Override
	public ArrayList<Review> selectReviewList(int contentid) {
		return pMapper.selectReviewList(contentid);
	}

}
