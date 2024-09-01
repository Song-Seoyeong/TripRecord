package com.finalproject.triprecord.reconciliation.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.finalproject.triprecord.member.model.vo.Calculate;
import com.finalproject.triprecord.reconciliation.model.service.ReconciliationService;

@Controller
public class ReconciliationController {
	
	@Autowired
	private ReconciliationService rcService;
	
	//매월 마지막일 9시에 정산 cron = "0 0 9 L * ?" 10초마다 정산 fixedDelay = 1000
	@Scheduled(cron = "59 59 23 L * ?")
	public void Reconciliation() {
		//월말(10초)지나면 calculate가 된 멤버번호가 일치하는 플래너에게 일괄 지급
		ArrayList<Calculate> calcList = rcService.getCalcList();
		for(int i = 0; i < calcList.size(); i++) {
			int memberNo = calcList.get(i).getPlannerNo();
			int exMoney = calcList.get(i).getExpectedMoney();
			
			Calculate requireCalcData = new Calculate();
			requireCalcData.setPlannerNo(memberNo);
			requireCalcData.setExpectedMoney(exMoney);
			
			int upResult = rcService.updatePlannerPoint(requireCalcData);
			int sResult = rcService.updateCalcStatus(requireCalcData);
			
		}	
	}
	
}
