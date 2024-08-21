package com.finalproject.triprecord.reconciliation.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.finalproject.triprecord.member.model.vo.Calculate;

@Mapper
public interface ReconciliationMapper {

	ArrayList<Calculate> getCalcList();

	int updatePlannerPoint(Calculate requireCalcData);

	int updateCalcStatus(Calculate requireCalcData);



}
