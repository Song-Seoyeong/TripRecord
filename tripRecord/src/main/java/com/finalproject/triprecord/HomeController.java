package com.finalproject.triprecord;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalproject.triprecord.common.controller.SendMessegeController;
import com.finalproject.triprecord.common.model.vo.Cancel;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.matching.model.service.MatchingService;
import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.model.service.MemberService;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;
import com.finalproject.triprecord.place.model.service.PlaceService;
import com.finalproject.triprecord.place.model.vo.Place;
import com.finalproject.triprecord.plan.model.service.PlanService;
import com.finalproject.triprecord.plan.model.vo.Schedule;

import jakarta.servlet.http.HttpSession;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

@Controller
public class HomeController {
	
	@Autowired
	private MatchingService matService;
	
	@Autowired
	private PlaceService pService;
	
	@Autowired
	private MemberService mService;
	
	@Autowired
	private PlanService plService;
	
	@GetMapping("home")
	public String home(Model model, HttpSession session) {
		
		// 여행 지역 조회수 top 5
		ArrayList<Local> lList = pService.getTopList();
		
		// 일정 이미지
		Image img = pService.planImg();
		
		// 플래너 top 5 (좋아요순)
		ArrayList<Planner> plannerList = matService.topPlannerList();
		
		// 관광지 top 5
		ArrayList<Place> pList = pService.topPlaceList();
		
		int planCount = 0;
		Member loginUser = (Member)session.getAttribute("loginUser");
		if(loginUser != null) {
			if(loginUser.getGrade().equals("GENERAL")) {
				planCount = plService.selectPlanCount(loginUser);
			} else if (loginUser.getGrade().equals("PLANNER")) {
				planCount = plService.selectPlanCount(loginUser);
			}
		}
		model.addAttribute("planCount", planCount);
		
		model.addAttribute("lList", lList);
		model.addAttribute("img", img);
		model.addAttribute("plList", plannerList);
		model.addAttribute("pList", pList);
		return "home";
	}
	
	@GetMapping("updateReqStatus.mp")
	@ResponseBody
	public String updateReqStatus() {
		ArrayList<ReqSchedule> rList = mService.getReqTotalList();
		
		int result = 0;
		SendMessegeController sm = new SendMessegeController();
		SingleMessageSentResponse sms = null;
			
		if(!rList.isEmpty()) {
			// 일정 생성 이전 리스트
			ArrayList<ReqSchedule> beforeSchList = new ArrayList<ReqSchedule>();
			
			// 일정 생성 이후 리스트
			ArrayList<ReqSchedule> afterSchList = new ArrayList<ReqSchedule>();
			try {
				
				for(ReqSchedule r : rList) {
					// 신청별 schedule
					Schedule sch = mService.getSchedule(r.getScheNo());
					
					// 날짜 체크
					LocalDate localDate = sch.getScStartDate().toLocalDate();
		
			        // 오늘 날짜 가져오기
			        LocalDate today = LocalDate.now();
		
			        // 날짜 비교
			        if (localDate.isBefore(today) || localDate.isEqual(today)) {
			        	if(r.getReqStatus() == 1) beforeSchList.add(r);
			        	else afterSchList.add(r);
			        }
				}
				
				if(!beforeSchList.isEmpty()) {
					for(ReqSchedule r : beforeSchList) {
						// 진행 상태 취소로 변경
						r.setReqStatus(4); 
						result += mService.updateReqState(r);
						
						
						r.setPayPoint((int)(r.getPayPoint()*1.1));
						
						// 포인트 환불
						mService.refundPoint(r);
						
						// 환불 확정 날짜 + 환불 금액으로 변경
						mService.updateReqConfirmDate(r);
						mService.updatePayPoint(r);
						
						// 플래너 경고
						mService.updateWarning(r);
						
						// 취소 사유 추가
						Cancel c = new Cancel();
						c.setCancelMemNo(r.getReqPlaNo());
						c.setCancelRefType("REQSCHEDULE");
						c.setCancelRefNo(r.getReqNo());
						c.setCancelComent("죄송합니다. 일정 생성 기간이 지나 자동으로 결제한 포인트의 110%가 환불되었습니다.<br> - 환불된 포인트 : " + (int)r.getPayPoint());
						mService.insertCancel(c);
						
						Member m = new Member();
						
						// 회원
						m = mService.getMember(r.getReqMemNo());
						sms =	sm.sendMmsByResourcePath(r, m, 1);
						
						// 플래너
						m = mService.getMember(r.getReqPlaNo());
						sms =	sm.sendMmsByResourcePath(r, m, 2);
					}
				}
				
				if(!afterSchList.isEmpty()) {
					for(ReqSchedule r : afterSchList) {
						// 진행 상태 구매확정으로 변경
						r.setReqStatus(3);
						result += mService.updateReqState(r);
						
						// 구매 확정 날짜
						mService.updateReqConfirmDate(r);
						
						// 플래너 정산에 추가
						mService.insertCalcultate(r);
						
						// 회원
						Member m = mService.getMember(r.getReqMemNo());
						sms =	sm.sendMmsByResourcePath(r, m, 3);
					}
				}
				
				
				
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					
				if(result == beforeSchList.size() + afterSchList.size()) {
					return "업데이트 완료(이전 일정 : " + beforeSchList.size() + ", 이후 일정 : " + afterSchList.size() + ")";
				}else {
					return "업데이트 실패";
				}
				
			}else {
				return "업데이트할 신청 리스트가 없음";
			}
	}
}
