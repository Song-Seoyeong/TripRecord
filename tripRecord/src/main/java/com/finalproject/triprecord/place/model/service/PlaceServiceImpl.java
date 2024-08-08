package com.finalproject.triprecord.place.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.common.model.vo.Image;
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
	public int checkPlace(Place ckPla) {
		return pMapper.checkPlace(ckPla);
	}

	@Override
	public Place selectPlace(Place ckPla) {
		 Place p =pMapper.selectPlace(ckPla);
		 pMapper.updatePlaceCount(ckPla);
		 p.setPlaceCount(p.getPlaceCount() + 1);
		 return p;
	}

	@Override
	public int insertPlace(Place ckPla) {
		return pMapper.insertPlace(ckPla);
	}

	@Override
	public ArrayList<Review> selectReviewList(int contentid) {
		return pMapper.selectReviewList(contentid);
	}

	@Override
	public int insertReview(Review r) {
		return pMapper.insertReview(r);
	}

	@Override
	public int insertImage(ArrayList<Image> list) {
		return pMapper.insertImage(list);
	}

	@Override
	public Review selectReview(int rId) {
		return pMapper.selectReview(rId);
	}

	@Override
	public ArrayList<Image> selectImage(int rId) {
		return pMapper.selectImage(rId);
	}

	@Override
	public int deleteReview(int rId) {
		return pMapper.deleteReview(rId);
	}

	@Override
	public int updateReview(Review r) {
		return pMapper.updateReview(r);
	}

	@Override
	public int delImg(ArrayList<String> deleteImg) {
		return pMapper.delImg(deleteImg);
	}

	@Override
	public int checkImage(int contentid) {
		return pMapper.checkImage(contentid);
	}

	@Override
	public void insertPlaImage(Image i) {
		pMapper.insertPlaImage(i);
	}

	@Override
	public int checkName(Place ckPla) {
		return pMapper.checkName(ckPla);
	}

	@Override
	public void updatePlaName(Place ckPla) {
		pMapper.updatePlaName(ckPla);
	}

	@Override
	public Place selectPlaceImg(Place p) {
		return pMapper.selectPlace(p);
	}

	@Override
	public ArrayList<Local> getTopList() {
		return pMapper.getTopList();
	}

	@Override
	public ArrayList<Place> topPlaceList() {
		return pMapper.topPlaceList();
	}

	@Override
	public ArrayList<Place> selectPlaceList(int localNo) {
		return pMapper.selectPlaceList(localNo);
	}

}
