package com.finalproject.triprecord.place.model.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.place.model.dao.PlaceMapper;

@Service
public class PlaceServiceImpl implements PlaceService {
	
	@Autowired
	private PlaceMapper pMapper;

	@Override
	public ArrayList<Local> getLocalList() {
		return pMapper.getLocalList();
	}

}
