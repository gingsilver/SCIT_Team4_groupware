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
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.service.AdminService;
import net.softsociety.Team4GroupWare.service.DraftService;

@Slf4j
@Controller
@RequestMapping("/draft")
public class DraftController {
	
	@Autowired
	DraftService draftservice;
	
	@Autowired
	AdminService adminservice;
	
	@GetMapping("/main")
	public String draftmain(@AuthenticationPrincipal UserDetails user, Model model) {
		Employee admin = adminservice.readAdmin(user.getUsername());
		
		DocumentForm doc = new DocumentForm();
		doc.setCompany_code(admin.getCompany_code());
		doc.setDocument_form_writer_code(admin.getEmployee_code());
		
		ArrayList<DocumentForm> docform = draftservice.selectAllDoc(doc);
		
		model.addAttribute("docform", docform);
		
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
	
	@GetMapping("/writedoc")
	public String writedoc() {
		return "draftView/writedoc";
	}
	
	@GetMapping({"readDoc"})
	public String readDoc(
			@RequestParam(name="document_form_code", defaultValue = "0") String document_form_code
			, Model model
			, @AuthenticationPrincipal UserDetails user) {
		//db에서 글을 읽어서
		log.debug("글 번호 : {}", document_form_code);
		DocumentForm docform = adminservice.findDocByCode(document_form_code);
		
		if(docform.equals(null)) {
			return "redirect:/admin/adminDraft"; //글이 없을때
		}
		
		//결과를 모델에 담아서 html에서 출력
		model.addAttribute("docform", docform);
		
		return "draftView/readDoc";
	}
}
