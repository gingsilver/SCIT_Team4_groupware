package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.service.AdminService;
import net.softsociety.Team4GroupWare.service.DraftService;

@Controller
@RequestMapping("/draft")
public class DraftController {
	
	@Autowired
	DraftService draftservice;
	
	@Autowired
	AdminService adminservice;
	
	@GetMapping("/main")
	public String draftmain() {
		return "draftView/draftmain";
	}
	
	@GetMapping("/write")
	public String writeDdraft(@AuthenticationPrincipal UserDetails user, Model model) {
		// 회사코드, 관리자 내용 가져오기
		Employee employee = draftservice.readEmployee(user.getUsername());
		Company company = adminservice.readCompany(employee.getCompany_code());
		JSONArray json = adminservice.readOrg(company);
				
		ArrayList<Employee> empList = adminservice.employeeList(employee);
				
		for(int i = 0; i < empList.size(); i++) {
			if(empList.get(i).getRole_name().equals("ROLE_ADMIN")) {
				empList.remove(i);
			}
		}
				
		model.addAttribute("employee", employee);
		model.addAttribute("company", company);
		model.addAttribute("json", json);
		model.addAttribute("empList",empList);
		
		return "draftView/writedraft";
	}
}
