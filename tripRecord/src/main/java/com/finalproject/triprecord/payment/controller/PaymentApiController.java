package com.finalproject.triprecord.payment.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
	// ResponseEntitiy 사용 시 결과값, 상태코드, 헤더값을 프론트에 넘겨주기 가능
	@PostMapping("payment.pm")
	public ResponseEntity<String> paymentComplete(HttpSession session, @RequestParam("impUid") String impUid,
			@RequestParam("merchantUid") String merchantUid, @RequestParam("poNo") int pointNo,
			@RequestParam("point") int point) {

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

	@PostMapping("canclePay.pm")
	@ResponseBody
	public String refundComplete(HttpSession session,
			@RequestParam("merchantUidList") ArrayList<String> merchantUidList,
			@RequestParam("cancleAmount") int cancleAmount, @RequestParam("cancelPoint") int canclePoint)
			throws IOException {
		
		int memberNo = ((Member) session.getAttribute("loginUser")).getMemberNo();
		int memberPoint = ((Member) session.getAttribute("loginUser")).getMemberPoint();
		if (memberPoint >= canclePoint) {
			for (int i = 0; i < merchantUidList.size();) {
				String token = rService.getToken(apikey, secretkey);
				System.out.println("토큰 번호 : " + token);
				rService.refundRequest(token, merchantUidList.get(i));
				
				int result2 = pService.deletePayments(merchantUidList.get(i));
				
				if(result2 > 0) {
					return "success1";
				}else {
					return "fail1";
				}
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("memberNo", memberNo);
			map.put("canclePoint", canclePoint);
			System.out.println(map);
			int r = pService.minusPoint(map);
			System.out.println("포인트 차감 : " + r);
			return "success2";
		} else {
			return "fail2";
		}
	}
}
