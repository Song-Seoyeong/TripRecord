package com.finalproject.triprecord.member.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.triprecord.common.model.service.GoogleDriveService;
import com.finalproject.triprecord.member.model.service.MemberService;
import com.finalproject.triprecord.member.model.vo.Member;
import com.finalproject.triprecord.member.model.vo.Planner;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class MemberController {

	@Autowired
	private MemberService mService;

	@Autowired
	private BCryptPasswordEncoder bcrypt;

	@Autowired
	private GoogleDriveService gdService;

	@GetMapping("loginView.me")
	public String loginView(@RequestParam(value="success",required=false) String success, Model model) {
		if(success != null) {
			model.addAttribute("success", success);
		}
		return "login";
	}

	@GetMapping("enrollView.me")
	public String enrollView() {
		return "enrollMember";
	}

	@GetMapping("searchIdView.me")
	public String searchId() {
		return "searchId";
	}

	@GetMapping("searchPwdView.me")
	public String searchPwd() {
		return "searchPwd";
	}

	@PostMapping("login.me")
	public String login(@RequestParam("memberId") String id, @RequestParam("memberPwd") String pwd,
			@RequestParam("beforeURL") String beforeURL, HttpSession session, Model model) {
		Member m = new Member();
		m.setMemberId(id);
		m.setMemberPwd(pwd);
		Member loginUser = mService.login(m);
		if (loginUser != null) {
			if (bcrypt.matches(m.getMemberPwd(), loginUser.getMemberPwd())) {
				session.setAttribute("loginUser", loginUser);

				if (!loginUser.getGrade().equals("ADMIN")) {
					if (beforeURL.contains(".me")) {
						return "redirect:home";
					} else {
						return "redirect:" + beforeURL;
					}
				} else {
					return "redirect:dashBoard.ad";
				}

			} else {
				model.addAttribute("error", "error");
				return "login";
			}
		} else {
			model.addAttribute("error", "error");
			return "login";
		}
	}

	@GetMapping("logout.me")
	public String logout(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session != null) {
			session.invalidate();
		}
		return "index";
	}

	@GetMapping("updatePwdView.me")
	public String updatePwd() {
		return "updatePwd";
	}

	@GetMapping("enrollPlannerView.me")
	public String enrollPlanner() {
		return "enrollPlanner";
	}

	@PostMapping("enrollMember.me")
	public String enrollMember(@ModelAttribute Member m, @RequestParam("memEmail") String frontEmail,
			@RequestParam("domain") String backEmail, RedirectAttributes ra) {
		String email = null;
		if (!frontEmail.equals("")) {
			email = frontEmail + "@" + backEmail;
		}
		m.setEmail(email);
		String encPwd = bcrypt.encode(m.getMemberPwd());
		m.setMemberPwd(encPwd);

		int result = mService.enrollMember(m);
		if (result > 0) {
			ra.addAttribute("success", "success");
			return "redirect:loginView.me";
		} else {
			return "에러페이지";
		}
	}

	@PostMapping("enrollPlanner.me")
	public String enrollPlanner(@ModelAttribute Member m, @ModelAttribute Planner p,
			@RequestParam("memEmail") String frontEmail, @RequestParam("domain") String backEmail, RedirectAttributes ra) {

		String email = null;
		if (!frontEmail.equals("")) {
			email = frontEmail + "@" + backEmail;
		}
		m.setEmail(email);
		String encPwd = bcrypt.encode(m.getMemberPwd());
		m.setMemberPwd(encPwd);
		m.setGrade("PLANNER");
		int result = mService.enrollMember(m); // 플래너회원가입 시키기
		if (result > 0) {
			Member mem = mService.login(m);
			p.setMemberNo(mem.getMemberNo()); // 로그인 정보에서 memberNo 가져오기
			int result2 = mService.enrollPlanner(p); // 플래너 추가정보 입력하기
			if (result2 > 0) {
				ra.addAttribute("success", "success");
				return "redirect:loginView.me";
			} else {
				return "에러페이지";
			}
		} else {
			return "에러페이지";
		}
	}

	@PostMapping("checkId.me")
	@ResponseBody
	public Integer checkId(@RequestParam("id") String id) {
		return mService.checkId(id);
	}

	@GetMapping("findId.me")
	@ResponseBody
	public Member findId(@ModelAttribute Member m) {
		m = mService.findId(m);
		return m;
	}

	@GetMapping("findPwd.me")
	@ResponseBody
	public Member findPwd(@ModelAttribute Member m, Model model) {
		Member mem = mService.findPwd(m);
		return mem;
	}

	@PostMapping("updatePwd.me")
	public String updatePwd(@ModelAttribute Member m) {
		m.setMemberPwd(bcrypt.encode(m.getMemberPwd()));
		int result = mService.updatePwd(m);
		if (result > 0) {
			return "redirect:loginView.me";
		} else {
			return "error";
		}
	}
	
	@PostMapping("loginLogPrint.me")
	@ResponseBody
	public void loginLogPrint(@RequestParam("loginUser") String loginUser) {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		
		try {
			gdService.logActivity(
					format.format(date) + " [INFO] - " + loginUser + "님이 로그인하였습니다.");
		} catch (IOException e) {

		}
	}
}
