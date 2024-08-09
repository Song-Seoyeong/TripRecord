package com.finalproject.triprecord.payment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.payment.model.service.PaymentService;
import com.finalproject.triprecord.payment.model.service.RefundService;

import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentApiController {
	
	
	@Autowired
	private PaymentService pService;
	
	@Autowired
	private RefundService rService;
	
//	@Value("${imp.api.key}")
//	private String apikey;
//	
//	@Value("${imp.api.secretkey}")
//	private String secretkey;
//
//	
//	@PostConstruct
//	public void init() {
//		this.iamportClient = new IamportClient(apikey, secretkey);
//	}
	
	//결제시 결제내역이 저장되는 메소드
	//ResponseEntitiy 사용 시 결과값, 상태코드, 헤더값을 프론트에 넘겨주기 가능
	@PostMapping("payment.pm")
	public ResponseEntity<String> paymentComplete(HttpSession session,
												  @RequestParam("impUid") String impUid,
												  @RequestParam("merchantUid") String merchantUid,
												  @RequestParam("poNo") int pointNo,
												  @RequestParam("point") int point){
		
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		
		 try {
	           

			 		Payment payments = new Payment();
			 		payments.setMemberNo(memberNo);
			 		payments.setPointNo(pointNo);
			 		payments.setImpUid(impUid);
			 		payments.setMerchantUid(merchantUid);
			 		pService.saveOrder(payments);
			 		
			 		Member m = new Member();
			 		m.setMemberNo(memberNo);
			 		m.setMemberPoint(point);
			 		pService.updateMemberPoint(m);
			 		
	            return ResponseEntity.ok("결제가 완료되었습니다.");

	        } catch (Exception e) {
	            return ResponseEntity.status(500).body("결제 처리 중 오류가 발생했습니다.");
	        }
	    }
	
//	@PostMapping("refund.pm")
//	public ResponseEntity<String> refundComplete(){
//		String token = rService.getToken(apikey, secretkey);
//		
//		//rService.refundRequest(token, merchantUid, reason);
//		return ResponseEntity.ok("환불이 완료되었습니다.");
//	}
	}

