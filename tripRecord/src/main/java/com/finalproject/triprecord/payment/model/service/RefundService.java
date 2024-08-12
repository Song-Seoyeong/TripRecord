package com.finalproject.triprecord.payment.model.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

@Service
public class RefundService {

	public String getToken(String apikey, String secretkey) throws IOException {

		URL url = new URL("https://api.iamport.kr/users/getToken");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		// 요청 방식 POST
		conn.setRequestMethod("POST");

		// 요청의 Content-Type과 Accept 헤더 설정
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");

		// 해당 연결을 출력 스트림(요청)으로 사용
		conn.setDoOutput(true);

		// JSON 객체에 해당 API가 필요로하는 데이터 추가.
		JsonObject json = new JsonObject();
		json.addProperty("imp_key", apikey);
		json.addProperty("imp_secret", secretkey);

		// 출력 스트림으로 해당 conn에 요청
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		bw.write(json.toString()); // json 객체를 문자열 형태로 HTTP 요청 본문에 추가
		bw.flush();
		bw.close();

		// 입력 스트림으로 conn 요청에 대한 응답 반환
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		Gson gson = new Gson(); // 응답 데이터를 자바 객체로 변환
		String response = gson.fromJson(br.readLine(), Map.class).get("response").toString();
		String accessToken = gson.fromJson(response, Map.class).get("access_token").toString();
		br.close();

		conn.disconnect(); // 연결 종료

		return accessToken;
	}

	public void refundRequest(String access_token, String merchantUid) throws IOException {
		URL url = new URL("https://api.iamport.kr/payments/cancel");
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

		// 요청 방식을 POST로 설정
		conn.setRequestMethod("POST");

		// 요청의 Content-Type, Accept, Authorization 헤더 설정
		conn.setRequestProperty("Content-type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		conn.setRequestProperty("Authorization", access_token);

		// 해당 연결을 출력 스트림(요청)으로 사용
		conn.setDoOutput(true);

		// JSON 객체에 해당 API가 필요로하는 데이터 추가.
		JsonObject json = new JsonObject();
		json.addProperty("merchant_uid", merchantUid);

		// 출력 스트림으로 해당 conn에 요청
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
		bw.write(json.toString());
		bw.flush();
		bw.close();

		// 입력 스트림으로 conn 요청에 대한 응답 반환
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		br.close();
		conn.disconnect();
	}

}
