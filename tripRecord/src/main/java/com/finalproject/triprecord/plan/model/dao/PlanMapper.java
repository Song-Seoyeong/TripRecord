package com.finalproject.triprecord.plan.model.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

@Mapper
public interface PlanMapper {

	int saveScheInsert(Schedule s);

	int savePlanInsert(ArrayList<Plan> list);

//	int saveHashTagInsert(String[] hashtag);
	
}
