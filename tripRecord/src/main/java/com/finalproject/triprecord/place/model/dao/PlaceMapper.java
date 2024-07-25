package com.finalproject.triprecord.place.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.finalproject.triprecord.common.model.vo.Local;

@Mapper
public interface PlaceMapper {

	ArrayList<Local> getLocalList();

}
