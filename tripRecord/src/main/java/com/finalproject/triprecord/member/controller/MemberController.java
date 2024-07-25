package com.finalproject.triprecord.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
	
	@GetMapping("searchIdView.me")
	public String searchId() {
		return "searchId";
	}
	
	@GetMapping("searchPwdView.me")
	public String searchPwd() {
		return "searchPwd";
	}
	@PostMapping("login.me")
	public String login() {
		
		
		return "redirect:/";
	}
	@GetMapping("updatePwdView.me")
	public String updatePwd() {
		return "updatePwd";
	}
	
	@GetMapping("enrollPlannerView.me")
	public String enrollPlanner() {
		return "enrollPlanner";
	}
}
