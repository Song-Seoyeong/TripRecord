package com.finalproject.triprecord.common.controller;

import java.io.IOException;

import org.springframework.stereotype.Controller;

import com.finalproject.triprecord.matching.model.vo.ReqSchedule;
import com.finalproject.triprecord.member.model.vo.Member;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Controller
public class SendMessageController {
	
	 final DefaultMessageService MESSAGESERVICE;

    public SendMessageController() {
        // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
        this.MESSAGESERVICE = NurigoApp.INSTANCE.initialize("NCSIRZZB3NMIXZCS", "EOEHWXTEYAMMZF8ABW3OURFJ4OFGMB7Z", "https://api.coolsms.co.kr");
    }
    
    // 환불 문자
    public SingleMessageSentResponse sendMmsByResourcePath(ReqSchedule r, Member m, int msgType) throws IOException {
    	//System.out.println("메세지 전");
        Message message = new Message();
        message.setFrom("01066171079");
        message.setTo(m.getPhone());
        String msg = "안녕하세요. " + m.getNickname() + "님 :)";
        if(msgType == 1) { // 회원 환불
        	msg += " 죄송합니다. 여행 날짜까지 플래너가 일정을 생성하지 않아 결제가 환불되었습니다. 죄송한 마음을 담아 결제하신 포인트의 10%를 추가로 지급해드렸습니다. 감사합니다.";
        }else if(msgType == 2) { // 플래너 경고
        	msg += "신청 여행의 시작 날짜까지 일정을 생성하지않아 플래너 패널티를 부여합니다. 경고 3회를 받을 경우 불이익이 있을 수 있습니다.";
        } else if(msgType == 3) { // 자동 구매 확정
        	msg += "일정 도착 후 여행 시작 일자까지 구매확정을 하지않아 자동으로 구매확정으로 전환됩니다.";
        }
        message.setText(msg);
        //System.out.println("메세지 후");
        SingleMessageSentResponse response = this.MESSAGESERVICE.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }
}
