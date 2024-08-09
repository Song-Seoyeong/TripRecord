package com.finalproject.triprecord.payment.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.payment.model.dao.PaymentMapper;

@Service
public class RefundService {
	
	@Autowired
	private PaymentMapper pMapper;

	public String getToken(String apikey, String secretkey) {
		// TODO Auto-generated method stub
		return null;
	}

	public void refundRequest(String access_token, String merchantUid, String reason) {
		// TODO Auto-generated method stub
		
	}
	
	
}
