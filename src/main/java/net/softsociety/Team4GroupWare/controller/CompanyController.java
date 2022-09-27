package net.softsociety.Team4GroupWare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.service.CompanyService;

@Slf4j
@Controller
@RequestMapping("company")
public class CompanyController {
	
	@Autowired
	CompanyService service;
	
	@GetMapping("signup")
	public String signup() {
		return "companyView/signup";
	}
	
	@PostMapping("join")
	public String join(Company company, Employee employee) {
		log.debug("가져온객체 : {}, {}", company, employee);
		
		int result = service.insertCompany(company);
		
		if(result == 1) {
			Company company2 = service.readCompany(company.getCompany_business_num());
			
			employee.setCompany_code(company2.getCompany_code());
			int result2 = service.insertAdmin(employee);
			
			if(result2 != 1) {
				log.debug("회사 생성 실패");
			}
		}
		return "redirect:/";
	}
	
	@ResponseBody
	@PostMapping({"checkid"})
	public int checkid(String employee_id) {
		log.debug("가져온 id : {}", employee_id);
		int result = 0;
		
		result = service.checkID(employee_id);
		
		return result;
	}
	
	@ResponseBody
	@PostMapping({"checkBusinessNum"})
	public int checkBusinessNum(String company_business_num) {
		log.debug("가져온 id : {}", company_business_num);
		int result = 0;
		
		result = service.checkBusinessNum(company_business_num);
		
		return result;
	}
	
	@ResponseBody
	@GetMapping("findCompany")
	public String findCompany(String company_business_num) {
		Company company = service.readCompany(company_business_num);
		Employee employee = service.readAdmin(company.getCompany_code());
		
		String findedID = employee.getEmployee_id();
		
		return findedID;
	}
}
