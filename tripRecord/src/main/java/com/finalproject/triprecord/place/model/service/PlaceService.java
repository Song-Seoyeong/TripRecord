package com.finalproject.triprecord.place.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.place.model.vo.Place;

public interface PlaceService {

	ArrayList<Local> getLocalList();

	int checkPlace(HashMap<String, Integer> map);

	Place selectPlace(HashMap<String, Integer> map);

	int insertPlace(HashMap<String, Integer> map);

	ArrayList<Review> selectReviewList(int contentid);

	int insertReview(Review r);

	int insertImage(ArrayList<Image> list);

	Review selectReview(int rId);

	ArrayList<Image> selectImage(int rId);

}
