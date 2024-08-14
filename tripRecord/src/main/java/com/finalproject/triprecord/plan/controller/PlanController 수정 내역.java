- 197번 줄 수정

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
	    		if(!hashtag.equals("")) {
	    			sList.get(s).setHashtag(hashtag);
	    		}
	    		hashtag = "";
	    	}
	    }
	    
	    model.addAttribute("page", currentPage);
	    model.addAttribute("sList", sList);
	    model.addAttribute("pi", pi);
	    model.addAttribute("listCount", listCount);
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
	

- 260번 줄 수정

//	마이페이지 내 일정 보기 -> 내 여행 노트 -> 상세 조회
	@GetMapping("detailMyTripNote.pl")
	public String detailMyTripNote(@RequestParam("scNo") int scNo, @RequestParam(value="page", defaultValue="1") int page, 
									HttpSession session, Model model, RedirectAttributes ra, HttpServletRequest req) {
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
	    
	    HashMap<Integer, String> dates = dateFunction(s.getStartDate(), s.getEndDate());
	    
	    ArrayList<Plan> pList = plService.detailMyTripNote(scNo);
	    if(!pList.isEmpty()) {
	    	for(int i = 0; i < pList.size(); i++) {
	    		pList.get(i).setDay(pList.get(i).getDay().split(" ")[0]);
	    	}
	    	
	    	model.addAttribute("page", page);
	    	model.addAttribute("s", s);
	    	model.addAttribute("dates", dates);
	    	model.addAttribute("pList", pList);
	    	model.addAttribute("loc", req.getRequestURI());
	    	
	    	return "detailMyTripNote";
	    } else {
	    	throw new PlanException("일정 상세 조회에 실패하였습니다.");
	    }
	}

- 309번 줄 수정
	
	// 마이페이지 -> 내 여행 노트 -> 상세 보기 -> 장소, 시간, 메모 수정 ajax 
	@GetMapping("detailTripUpdate.pl")
	@ResponseBody
	public String detailTripUpdate(@RequestParam("place") String place, @RequestParam("time") String time, @RequestParam("memo") String memo, @RequestParam("plNo") String plNo) {
		Properties prop = new Properties();
		prop.setProperty("place", place);
		prop.setProperty("time", time);
		prop.setProperty("memo", memo);
		prop.setProperty("plNo", plNo);
		int result = plService.detailTripUpdate(prop);
		return result > 0? "1" : "0";
	}

- 322번 줄 추가
	// 마이페이지 -> 내 여행 노트 -> 상세 보기 -> 예약 여부 수정 ajax 
	@GetMapping("updateReserve.pl")
	@ResponseBody
	public String updateReserve(@RequestParam("plNo") String plNo, @RequestParam("status") String status) {
		Properties prop = new Properties();
		prop.setProperty("plNo", plNo);
		prop.setProperty("status", status);
		int result = plService.updateReserve(prop);
		return result > 0 ? "1" : "0";
	}
	
	
	
	
	
	
	
	
	
	
	
}
