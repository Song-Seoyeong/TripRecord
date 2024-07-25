package com.finalproject.triprecord.place.model.service;

import java.util.ArrayList;
import java.util.HashMap;

import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.place.model.vo.Place;

public interface PlaceService {

	ArrayList<Local> getLocalList();

	int checkPlace(HashMap<String, Integer> map);

	Place selectPlace(HashMap<String, Integer> map);

	int insertPlace(HashMap<String, Integer> map);

}
