package com.finalproject.triprecord.place.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PlaceRestController {
	
	public static final String SERVICEKEY = "BLrXOBbtbM7aAzBv8Cw6JpcKbQICvl4WGca4%2F4EXvcBWkVtqqtW4UgPuJgEBE4BkaIQcO0OjCqWmVo5gO8KGVQ%3D%3D";
	
	public static String basicUrl = "http://apis.data.go.kr/B551011/KorService1/areaBasedList1";
	
	@GetMapping("recoPlaceList.pla")
	@ResponseBody
	public Map<String, Object> recoPlaceList(@RequestParam(value="page", defaultValue="1", required=false) int page) {
		String parameter = "";
		
		parameter += "?numOfRows=" + 9;
		parameter += "&pageNo=" + page;
		parameter += "&MobileOS=ETC&MobileApp=AppTest";
		parameter += "&ServiceKey=" + SERVICEKEY;
		parameter += "&arrange=A&contentTypeId=&areaCode=&_type=json";
		
		String adrr = basicUrl + parameter;
		
		StringBuffer sb = new StringBuffer();
		
		try {
			URL url = new URL(adrr); // 생성한 주소를 url에 담아 URL 객체 생성
			
			HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
			// url에서 메소드를 이용하여 서버와 연결할 connection 객체 생성
			
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			
			String resultStr;
			
			while((resultStr = br.readLine()) != null) {
				sb.append(resultStr);
			}
			
			urlConnection.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// JSON 문자열을 Map으로 변환
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = null;
        try {
            jsonMap = mapper.readValue(sb.toString(), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonMap;
	}
}
