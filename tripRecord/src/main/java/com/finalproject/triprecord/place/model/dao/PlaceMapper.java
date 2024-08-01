package com.finalproject.triprecord.place.model.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.place.model.vo.Place;

@Mapper
public interface PlaceMapper {

	ArrayList<Local> getLocalList();

	int checkPlace(HashMap<String, Integer> map);

	Place selectPlace(HashMap<String, Integer> map);

	int insertPlace(HashMap<String, Integer> map);

	void updatePlaceCount(HashMap<String, Integer> map);

	ArrayList<Review> selectReviewList(int contentid);

	int insertMapper(Review r);

	int insertImage(ArrayList<Image> list);

	Review selectReview(int rId);

	ArrayList<Image> selectImage(int rId);

	int deleteReview(int rId);

	int updateReview(Review r);

}
