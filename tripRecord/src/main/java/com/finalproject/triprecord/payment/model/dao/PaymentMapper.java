package com.finalproject.triprecord.payment.model.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import com.finalproject.triprecord.common.model.vo.Payment;
import com.finalproject.triprecord.member.model.vo.Member;

@Mapper
public interface PaymentMapper {

	int saveOrder(Payment payments);

	int updateMemberPoint(Member m);

	int minusPoint(HashMap<String, Object> map);

	int deletePayments(String string);

}
