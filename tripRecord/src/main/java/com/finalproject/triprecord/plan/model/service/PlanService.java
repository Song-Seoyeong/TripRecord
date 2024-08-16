package com.finalproject.triprecord.plan.model.service;

import java.util.ArrayList;
import java.util.Properties;

import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

public interface PlanService {

	int savePlanInsert(Schedule s, ArrayList<Plan> list, ArrayList<HashTag> tagList);

	ArrayList<HashTag> hashTagList();

	ArrayList<Schedule> myTripNoteList(int memberNo, PageInfo pi);

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
