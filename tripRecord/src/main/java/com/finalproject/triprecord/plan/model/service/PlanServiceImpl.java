package com.finalproject.triprecord.plan.model.service;

import java.util.ArrayList;
import java.util.Properties;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.PageInfo;
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
			
			if(result > 0 && !tagList.contains(null)) { // controller 에서 해시태그 선택 안 하면 null 담아뒀음
				for(int i = 0; i < tagList.size(); i++) {
					tagList.get(i).setTagRefNo(s.getScNo());
				}
				return plMapper.saveHashTagInsert(tagList);
				
			} else if(result > 0 && tagList.contains(null)) {
				return 1;
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}

	@Override
	public ArrayList<Schedule> myTripNoteList(int memberNo, PageInfo pi) {
		int offset = (pi.getCurrentPage() - 1)*pi.getBoardLimit();
		RowBounds rowBounds = new RowBounds(offset, pi.getBoardLimit());
		return plMapper.myTripNoteList(rowBounds, memberNo);
	}

	@Override
	public ArrayList<HashTag> myTripTagList(ArrayList<Schedule> sList) {
		return plMapper.myTripTagList(sList);
	}

	@Override
	public int getListCount(int memberNo) {
		return plMapper.getListCount(memberNo);
	}

	@Override
	public Schedule detailMySchedule(int scNo) {
		return plMapper.detailMySchedule(scNo);
	}

	@Override
	public ArrayList<Plan> detailMyTripNote(int scNo) {
		return plMapper.detailMyTripNote(scNo);
	}

	@Override
	public int deleteTripNote(int scNo) {
		return plMapper.deleteTripNote(scNo);
	}

	@Override
	public int detailTripUpdate(Properties prop) {
		return plMapper.detailTripUpdate(prop);
	}

	@Override
	public int updateReserve(Properties prop) {
		return plMapper.updateReserve(prop);
	}

	
}
