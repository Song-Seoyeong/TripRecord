package com.finalproject.triprecord.plan.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.triprecord.common.Pagination;
import com.finalproject.triprecord.common.model.vo.HashTag;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.member.model.service.MemberService;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.place.model.service.PlaceService;
import com.finalproject.triprecord.place.model.vo.Place;
import com.finalproject.triprecord.plan.model.exception.PlanException;
import com.finalproject.triprecord.plan.model.service.PlanService;
import com.finalproject.triprecord.plan.model.vo.Plan;
import com.finalproject.triprecord.plan.model.vo.Schedule;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class PlanController {
	
	@Autowired
	PlanService plService;
	
	@Autowired
	private PlaceService pService;

	@Autowired
	private MemberService mService;
	
	// 일정 메인
	@GetMapping("planMain.pl")
	public String planMainView(Model model) {
		ArrayList<HashTag> list = plService.hashTagList();
		model.addAttribute("list", list);
		return "planMain";
	}
	
	// 일정 생성
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
	
	// 여행 n일 계산
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
	
	// planDetil 에서 시작일, 종료일, 지역 수정
	@PostMapping("updateValue.pl")
	public ResponseEntity<Map<String, Object>> updateValue(@RequestParam("startDate") String startDate, 
														   @RequestParam("endDate") String endDate,
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
	
	// 일정 저장
	@PostMapping("saveplan.pl")
	public String savePlanInsert(@ModelAttribute Schedule s, @ModelAttribute Plan p, 
								HttpSession session, @RequestParam("count") String count) {
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
	
	// 마이페이지 내 일정 보기 -> 내 여행 노트
	@GetMapping("myTripNote.pl")
	public String myTripNoteList(HttpSession session, HttpServletRequest request, 
								Model model, @RequestParam(value="page", defaultValue="1") int currentPage) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
	    Image image = mService.existFileId(memberNo); 
	    
	    int listCount = plService.getListCount(memberNo);
	    PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
	    
	    ArrayList<Schedule> sList = plService.myTripNoteList(memberNo, pi);
	    
	    if(sList.isEmpty()) {
	    	Schedule s = new Schedule();
	    	s.setScNo(0); // 일정 작성 안 했어도 해시태그 가지러 갔다올 거라
	    	sList.add(s);
	    } else {
	    	Date today = new Date();
	    	
	    	for(int i = 0; i < sList.size(); i++) {
	    		if(today.compareTo(sList.get(i).getScEndDate()) > 0) {
	    			sList.get(i).setStatus("완료");
	    			
	    		} else {
	    			sList.get(i).setStatus("기대하는 중");
	    		}
	    	}
	    }
	    
	    ArrayList<HashTag> hList = plService.myTripTagList(sList);
	    String hashtag = "";
	    
	    if(!hList.isEmpty()) {
	    	for(int s = 0; s < sList.size(); s++) {
	    		int scNo = sList.get(s).getScNo();
	    		for(int i = 0; i < hList.size(); i++) {
    				if(scNo == hList.get(i).getTagRefNo()) {
    					hashtag += hList.get(i).getTagName() + " ";
	    			}
    			}
	    		
	    		sList.get(s).setHashtag(hashtag);
	    		hashtag = "";
	    	}
	    }
	    
	    model.addAttribute("sList", sList);
	    model.addAttribute("pi", pi);
	    model.addAttribute("loc", request.getRequestURI());
	    
	    if (image != null && image.getImageRename() != null) {
	        String existFileId = image.getImageRename(); 
	        model.addAttribute("rename", existFileId);
	    } else {
	        // 이미지가 없거나 리네임이 없는 경우 처리
	        model.addAttribute("rename", "defaultImageName"); 
	    }
		return "myTripNote";
	}
	
//	마이페이지 내 일정 보기 -> 내 여행 노트 -> 상세 조회
	@GetMapping("detailMyTripNote.pl")
	public String detailMyTripNote(@RequestParam("scNo") int scNo, @RequestParam("page") int page, 
									HttpSession session, Model model, RedirectAttributes ra) {
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
	    
		Image image = mService.existFileId(memberNo); 
	    if (image != null && image.getImageRename() != null) {
	    	String existFileId = image.getImageRename(); 
	    	model.addAttribute("rename", existFileId);
	    } else {
	    	// 이미지가 없거나 리네임이 없는 경우 처리
	    	model.addAttribute("rename", "defaultImageName"); 
	    }
	    
	    Schedule s = plService.detailMySchedule(scNo);
	    System.out.println(s);
	    
	    HashMap<Integer, LocalDate> dates = dateFunction(s.getStartDate(), s.getEndDate());
	    
	    String dateStr = dates.values().toString();
	    
	    ArrayList<Plan> pList = plService.detailMyTripNote(scNo);
	    System.out.println("pList" + pList);
	    
	    if(!pList.isEmpty()) {
	    	for(int i = 0; i < pList.size(); i++) {
	    		pList.get(i).setDay(pList.get(i).getDay().split(" ")[0]);
	    	}
	    	
	    	ra.addAttribute("page", page);
	    	model.addAttribute("s", s);
	    	model.addAttribute("dates", dates);
	    	model.addAttribute("pList", pList);
	    	
	    	return "detailMyTripNote";
	    } else {
	    	throw new PlanException("일정 상세 조회에 실패하였습니다.");
	    }
	}
	
	// 마이페이지 -> 내 여행 노트 -> 상세 보기 -> 일정 삭제
	@PostMapping("deleteTripNote.pl")
	public String deleteTripNote(@RequestParam("scNo") int scNo) {
		int result = plService.deleteTripNote(scNo);
		if(result > 0) {
			return "redirect:myTripNote.pl";
		} else {
			throw new PlanException("일정 삭제에 실패하였습니다.");
		}
	}
	
	// 마이페이지 -> 내 여행 노트 -> 상세 보기 -> 수정 ajax 
	@GetMapping("detailTripUpdate.pl")
	public String detailTripUpdate(@ModelAttribute Plan p) {
		int result = plService.detailTripUpdate(p);
		return result == 1? "success" : "fail";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
