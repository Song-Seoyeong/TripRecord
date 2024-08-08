package com.finalproject.triprecord.plan.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.place.model.service.PlaceService;
import com.finalproject.triprecord.place.model.vo.Place;
import com.finalproject.triprecord.plan.model.exception.PlanException;
import com.finalproject.triprecord.plan.model.service.PlanService;
import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

import jakarta.servlet.http.HttpSession;

@Controller
public class PlanController {
	
	@Autowired
	private PlaceService pService;
	
	@Autowired
	private PlanService plService;

	@GetMapping("planMain.pl")
	public String planMainView(Model model) {
		ArrayList<HashTag> list = plService.hashTagList();
		model.addAttribute("list", list);
		return "planMain";
	}

	@PostMapping("planCreate.pl")
	public String planCreateView(@ModelAttribute Schedule s,
								 @RequestParam("spot") String spot,
								 @RequestParam(value = "togetherTagNo", required = false) String togetherNo,
								 @RequestParam(value = "hashtagTagNos", required = false) String hashtagNo,
								 @RequestParam("localNo") int localNo,
								 Model model) {
		HashMap<Integer, LocalDate> dates = dateFunction(s.getStartDate(), s.getEndDate());
		
		ArrayList<Place> pList = pService.selectPlaceList(localNo);
		
		model.addAttribute("pList", pList);
		model.addAttribute("s", s);
		model.addAttribute("spot", spot);
		model.addAttribute("togetherNo", togetherNo);
		model.addAttribute("hashtagNo", hashtagNo);
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
		Schedule s = new Schedule();
		s.setStartDate(startDate);
		s.setEndDate(endDate);
		s.setSpot(spot);
		
		HashMap<Integer, LocalDate> dates = dateFunction(startDate, endDate);
		
		
		if(!dates.isEmpty()) {
			Map<String, Object> map = new HashMap<>();
			map.put("s", s);
			map.put("dates", dates);

			return ResponseEntity.ok(map);
		} else {
			return null;
		}
		
	}
	
	@PostMapping("saveplan.pl")
	public String savePlanInsert(@ModelAttribute Schedule s, @ModelAttribute Plan p, HttpSession session, @RequestParam("count") String count) {
		Member loginUser = ((Member)session.getAttribute("loginUser"));
		if(loginUser != null) {
			s.setWriterNo(loginUser.getMemberNo());
		}
		
		String place[] = p.getPlace().split(",");
		String time[] = p.getTime().split(",");
		String memo[] = p.getPlace().split(",");
		String reserve[] = p.getReserve().split("/");
		String day[] = p.getDay().split(",");
		
		String cStr[] = count.split(";");
		Integer coNum[] = new Integer[cStr.length];
		for(int i = 0; i < cStr.length; i++) {
			coNum[i] = Integer.parseInt(cStr[i]);
		}
		
		s.setScLocalNo(Integer.parseInt(s.getSpot()));
		
		ArrayList<Plan> plList = new ArrayList<Plan>();
		for(int i = 0, coCount = 0, dNum = 0; i < place.length; i++, coCount++) {
			
			Plan pl = new Plan();
			pl.setPlNo(i);
			pl.setPlace(place[i]);
			pl.setTime(time[i]);
			pl.setMemo(memo[i]);
			pl.setReserve(reserve[i]);
			
			if(coCount == coNum[dNum]) {
				coCount = 0;
				dNum++;
			}
			pl.setDay(day[dNum]);
			
			plList.add(pl);
		}
		
		ArrayList<HashTag> tagList = new ArrayList<HashTag>();
		HashTag h = null;
		if(s.getHashtag() != null) {
			String hashtag[] = s.getHashtag().split(",");
			for(int i = 0; i < hashtag.length; i++) {
				h = new HashTag();
				h.setTagNo(Integer.parseInt(hashtag[i]));
				h.setTagRefType("SCHEDULE");
				tagList.add(h);
			}
		} else if(s.getTogether() != null) {
			h = new HashTag();
			h.setTagNo(Integer.parseInt(s.getTogether()));
			h.setTagRefType("SCHEDULE");
			tagList.add(h);
		}
		
		int result = plService.savePlanInsert(s, plList, tagList);
		
		if(result > 0) {
			return "redirect:/";
		} else {
			throw new PlanException("일정을 저장하던 중 오류가 발생했습니다.");
		}
	}
	
	
}
