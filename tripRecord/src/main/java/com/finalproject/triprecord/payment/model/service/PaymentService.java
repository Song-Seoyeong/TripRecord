package com.finalproject.triprecord.payment.model.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.payment.model.dao.PaymentMapper;

@Service
public class PaymentService {
	
	@Autowired
	private PaymentMapper pMapper;

	public int saveOrder(Payment payments) {
		return pMapper.saveOrder(payments);
		
	}

	public int updateMemberPoint(Member m) {
		return pMapper.updateMemberPoint(m);
		
	}

	public int minusPoint(HashMap<String, Object> map) {
		return pMapper.minusPoint(map);
		
	}

	public int deletePayments(String string) {
		return pMapper.deletePayments(string);
	}
}
