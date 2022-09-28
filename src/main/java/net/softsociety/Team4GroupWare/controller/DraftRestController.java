package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;
import java.util.List;

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
		
		//결재선 추가
		@PostMapping("addApproval")
		public int addApproval(DraftApproval approval) {
			String draft_code = draftservice.createCode();
			approval.setDraft_code(draft_code);
			
			String process_turn_code = draftservice.countDraftCode(draft_code);
			if(process_turn_code == null) {
				process_turn_code = "0";
			}
			approval.setProcess_turn_code(process_turn_code);
			
			if(approval.getProcess_type().equals("참조")) {
				approval.setProcess_enabled(4);
			} else if(approval.getProcess_type().equals("결재") ||approval.getProcess_type().equals("전결")) { 
				 approval.setProcess_enabled(3); 
			}
			log.debug("가져온 결재선 : {}", approval);
			int result = draftservice.addApproval(approval);
			 
			return result;
		}

}
