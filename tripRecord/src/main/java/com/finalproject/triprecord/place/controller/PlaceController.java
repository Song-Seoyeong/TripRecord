package com.finalproject.triprecord.place.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

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
	public String recoPlacelist(@RequestParam(value="page", defaultValue="1") int page,
								@RequestParam(value="localNo", defaultValue="") String areacode, Model model) {
		ArrayList<Local> list = pService.getLocalList();
		
		ArrayList<Local> topList = pService.getTopList();
		//System.out.println(topList);
		if(list != null) {
			model.addAttribute("topList", topList);
			model.addAttribute("list", list);
			model.addAttribute("page", page);
			model.addAttribute("areaCode", areacode);
			return "recommendPlace";
		}else {
			throw new PlaceException("추천장소 이동 중 에러 발생");
		}
	}
	
	@GetMapping("placeDetail.pla")
	public String placeDetail(@RequestParam("contentid") int contentid,
							  @RequestParam("contenttypeid") int contenttypeid,
							  @RequestParam(value="page", defaultValue="1") int page,
							  @RequestParam("areacode") int areacode,
							  @RequestParam(value="image", required=false) String image,
							  @RequestParam(value="title", required=false) String title,
							  Model model) {
		String name = null;
		if(title != null ) {
			try {
				name = URLDecoder.decode(title,"UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			name = name.split("\\(|\\[|\\{")[0];
			
			
		}
		
		Place ckPla = new Place();
		ckPla.setPlaceNo(contentid);
		ckPla.setLocalNo(areacode);
		ckPla.setContentTypeId(contenttypeid);
		ckPla.setPlaceName(name);
		
		// 이름 정보 저장되어있는지 확인
		if(name != null) {
			int checkName = pService.checkName(ckPla);
			
			if(checkName == 0) {
				pService.updatePlaName(ckPla);
			}
		}
				
		
		// 장소 db에 저장되어있는지 확인
		int checkPlace = pService.checkPlace(ckPla);
		
		// db 저장 여부에 따라 다름
		Place p = new Place();
		ArrayList<Review> list = new ArrayList<Review>();
		if(checkPlace > 0){
			p = pService.selectPlace(ckPla);
			list = pService.selectReviewList(contentid);
		}else {
			int result = pService.insertPlace(ckPla);
			if(result > 0) {
				p.setLocalNo(areacode);
				p.setPlaceNo(contentid);
				p.setPlaceCount(1);
				p.setPlaceStar(0);
				p.setPlaceName(name);
			}else {
				throw new PlaceException("장소 조회 중 에러 발생");
			}
		}
		
		// db에 이미지 정보 저장되어있는지 확인
		int checkImage = pService.checkImage(contentid);
		
		//System.out.println(image.split("\\(|\\)")[1]);
		
		// api에서 이미지를 제공 해주면 이미지 저장
		if(checkImage == 0 && !image.equals("()")) {
			Image i = new Image();
			i.setImageRefNo(contentid);
			i.setImagePath(image.split("\\(|\\)")[1]);
			i.setImageRefType("RECOPLACE");
			i.setImageOriginName(name);
			
			pService.insertPlaImage(i);
		}
		
		//System.out.println(list);
		if(p != null) {
			model.addAttribute("p", p);
			model.addAttribute("list", list);
			model.addAttribute("page", page);
			return "placeDetail";
		}else {
			throw new PlaceException("장소 조회 중 에러 발생");
		}
	}
	
	@GetMapping("placeReviewWrite.pla")
	public String placeReviewWrite(@RequestParam("contentid") int contentid,
								   @RequestParam("contenttypeid") int contenttypeid,
								   @RequestParam(value="page", defaultValue="1") int page,
								  @RequestParam("areacode") int areacode,
								   Model model) {
		//System.out.println("여기인가");
		
		Place p =new Place();
		p.setLocalNo(areacode);
		p.setPlaceNo(contentid);
		
		p = pService.selectPlaceImg(p);
		//System.out.println(p);
		
		model.addAttribute("contentid", contentid);
		model.addAttribute("contenttypeid", contenttypeid);
		model.addAttribute("areacode", areacode);
		model.addAttribute("page", page);
		model.addAttribute("p", p);
		return "placeReviewWrite";
	}
	
	@PostMapping("insertPlaReview.pla")
	public String insertPlaReview(@ModelAttribute Review r,
								  @RequestParam(value="files", required=false) ArrayList<MultipartFile> files,
								  @RequestParam("contenttypeid") int contenttypeid,
								  @RequestParam(value="page", defaultValue="1") int page,
								  @RequestParam("areacode") int areacode,
								  HttpServletRequest request, RedirectAttributes ra) {
		//System.out.println(files);
		
		Member loginUser = (Member)request.getSession().getAttribute("loginUser");
		r.setMemberNo(loginUser.getMemberNo());
		r.setRevRefType("PLACE");
		
		// 첫 리뷰인지 확인
		int checkRe = pService.checkReview(loginUser.getMemberNo());
		
		//System.out.println(checkRe);
		if(checkRe == 0) {
			pService.givePoint(loginUser.getMemberNo());
			loginUser.setMemberPoint(loginUser.getMemberPoint() + 1000);
			ra.addAttribute("ch", "0");
		}
		
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
					a.setImageRefType("REVIEW");
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
			   						@RequestParam(value="page", defaultValue="1") int page,
			   						@RequestParam("areacode") int areaCode,
			   						@RequestParam("reviewNo") int rId,
			   						Model model) {
		Place p =new Place();
		p.setLocalNo(areaCode);
		p.setPlaceNo(contentid);
		
		p = pService.selectPlaceImg(p);
		
		//System.out.println(p);
		
		Review r = pService.selectReview(rId);
		ArrayList<Image> list = pService.selectImage(rId);
		
		//System.out.println(rId);
		//System.out.println(list);
		//System.out.println(r);
		if(r != null && list != null) {
			model.addAttribute("page", page);
			model.addAttribute("r", r);
			model.addAttribute("list", list);
			model.addAttribute("p", p);
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
							   @RequestParam(value="page", defaultValue="1") int page,
							   RedirectAttributes ra) {
		int result = pService.deleteReview(rId);
		
		if(result > 0) {
			ra.addAttribute("contentid", contentid);
			ra.addAttribute("contenttypeid", contenttypeid);
			ra.addAttribute("areacode", areacode);
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
		Place p =new Place();
		p.setLocalNo(areacode);
		p.setPlaceNo(contentid);
		
		p = pService.selectPlaceImg(p);
		
		Review r = pService.selectReview(rId);
		
		ArrayList<Image> list = pService.selectImage(rId);
		
		if(r != null && list != null) {
			model.addAttribute("contentid", contentid);
			model.addAttribute("contenttypeid", contenttypeid);
			model.addAttribute("areacode", areacode);
			model.addAttribute("page", page);
			model.addAttribute("r", r);
			model.addAttribute("list", list);
			model.addAttribute("p", p);
			return "placeReviewUpdate";
		}else {
			throw new PlaceException("리뷰 수정 페이지 이동 중 에러 발생");
		}
	}
	
	@PostMapping("updatePlaReview.pla")
	public String updatePlaReview(@ModelAttribute Review r,
								  @RequestParam(value="delImg", required=false) ArrayList<String> delImgs,
								  @RequestParam(value="files", required=false) ArrayList<MultipartFile> files,
							  	  @RequestParam("contenttypeid") int contenttypeid,
							  	  @RequestParam(value="page", defaultValue="1") int page,
							  	  @RequestParam("areacode") int areacode,
							  	  HttpServletRequest request, RedirectAttributes ra) {
		// 리뷰수정
		int result = pService.updateReview(r);
		
		
		ArrayList<String> deleteImg = new ArrayList<String>();
		int delResult = 0;
		int insertResult;
		
		try {
			// 이미지 삭제
			if(delImgs != null && !delImgs.isEmpty()) {
				for(String del : delImgs) {
					if(!del.equals("none")) {
						deleteImg.add(del);
						// 구글 드라이브에서 삭제
						gdService.deleteFile(del);
					}
				}
				if(!deleteImg.isEmpty())
					delResult = pService.delImg(deleteImg);
			}
			
			// 이미지 추가
			ArrayList<Image> list = new ArrayList<Image>();
			for(int i = 0; i < files.size(); i++) {
				MultipartFile upload = files.get(i);
				
				//if(!upload.getOriginalFilename().equals("")) {
				if(upload != null && !upload.isEmpty()) {
		            String fileId;
		            
					fileId = gdService.uploadFile(upload.getInputStream(), upload.getOriginalFilename());
					Image a = new Image();
					a.setImageOriginName(upload.getOriginalFilename());
					a.setImageRename(fileId);
					a.setImagePath("drive://files/" + fileId);
					a.setImageThum(2);
					a.setImageRefType("REVIEW");
					a.setImageRefNo(r.getReviewNo());
					
					list.add(a);
				}
			}
			
			if(!list.isEmpty()) {
				insertResult = pService.insertImage(list);
				
				if(insertResult < 0) {
					for(Image i : list) {
						gdService.deleteFile(i.getImageRename());
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(result + delResult == 1 + deleteImg.size()) {
			ra.addAttribute("contentid", r.getRevRefNo());
			ra.addAttribute("contenttypeid", contenttypeid);
			ra.addAttribute("areacode", areacode);
			ra.addAttribute("page", page);
			ra.addAttribute("reviewNo", r.getReviewNo());
			return "redirect:placeReviewDetail.pla";
		}else {
			throw new PlaceException("리뷰 수정 중 에러발생");
		}
	}
	
}
