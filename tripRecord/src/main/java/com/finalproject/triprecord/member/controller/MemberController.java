package com.finalproject.triprecord.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {
	
	@GetMapping("loginView.me")
	public String loginView() {
		return "login";
	}
	
	@GetMapping("enrollView.me")
	public String enrollView() {
		return "enrollMember";
	}
	
	@GetMapping("enrollPlannerView.me")
	public String enrollPlannerView() {
		return "enrollPlanner";
	}
	
	@GetMapping("searchIdView.me")
	public String searchIdView() {
		return "searchId";
	}
	
	@GetMapping("searchPwdView.me")
	public String searchPwdView() {
		return "searchPwd";
	}
	
	@GetMapping("updatePwdView.me")
	public String updatePwdView() {
		return "updatePwd";
	}
}
