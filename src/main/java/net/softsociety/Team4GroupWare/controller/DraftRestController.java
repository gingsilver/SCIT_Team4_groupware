package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.DraftApproval;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Organization;
import net.softsociety.Team4GroupWare.service.AdminService;
import net.softsociety.Team4GroupWare.service.DraftService;

@Slf4j
@Controller
@RequestMapping("/draft")
@ResponseBody
public class DraftRestController {
	
	// 기안 서비스 선언
	@Autowired
	DraftService draftservice;
		
	// 관리자 서비스 선언
	@Autowired
	AdminService adminservice;
	
	//조직도 불러오는 ajax 컨트롤러
		@PostMapping("readOrg")
		public JSONArray readOrg(@AuthenticationPrincipal UserDetails user) {
			//회사코드, 관리자 내용 가져오기
			Employee admin = adminservice.readAdmin(user.getUsername());
			Company company = adminservice.readCompany(admin.getCompany_code());
			
			JSONArray json = adminservice.readOrg(company);
			
			return json;
		}
		
		//조직도 내 사원 목록 불러오는 ajax 컨트롤러
		@PostMapping("searchEmployee")
		public ArrayList<Employee> searchEmployee(@AuthenticationPrincipal UserDetails user, String organization){
			Employee employee = adminservice.readAdmin(user.getUsername());
			Company company = adminservice.readCompany(employee.getCompany_code());
			
			String realOrg = organization.substring(company.getCompany_name().length()+2);
			
			log.debug("가져온 팀명 : {}", realOrg);

			employee.setOrganization(realOrg);
			
			ArrayList<Employee> empList = adminservice.findByOrganization(employee);
			
			return empList;
		}
		
		//결재선 추가 클릭시 예비 기안 코드 시퀀스 추가
		@GetMapping("addDraftSeq")
		public int addDraftSeq() {
			int result = 0;
			result = draftservice.addDraftSeq();
			
			return result;
		}
		
		//결재선 추가
		@PostMapping("addApproval")
		public int addApproval(DraftApproval approval) {
			int result = 0;
			
			log.debug("가져온 결재선 : {}", approval);
			
			return result;
		}

}
