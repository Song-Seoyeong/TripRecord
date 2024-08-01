package com.finalproject.triprecord.place.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.triprecord.common.model.service.GoogleDriveService;
import com.finalproject.triprecord.common.model.vo.Image;
import com.finalproject.triprecord.common.model.vo.Local;
import com.finalproject.triprecord.common.model.vo.Review;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.place.model.exception.PlaceException;
import com.finalproject.triprecord.place.model.service.PlaceService;
import com.finalproject.triprecord.place.model.vo.Place;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class PlaceController {
	
	@Autowired
	private PlaceService pService;
	
	@Autowired
	private GoogleDriveService gdService;
	
	@GetMapping("recoPlace.pla")
	public String recoPlacelist(@RequestParam(value="page", defaultValue="1") int page, Model model) {
		ArrayList<Local> list = pService.getLocalList();
		if(list != null) {
			model.addAttribute("list", list);
			model.addAttribute("page", page);
			return "recommendPlace";
		}else {
			return "fail";
		}
	}
	
	@GetMapping("placeDetail.pla")
	public String placeDetail(@RequestParam("contentid") int contentid,
							  @RequestParam("contenttypeid") int contenttypeid,
							  @RequestParam("page") int page,
							  @RequestParam("areaCode") int areaCode,
							  Model model) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		map.put("contentid", contentid);
		map.put("contenttypeid", contenttypeid);
		map.put("areaCode", areaCode);
		
		// 장소 db에 저장되어있는지 확인
		int checkPlace = pService.checkPlace(map);
		
		// db 저장 여부에 따라 다름
		Place p = new Place();
		ArrayList<Review> list = new ArrayList<Review>();
		if(checkPlace > 0){
			p = pService.selectPlace(map);
			list = pService.selectReviewList(contentid);
		}else {
			int result = pService.insertPlace(map);
			if(result > 0) {
				p.setLocalNo(areaCode);
				p.setPlaceNo(contentid);
				p.setPlaceCount(1);
				p.setPlaceStar(0);
			}else {
				throw new PlaceException("장소 조회 중 에러 발생");
			}
		}
		if(p != null) {
			model.addAttribute("p", p);
			model.addAttribute("list", list);
			return "placeDetail";
		}else {
			throw new PlaceException("장소 조회 중 에러 발생");
		}
	}
	
	@GetMapping("placeReviewWrite.pla")
	public String placeReviewWrite(@RequestParam("contentid") int contentid,
								   @RequestParam("contenttypeid") int contenttypeid,
								   @RequestParam("page") int page,
								  @RequestParam("areacode") int areaCode,
								   Model model) {
		//System.out.println("여기인가");
		model.addAttribute("contentid", contentid);
		model.addAttribute("contenttypeid", contenttypeid);
		model.addAttribute("areacode", areaCode);
		model.addAttribute("page", page);
		return "placeReviewWrite";
	}
	
	@PostMapping("insertPlaReview.pla")
	public String insertPlaReview(@ModelAttribute Review r,
								  @RequestParam(value="files", required=false) ArrayList<MultipartFile> files,
								  @RequestParam("contenttypeid") int contenttypeid,
								  @RequestParam("page") int page,
								  @RequestParam("areacode") int areacode,
								  HttpServletRequest request, RedirectAttributes ra) {
		//System.out.println(files);
		
		int memNo = ((Member)request.getSession().getAttribute("loginUser")).getMemberNo();
		r.setMemberNo(memNo);
		r.setRevRefType("PLACE");
		
		int result = pService.insertReview(r);
		
		
		//.......여기부터.......//
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
					a.setImageRefType("RECOPLACE");
					a.setImageRefNo(r.getReviewNo());
					
					list.add(a);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//.......여기까지 구글 드라이브에 이미지 저장.......//
		
		int iResult = 0;
		if(!list.isEmpty()) {
			iResult = pService.insertImage(list);
		}
		if(result + iResult == 1 + list.size()) {
			ra.addAttribute("contentid", r.getRevRefNo());
			ra.addAttribute("contenttypeid", contenttypeid);
			ra.addAttribute("areacode", areacode);
			ra.addAttribute("page", page);
			return "redirect:placeDetail.pla";
		} else {
			for(Image a : list) {
				try {
					//구글드라이브 이미지 삭제
					gdService.deleteFile(a.getImageRename());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			throw new PlaceException("리뷰 작성을 실패했습니다.");
		}
	}
	
	
	@GetMapping("placeReviewDetail.pla")
	public String placeReviewDetail(@RequestParam("contentid") int contentid,
			   						@RequestParam("contenttypeid") int contenttypeid,
			   						@RequestParam("page") int page,
			   						@RequestParam("areacode") int areaCode,
			   						@RequestParam("reviewNo") int rId,
			   						Model model) {
		Review r = pService.selectReview(rId);
		ArrayList<Image> list = pService.selectImage(rId);
		
		//System.out.println(list);
		if(r != null && list != null) {
			model.addAttribute("contentid", contentid);
			model.addAttribute("contenttypeid", contenttypeid);
			model.addAttribute("areacode", areaCode);
			model.addAttribute("page", page);
			model.addAttribute("r", r);
			model.addAttribute("list", list);
			return "placeReviewDetail";
		}else {
			throw new PlaceException("리뷰 상세 보기 중 에러 발생");
		}
	}
	
	@PostMapping("deleteReview.pla")
	public String deleteReview(@RequestParam("reviewNo") int rId,
							   @RequestParam("contentid") int contentid,
							   @RequestParam("contenttypeid") int contenttypeid,
							   @RequestParam("areacode") int areacode,
							   @RequestParam("page") int page,
							   RedirectAttributes ra) {
		int result = pService.deleteReview(rId);
		
		if(result > 0) {
			ra.addAttribute("contentid", contentid);
			ra.addAttribute("contenttypeid", contenttypeid);
			ra.addAttribute("areaCode", areacode);
			ra.addAttribute("page", page);
			return "redirect:placeDetail.pla";
		}else {
			throw new PlaceException("리뷰 삭제 중 에러 발생");
		}
	}
	
	@PostMapping("updateReviewView.pla")
	public String updateReviewView(@RequestParam("reviewNo") int rId,
			   					   @RequestParam("contentid") int contentid,
			   					   @RequestParam("contenttypeid") int contenttypeid,
			   					   @RequestParam("areacode") int areacode,
			   					   @RequestParam("page") int page,
			   					   Model model) {
		Review r = pService.selectReview(rId);
		ArrayList<Image> list = pService.selectImage(rId);
		
		if(r != null && list != null) {
			model.addAttribute("contentid", contentid);
			model.addAttribute("contenttypeid", contenttypeid);
			model.addAttribute("areacode", areacode);
			model.addAttribute("page", page);
			model.addAttribute("r", r);
			model.addAttribute("list", list);
			return "placeReviewUpdate";
		}else {
			throw new PlaceException("리뷰 상세 보기 중 에러 발생");
		}
	}
	
	@PostMapping("updatePlaReview.pla")
	public String updatePlaReview(@ModelAttribute Review r,
								  @RequestParam(value="delImg", required=false) ArrayList<String> delImgs,
								  @RequestParam(value="files", required=false) ArrayList<MultipartFile> files,
							  	  @RequestParam("contenttypeid") int contenttypeid,
							  	  @RequestParam("page") int page,
							  	  @RequestParam("areacode") int areacode,
							  	  HttpServletRequest request, RedirectAttributes ra) {
		// 리뷰수정
		int result = pService.updateReview(r);
		
		// 이미지 삭제 확인
		ArrayList<String> deleteImg = new ArrayList<String>();
		int delResult;
		try {
			if(delImgs != null && !delImgs.isEmpty()) {
				for(String del : delImgs) {
					if(!del.equals("none")) {
						deleteImg.add(del);
						// 구글 드라이브에서 삭제
						gdService.deleteFile(del);
					}
				}
			}
			delResult = pService.delImg(deleteImg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
