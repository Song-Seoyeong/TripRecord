package com.finalproject.triprecord.plan.model.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.plan.model.dao.PlanMapper;
import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

@Service
public class PlanServiceImpl implements PlanService{

	@Autowired
	PlanMapper plMapper;
	
	@Override
	public int savePlanInsert(Schedule s, ArrayList<Plan> list, String[] hashtag) {
		int result = plMapper.saveScheInsert(s);
		if(result > 0) {
			for(int i = 0; i < list.size(); i++) {
				list.get(i).setScNo(s.getScNo());
			}
//			result = plMapper.savePlanInsert(list);
			return plMapper.savePlanInsert(list);
//			if(result > 0) {
//				return plMapper.saveHashTagInsert(hashtag);
//			} else {
//				return 0;
//			}
		} else {
			return 0;
		}
	}
	
}
