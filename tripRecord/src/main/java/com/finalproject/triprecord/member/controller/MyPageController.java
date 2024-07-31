package com.finalproject.triprecord.member.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.finalproject.triprecord.member.model.service.MemberService;

@Controller
public class MyPageController {
	
	@Autowired
	MemberService mService;
	
//////////마이페이지 ////////// 
	@GetMapping("myPage.mp")
	public String moveToMyPage() {
		return "myPage";
	}

	@GetMapping("updateMyPwd.mp")
	public String moveToUpdateMyPwd() {
		return "updateMyPwd";
	}

	@GetMapping("promoteToPlanner.mp")
	public String moveToPromoteToPlanner() {
		return "promoteToPlanner";
	}

	@GetMapping("myPoint.mp")
	public String moveToMyPoint() {
		return "myPoint";
	}

	@GetMapping("myPayPoint.mp")
	public String moveToMyPayPoint() {
		return "myPayPoint";
	}

	@GetMapping("myPlan.mp")
	public String moveToMyPlan() {
		return "myPlan";
	}

	@GetMapping("detailReqPlan.mp")
	public String moveToDetailReqPlan() {
		return "detailReqPlan";
	}

	@GetMapping("feedback.mp")
	public String moveToFeedback() {
		return "feedback";
	}

	@GetMapping("myTripNote.mp")
	public String moveToMyTripNote() {
		return "myTripNote";
	}

	@GetMapping("detailMyTripNote.mp")
	public String moveToDetailMyTripNote() {
		return "detailMyTripNote";
	}

	@GetMapping("myInquiry.mp")
	public String moveToMyInquiry() {
		return "myInquiry";
	}

	@GetMapping("updateInquiry.mp")
	public String moveToUpdateInquiry() {
		return "updateInquiry";
	}

	@GetMapping("detailMyInquiry.mp")
	public String moveToDetailMyInquiry() {
		return "detailMyInquiry";
	}

	@GetMapping("myBoard.mp")
	public String moveToMyBoard() {
		return "myBoard";
	}

	@GetMapping("detailMyBoard.mp")
	public String moveToDetailMyBoard() {
		return "detailMyBoard";
	}

	@GetMapping("updateMyBoard.mp")
	public String moveToUpdateMyBoard() {
		return "updateMyBoard";
	}

////////// 플래너 페이지 ////////// 
	@GetMapping("plannerPage.mp")
	public String moveToPlannerPage() {
		return "plannerPage";
	}

	@GetMapping("request.mp")
	public String moveToRequest() {
		return "request";
	}

	@GetMapping("detailRequest.mp")
	public String moveToDetailRequest() {
		return "detailRequest";
	}

	@GetMapping("sales.mp")
	public String moveToSales() {
		return "sales";
	}

	@GetMapping("updatePlanner.mp")
	public String moveToUpdatePalnner() {
		return "updatePlanner";
	}

	@GetMapping("processRequest.mp")
	public String moveToProcessRequest() {
		return "processRequest";
	}

	@GetMapping("requestEnd.mp")
	public String moveToRequestEnd() {
		return "requestEnd";
	}
}
