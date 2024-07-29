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
import com.finalproject.triprecord.common.Pagination;
import com.finalproject.triprecord.common.model.vo.PageInfo;

@RestController
public class PlaceRestController {
	
	public static final String SERVICEKEY = "BLrXOBbtbM7aAzBv8Cw6JpcKbQICvl4WGca4%2F4EXvcBWkVtqqtW4UgPuJgEBE4BkaIQcO0OjCqWmVo5gO8KGVQ%3D%3D";
	
	public static String basicUrl = "http://apis.data.go.kr/B551011/KorService1/";
	
	@GetMapping("recoPlaceList.pla")
	@ResponseBody
	public Map<String, Object> recoPlaceList(@RequestParam(value="page") int page,
											 @RequestParam(value="arrange", defaultValue = "A") String arrange,
											 @RequestParam(value="selectedKeyword", required=false) String selectedKeyword,
											 @RequestParam(value="areaCode", required=false) String areaCode) {
		String parameter = "";
		
		parameter += "?numOfRows=" + 12;
		parameter += "&pageNo=" + page;
		parameter += "&MobileOS=ETC&MobileApp=AppTest";
		parameter += "&ServiceKey=" + SERVICEKEY;
		parameter += "&arrange=" + arrange;
		parameter += "&contentTypeId=" + selectedKeyword;
		parameter += "&areaCode=" + areaCode;
		parameter += "&_type=json";
		
		String operation = "areaBasedList1";
		
		String adrr = basicUrl + operation + parameter;
		//System.out.println(adrr);
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
        //PageInfo pi = Pagination.getPageInfo(page, 12, 12);
        try {
            jsonMap = mapper.readValue(sb.toString(), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(jsonMap);
//       System.out.println(jsonMap.get("response"));
        Map<String, Object> responseMap = (Map<String, Object>) jsonMap.get("response");
        Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
        int totalCount = (Integer) bodyMap.get("totalCount");
        
        PageInfo pi = Pagination.getPageInfo(page, totalCount, 12);
        jsonMap.put("pi", pi);
        jsonMap.put("arrange", arrange);
        jsonMap.put("selectedKeyword", selectedKeyword);
        jsonMap.put("areaCode", areaCode);
        
        return jsonMap;
	}
	
	@GetMapping("placeInfo.pla")
	public Map<String, Object> placeInfo(@RequestParam("contentid")int contentid,
										 @RequestParam("contenttypeid") int contenttypeid){
		String operation = "detailCommon1";
		
		String parameter = "";
		
		parameter += "?MobileOS=ETC&MobileApp=AppTest";
		parameter += "&ServiceKey=" + SERVICEKEY;
		parameter += "&contentId=" + contentid;
		parameter += "&contentTypeId=" + contenttypeid;
		parameter += "&defaultYN=Y";
		parameter += "&firstImageYN=Y";
		parameter += "&addrinfoYN=Y";
		parameter += "&overviewYN=Y";
		parameter += "&_type=json";
		
		String adrr = basicUrl + operation + parameter;
		
		//System.out.println(adrr);
		
		StringBuffer sb = new StringBuffer();
		
		try {
			URL url = new URL(adrr);
			
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection(); 
			
			BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "UTF-8"));
			
			String resultStr;
			
			while((resultStr = br.readLine()) != null) {
				sb.append(resultStr);
			}
			
			urlCon.disconnect();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// JSON 문자열을 Map으로 변환
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = null;
        //PageInfo pi = Pagination.getPageInfo(page, 12, 12);
        try {
            jsonMap = mapper.readValue(sb.toString(), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
       // System.out.println(jsonMap);
//       System.out.println(jsonMap.get("response"));
        Map<String, Object> responseMap = (Map<String, Object>) jsonMap.get("response");
        Map<String, Object> bodyMap = (Map<String, Object>) responseMap.get("body");
        
        return jsonMap;
	}
	
	@GetMapping("detailImg.pla")
	public Map<String, Object> detailImg(@RequestParam("contentid")int contentid,
										 @RequestParam("contenttypeid") int contenttypeid){
		String operation = "detailImage1";
		
		String parameter = "";
		
		parameter += "?MobileOS=ETC&MobileApp=AppTest";
		parameter += "&ServiceKey=" + SERVICEKEY;
		parameter += "&contentId=" + contentid;
		if(contenttypeid == 39) {
			parameter += "&imageYN=N";
		}else {
			parameter += "&imageYN=Y";
		}
		parameter += "&subImageYN=Y";
		parameter += "&_type=json";
		
		String adrr = basicUrl + operation + parameter;
		
		//System.out.println(adrr);
		
		StringBuffer sb = new StringBuffer();
		
		try {
			URL url = new URL(adrr);
			
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection(); 
			
			BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "UTF-8"));
			
			String resultStr;
			
			while((resultStr = br.readLine()) != null) {
				sb.append(resultStr);
			}
			
			urlCon.disconnect();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// JSON 문자열을 Map으로 변환
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = null;
        //PageInfo pi = Pagination.getPageInfo(page, 12, 12);
        try {
            jsonMap = mapper.readValue(sb.toString(), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
       //System.out.println(jsonMap);
        
        return jsonMap;
	}
	
	@GetMapping("detailTypeInfo.pla")
	public Map<String, Object> detailTypeInfo(@RequestParam("contentid") int contentId,
											  @RequestParam("contenttypeid") int contentTypeId){
		String operation = "detailIntro1";
		
		String parameter = "";
		
		parameter += "?MobileOS=ETC&MobileApp=AppTest";
		parameter += "&ServiceKey=" + SERVICEKEY;
		parameter += "&contentId=" + contentId;
		parameter += "&contentTypeId=" + contentTypeId;
		parameter += "&_type=json";
		
		String adrr = basicUrl + operation + parameter;
		
		// System.out.println(adrr);
		
		StringBuffer sb = new StringBuffer();
		
		try {
			URL url = new URL(adrr);
			
			HttpURLConnection urlCon = (HttpURLConnection)url.openConnection(); 
			
			BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getInputStream(), "UTF-8"));
			
			String resultStr;
			
			while((resultStr = br.readLine()) != null) {
				sb.append(resultStr);
			}
			
			urlCon.disconnect();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// JSON 문자열을 Map으로 변환
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> jsonMap = null;
        //PageInfo pi = Pagination.getPageInfo(page, 12, 12);
        try {
            jsonMap = mapper.readValue(sb.toString(), new TypeReference<Map<String, Object>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
       //System.out.println(jsonMap);
//       System.out.println(jsonMap.get("response"));
        return jsonMap;
	}
}
