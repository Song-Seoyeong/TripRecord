package com.finalproject.triprecord.plan.model.dao;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

@Mapper
public interface PlanMapper {

	ArrayList<HashTag> hashTagList();
	
	int saveScheInsert(Schedule s);

	int savePlanInsert(ArrayList<Plan> list);

	int saveHashTagInsert(ArrayList<HashTag> tagList);

	ArrayList<Schedule> myTripNoteList(RowBounds rowBounds, int memberNo);

	ArrayList<HashTag> myTripTagList(ArrayList<Schedule> sList);

	int getListCount(int memberNo);

	Schedule detailMySchedule(int scNo);

	ArrayList<Plan> detailMyTripNote(int scNo);

	int deleteTripNote(int scNo);

	int detailTripUpdate(Properties prop);

	int updateReserve(Properties prop);

	int scheduleCount(int memberNo);

	void updatePoint(int memberNo);

	int detailDeletePlan(String plNo);

	int selectPlanCount(Member loginUser);

	
}
