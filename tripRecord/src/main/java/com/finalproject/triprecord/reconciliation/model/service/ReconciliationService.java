package com.finalproject.triprecord.reconciliation.model.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.member.model.vo.Calculate;
import com.finalproject.triprecord.reconciliation.model.dao.ReconciliationMapper;

@Service
public class ReconciliationService {
	@Autowired
	private ReconciliationMapper rcMapper;

	public ArrayList<Calculate> getCalcList() {
		return rcMapper.getCalcList();
	}

	public int updatePlannerPoint(Calculate requireCalcData) {
		return rcMapper.updatePlannerPoint(requireCalcData);
	}

	public int updateCalcStatus(Calculate requireCalcData) {
		return rcMapper.updateCalcStatus(requireCalcData);
	}
	

}
