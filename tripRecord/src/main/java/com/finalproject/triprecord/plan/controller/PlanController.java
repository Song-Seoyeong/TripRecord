package com.finalproject.triprecord.plan.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.triprecord.plan.model.vo.Plan;

@Controller
public class PlanController {
	
	@GetMapping("planMain.pl")
	public String planMainView() {
		return "planMain";
	}
	
	@GetMapping("planDetail.pl")
	public String planDetailView() { // 연결될 시 삭제
		return "planDetail";
	}
	
	@PostMapping("planCreate.pl")
	public String planCreateView(@ModelAttribute Plan p, @RequestParam("dayDiff") Integer dayDiff, Model model) {
		model.addAttribute("p", p);
		model.addAttribute("dayDiff", dayDiff);
		return "planDetail";
	}
}
