package com.finalproject.triprecord.payment.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.finalproject.triprecord.common.model.vo.Cancel;
import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.payment.model.service.PaymentService;
import com.finalproject.triprecord.payment.model.service.RefundService;
import com.siot.IamportRestClient.IamportClient;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;

@Controller
public class PaymentApiController {

	private IamportClient iamportClient;

	@Autowired
	private PaymentService pService;

	@Autowired
	private RefundService rService;

	@Value("${imp.api.key}")
	private String apikey;

	@Value("${imp.api.secretkey}")
	private String secretkey;

	@PostConstruct
	public void init() {
		this.iamportClient = new IamportClient(apikey, secretkey);
	}

	// 결제시 결제내역이 저장되는 메소드
	@PostMapping("payment.pm")
	@ResponseBody
	public String paymentComplete(HttpSession session, @RequestParam("impUid") String impUid,
			@RequestParam("merchantUid") String merchantUid, @RequestParam("poNo") int pointNo,
			@RequestParam("point") int point) {

			Member loginUser = (Member) session.getAttribute("loginUser");

			Payment payments = new Payment();
			payments.setMemberNo(loginUser.getMemberNo());
			payments.setPointNo(pointNo);
			payments.setImpUid(impUid);
			payments.setMerchantUid(merchantUid);
			int sResult = pService.saveOrder(payments);

			Member m = new Member();
			m.setMemberNo(loginUser.getMemberNo());
			m.setMemberPoint(point);
			int uResult = pService.updateMemberPoint(m);
			
			loginUser.setMemberPoint(loginUser.getMemberPoint() + point);
			
			session.setAttribute("loginUser", loginUser);
			return sResult + uResult == 2 ? "success" : "fail";

	}

	@PostMapping("canclePay.pm")
	@ResponseBody
	public String refundComplete(HttpSession session,
			@RequestParam("merchantUidList") ArrayList<String> merchantUidList,
			@RequestParam("cancleAmount") int cancleAmount, @RequestParam("cancelPoint") int cancelPoint,
			@RequestParam("cancelReason") String cancelReason,
			@RequestParam("payNoList")ArrayList<Integer> payNoList)throws IOException {
		
		Member loginUser = (Member) session.getAttribute("loginUser");
		int result = 0;
//		System.out.println("유저 포인트 : " + loginUser.getMemberPoint());
//		System.out.println("환불 포인트 : " + cancelPoint);
//		System.out.println("환불할 결제 번호 : " + payNoList);
//		System.out.println("회원 현 포인트 : " + loginUser.getMemberPoint());
		if (loginUser.getMemberPoint() >= cancelPoint) {
			for(int i = 0 ; i < payNoList.size(); i++) {
				//취소 회원 번호, 취소 사유, 취소 구분 타입,
				Cancel cancel = new Cancel();
				cancel.setCancelMemNo(loginUser.getMemberNo());
				cancel.setCancelComent(cancelReason);
				cancel.setCancelRefNo(payNoList.get(i));
				cancel.setCancelRefType("PAYMENT");
				pService.insertCancelReason(cancel);
			}
			
			for (int i = 0; i < merchantUidList.size(); i++) {
				String token = rService.getToken(apikey, secretkey);
				System.out.println("토큰 번호 : " + token);
				rService.refundRequest(token, merchantUidList.get(i));
				result = pService.deletePayments(merchantUidList.get(i));
				
				
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("memberNo", loginUser.getMemberNo());
			map.put("canclePoint", cancelPoint);
			int result2 = pService.minusPoint(map);
//			System.out.println("포인트 차감 : " + result2);
//			System.out.println(result + result2);
			
			loginUser.setMemberPoint(loginUser.getMemberPoint()-cancelPoint);
			session.setAttribute("loginUser", loginUser);
			return result + result2 >= 2 ? "success" : "fail";
		}else {
			return "shortage";
		}
	}
}
