package com.finalproject.triprecord.plan.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
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
	public String planCreateView(@ModelAttribute Plan p, /*@RequestParam("dayDiff") Integer dayDiff,*/ Model model) {
		HashMap<Integer, LocalDate> dates = dateFunction(p.getStartDate(), p.getEndDate());

		model.addAttribute("p", p);
		model.addAttribute("dates", dates);
		return "planDetail";
	}

	public HashMap<Integer, LocalDate> dateFunction(String startDate, String endDate) {

		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd");

		LocalDate start = LocalDate.parse(startDate, format);
		LocalDate end = LocalDate.parse(endDate, format);

		HashMap<Integer, LocalDate> dates = new HashMap<>();
		int day = 1;
		for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
			dates.put(day, date);
			day++;
		}

		return dates;
	}
	
	@PostMapping("updateValue.pl")
	public ResponseEntity<Map<String, Object>> updateValue(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
							@RequestParam("spot") String spot) {
		Plan p = new Plan();
		p.setStartDate(startDate);
		p.setEndDate(endDate);
		p.setSpot(spot);
		
		HashMap<Integer, LocalDate> dates = dateFunction(startDate, endDate);
		
		
		if(!dates.isEmpty()) {
			Map<String, Object> map = new HashMap<>();
			map.put("p", p);
			map.put("dates", dates);

			return ResponseEntity.ok(map);
		} else {
			return null;
		}
		
	}
}
