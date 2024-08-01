package com.finalproject.triprecord.board.controller;


import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.finalproject.triprecord.board.model.service.BoardService;
import com.finalproject.triprecord.board.model.vo.Board;
import com.finalproject.triprecord.board.model.vo.CategorySelect;
import com.finalproject.triprecord.common.Pagination;
import com.finalproject.triprecord.common.model.service.GoogleDriveService;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.PageInfo;
import com.finalproject.triprecord.member.model.vo.Member;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class BoardController {


	@Autowired
	private BoardService bService;
	
	@Autowired
	private GoogleDriveService gdService;
	
	@GetMapping("community.bo") // 커뮤니티 게시글 가져오기
	public String community(@RequestParam(value="page", defaultValue="1") int currentPage, Model model, HttpServletRequest req) {
		
		CategorySelect cs = new CategorySelect();
		cs.setBoardType("GENERAL");
		
		
		int listCount = bService.getListCount(cs);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10); // 10개씩 보여주겠다
		ArrayList<Board> cList = bService.getBoardList(cs, pi); // 메소드 안에 해당 타입 넣으면 그거 가져옴
		// GENERAL, QUESTION, NOTICE -> 일반, 문의, 공지
		// GENERAL -> 동행 WITH, 양도 GIVE, 후기 REVIEW
		
		
		
		if(!cList.isEmpty()) {
			model.addAttribute("cList", cList);
			model.addAttribute("pi",pi);
			model.addAttribute("loc", req.getRequestURI());
			model.addAttribute("generalType","ALL");
			model.addAttribute("boardType","GENERAL");
		} else {
			model.addAttribute("nothing", null);
		}
		return "commuList";
	}
	
	@GetMapping("categorySelect.bo")
	public String categorySelect(@ModelAttribute CategorySelect cs, @RequestParam(value="page", defaultValue="1") int currentPage,
								 Model model, HttpServletRequest req) {
		
		System.out.println(cs);
		if(cs.getGeneralType().equals("동행") || cs.getGeneralType().equals("WITH") ) {
			cs.setGeneralType("WITH");
		} else if(cs.getGeneralType().equals("양도") || cs.getGeneralType().equals("GIVE")) {
			cs.setGeneralType("GIVE");
		} else if(cs.getGeneralType().equals("후기") || cs.getGeneralType().equals("REVIEW")) {
			cs.setGeneralType("REVIEW");
		} else if(cs.getGeneralType() == "") {
			cs.setGeneralType("ALL");
		} else {
			cs.setGeneralType("ALL");
		}
		
		if(cs.getLocalName() == "") {
			cs.setLocalName("ALL");
		} else if(cs.getLocalName().equals("전국")) {
			cs.setLocalName("ALL");
		}
		// 위 2개의 카테고리를 반복 설정? 클릭 시 어느 순간 에러가 남
		
		//System.out.println(cs);
		
		int listCount = bService.getCategorySelectListCount(cs);
		
		PageInfo pi = Pagination.getPageInfo(currentPage, listCount, 10);
		ArrayList<Board> cList = bService.getCategorySelectBoardList(cs, pi);
		
		System.out.println(cList);
		
		if(!cList.isEmpty()) {
			model.addAttribute("cList", cList);
			model.addAttribute("pi",pi);
			model.addAttribute("loc", req.getRequestURI());
			model.addAttribute("generalType", cs.getGeneralType());
			model.addAttribute("localName", cs.getLocalName());
			//model.addAttribute("boardType", "GENERAL");    // 현재 커뮤니티 게시판 == 무조건 GENERAL
		} else {
			model.addAttribute("nothing", "nothing");
		}
		
		
	

		return "commuList";
	}
	
	@GetMapping("commuWrite.bo")
	public String commuWrite() {
		return "communityWrite";
	}
	
	@GetMapping("commuSelect.bo")
	public String commuSelect(@RequestParam("boardNo") Integer boardNo, @RequestParam(value="page", defaultValue="1") int page, Model model, HttpSession session) {
		Member loginUser = (Member)session.getAttribute("loginUser");
		int id = 0;
		if(loginUser != null) {
			id = loginUser.getMemberNo();
		}
		
		Board board = bService.selectBoard(boardNo, id);
		ArrayList<Image> iList = bService.selectImage(boardNo); // 무조건 BOARD 니까 boardNo 만 보내면 가능
		ArrayList<com.finalproject.triprecord.board.model.vo.Reply> rList = bService.selectReply(boardNo);
		/* 댓글 작성자들 썸네일 사진도 가져와야 함 */
		if(board != null) {
			model.addAttribute("b", board);
			model.addAttribute("page", page);
			model.addAttribute("rlist", rList);
			model.addAttribute("iList", iList);
			return "communitySelect";
		} else {
			return "NOT";
		}
	}
	
	@GetMapping("commuEdit.bo")
	public String commuEdit(@RequestParam("boardNo") Integer boardNo, Model model) {
		Board b = bService.selectBoard(boardNo, 0);
		if(b != null) {
			model.addAttribute("b",b);
			
		}
		return null;
	}
	
	/*
	 * BOARD.BOARD_NO = GENERAL_BO.GENERAL_NO
	 */
	
	public int returnBoardNo() {
		return bService.selectBoardNo(); // 단순히 seq_board.currval 을 가져오려고 만듬
	}
	
	
	@PostMapping("insertBoard.bo")
	public String insertBoard(@ModelAttribute Board b, HttpSession session, @RequestParam(value="files", required=false) ArrayList<MultipartFile> files) {
		// 부트 board 에는 (일반,공지,문의) + (양도,동행,후기) + (지역이름) 있음
		Member loginUser = (Member)session.getAttribute("loginUser");
		b.setBoardWriterNo(loginUser.getMemberNo());
		int result = bService.insertBoard(b); // 성공 시 1
		int result2 = bService.insertGeneralBoard(b); // 성공 시 1
		int bNo = returnBoardNo(); // seq_board.currval
		
		ArrayList<Image> list = new ArrayList<Image>();
		for(int i = 0; i < files.size(); i++) {
			MultipartFile upload = files.get(i);
			
			//if(!upload.getOriginalFilename().equals("")) {
			if(upload != null && !upload.isEmpty()) {
	            String fileId;
	            
				try {
					fileId = gdService.uploadFile(upload.getInputStream(), upload.getOriginalFilename());
					Image a = new Image();
					a.setImageOriginName(upload.getOriginalFilename());
					a.setImageRename(fileId);
					a.setImagePath("drive://files/" + fileId);
					a.setImageThum(2);
					a.setImageRefType("BOARD");
					a.setImageRefNo(bNo);
					
					list.add(a);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		int iResult = 0;
		if(!list.isEmpty()) {
			iResult = bService.insertImage(list);
		}
		if(result + result2 + iResult == 2 + list.size()) {
			return "redirect:community.bo";
		}else {
			bService.deleteBoard(bNo); // 이미지 넣기 실패해서 글도 삭제 ?
			for(Image a : list) {
				try {
					gdService.deleteFile(a.getImageRename());
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			return "NOT";
		}
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
	
	@GetMapping("notice.no")
	public String notice() {
		return "noticeList";
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
