package com.finalproject.triprecord.plan.model.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.plan.model.dao.PlanMapper;
import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

@Service
public class PlanServiceImpl implements PlanService{

	@Autowired
	PlanMapper plMapper;

	@Override
	public ArrayList<HashTag> hashTagList() {
		return plMapper.hashTagList();
	}

	@Override
	public int savePlanInsert(Schedule s, ArrayList<Plan> list, ArrayList<HashTag> tagList) {
		int result = plMapper.saveScheInsert(s);
		if(result > 0) {
			for(int i = 0; i < list.size(); i++) {
				list.get(i).setScNo(s.getScNo());
			}
			
			result = plMapper.savePlanInsert(list);
			
			if(result > 0 && !tagList.get(0).getTagRefType().equals("")) {
				for(int i = 0; i < tagList.size(); i++) {
					tagList.get(i).setTagRefNo(s.getScNo());
				}
				return plMapper.saveHashTagInsert(tagList);
			} else if(result > 0 && tagList.get(0).getTagRefType().equals("")) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	
}
