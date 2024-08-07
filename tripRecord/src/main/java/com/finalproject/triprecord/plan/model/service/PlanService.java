package com.finalproject.triprecord.plan.model.service;

import java.util.ArrayList;

import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

public interface PlanService {

	int savePlanInsert(Schedule s, ArrayList<Plan> list, String[] hashtag);

}
