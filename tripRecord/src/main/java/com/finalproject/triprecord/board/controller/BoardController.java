package com.finalproject.triprecord.board.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

	@GetMapping("community.bo")
	public String community() { // 단순 이동
		return "commuList";
	}
	
	@GetMapping("commuWrite.bo")
	public String commuWrite() {
		return "communityWrite";
	}
	
	@GetMapping("commuSelect.bo")
	public String commuSelect() {
		return "communitySelect";
	}
	@GetMapping("notice.no")
	public String notice() {
		return "noticeList";
	}
	
	@GetMapping("ask.no")
	public String ask() {
		return "askList";
	}
	
	@GetMapping("askSelect.no")
	public String askSelect() { // 선택한 글번호 넘겨받기
		
		
		return "askSelect";
	}
	
	@GetMapping("askWrite.no")
	public String askWrite() {
		return "askWrite";
	}
	
	@GetMapping("noticeSelect.no")
	public String noticeSelect() { // 선택한 공지 글번호 가져오기
		
		return "noticeSelect";
	}
	
	@GetMapping("noticeWrite.no")
	public String noticeWrite() {
		return "noticeWrite";
	}
}
