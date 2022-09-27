package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Company;
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
		
		@PostMapping("addOrganization")
		public int addOrganization(Organization org, @AuthenticationPrincipal UserDetails user) {
			log.debug("가져온 조직 : {}", org);
			//1. 컴퍼니 아이디 가져와서 저장
			Employee employee = adminservice.readAdmin(user.getUsername());
			org.setCompany_code(employee.getCompany_code());
			
			//2. 부모 아이디에 가지고 있는 부서 아이디의 끝번호를 가져와서.. 저장? 그리고 그 번호 +1해서 org에 부서 아이디로 저장
			ArrayList<Organization> orgList = adminservice.findByParentId(org.getParent_id());
			
			String department_node = null;
			for(int i = 0; i < orgList.size(); i++) {
				department_node = orgList.get(i).getDepartment_id();
			}
			org.setDepartment_id(department_node);
			
			int result = adminservice.addOrganization(org);
			
			return result;
		}
		
		@PostMapping("addEmpForOrg")
		public int addEmpForOrg(Employee employee) {
			log.debug("가져온 멤버 : {}", employee);
			
			int result = adminservice.addEmpForOrg(employee);
			
			return result;
		}

}
