package net.softsociety.Team4GroupWare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.service.EmailService;
import net.softsociety.Team4GroupWare.service.ManagementService;

@Slf4j
@Controller
@RequestMapping("management")
public class ManagementController {

	@Autowired
	ManagementService service;
	
	/*------------------------------ 출퇴근 기록 페이지 ------------------------------*/

	
	//출퇴근 기록 페이지로 이동
	@GetMapping("attendance")
	public String attendance() {
		return "management/attendance";
	}
	
	
	/*------------------------------ 휴무 신청 페이지 ------------------------------*/

	
	//휴무 신청 페이지로 이동
	@GetMapping("dayoff")
	public String dayoff() {
		return "management/dayoff";
	}
	
	
	/*------------------------------ 급여정산서 페이지 ------------------------------*/

	
	//급여 정산서 페이지로 이동
	@GetMapping("salary")
	public String salary() {
		return "management/salary";
	}
	
	/*------------------------------ 근로계약서 페이지 ------------------------------*/

	//근로 계약서 페이지로 이동
	@GetMapping("contract")
	public String contract() {
		

		
		return "management/contract";
	}
	
	
}
