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

	int checkPlace(Place ckPla);

	Place selectPlace(Place ckPla);

	int insertPlace(Place ckPla);

	void updatePlaceCount(Place ckPla);

	ArrayList<Review> selectReviewList(int contentid);

	int insertReview(Review r);

	int insertImage(ArrayList<Image> list);

	Review selectReview(int rId);

	ArrayList<Image> selectImage(int rId);

	int deleteReview(int rId);

	int updateReview(Review r);

	int delImg(ArrayList<String> deleteImg);

	int checkImage(int contentid);

	void insertPlaImage(Image i);

	int checkName(Place ckPla);

	void updatePlaName(Place ckPla);

	ArrayList<Local> getTopList();

	ArrayList<Place> topPlaceList();

	ArrayList<Place> selectPlaceList(int localNo);

	Image planImg();

	int checkReview(int memNo);

	void givePoint(int memNo);

}
