package net.softsociety.Team4GroupWare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Salary;
import net.softsociety.Team4GroupWare.service.CompanyService;
import net.softsociety.Team4GroupWare.service.EmployeeService;
import net.softsociety.Team4GroupWare.service.ManagementService;

@Slf4j
@Controller
@RequestMapping("management")
public class ManagementController {

	//근태 서비스 선언
	@Autowired
	ManagementService managementservice;
	
	//회원 서비스 선언
	@Autowired
	EmployeeService employservice;
	
	//회사 서비스 선언
	@Autowired
	CompanyService companyservice;
	
	/*------------------------------ 출퇴근 기록 페이지 ------------------------------*/

	
	//출퇴근 기록 페이지로 이동
	@GetMapping("attendance")
	public String attendance(
			Model model
			, @AuthenticationPrincipal UserDetails user	) {
		
		Employee employee= employservice.getEmployeeById(user.getUsername());			
		Company company = companyservice.findCompanyByCompanycode(employee.getCompany_code());
		Salary salary = managementservice.seleteSalaryOne(employee.getEmployee_code());
		
		model.addAttribute("salary", salary);
		model.addAttribute("company", company);
		model.addAttribute("employee", employee);
		
		
		
		
		
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
	
	/*------------------------------ 근로계약서(read only) 페이지 ------------------------------*/

	//근로 계약서(read only) 페이지로 이동
	@GetMapping("contract")
	public String contract(
		Model model
		, @AuthenticationPrincipal UserDetails user	) {
	
		Employee employee= employservice.getEmployeeById(user.getUsername());			
		Company company = companyservice.findCompanyByCompanycode(employee.getCompany_code());
		Salary salary = managementservice.seleteSalaryOne(employee.getEmployee_code());
		
		log.debug("DB에서 넘어온 salary 정보 : {} ", salary);
		
		model.addAttribute("salary", salary);
		model.addAttribute("company", company);
		model.addAttribute("employee", employee);

		
		return "management/contract";
	}
	
	/*------------------------------ 근로계약서(작성) 페이지 ------------------------------*/

	//근로 계약서(작성) 페이지로 이동
	@GetMapping("contractWrite")
	public String contractWrite(
			Model model
			, @AuthenticationPrincipal UserDetails user	) {
		
			Employee employee= employservice.getEmployeeById(user.getUsername());			
			Company company = companyservice.findCompanyByCompanycode(employee.getCompany_code());
			Salary salary = managementservice.seleteSalaryOne(employee.getEmployee_code());
			
			log.debug("DB에서 넘어온 salary 정보 : {} ", salary);
			
			model.addAttribute("salary", salary);
			model.addAttribute("company", company);
			model.addAttribute("employee", employee);
		
		return "management/contractWrite";
	}
	
	//작성된 근로 계약서 내용 가져오기
	@PostMapping("contractWrite")
	public String contractWrite(
			Employee Employee
			, Salary salary
			, @AuthenticationPrincipal UserDetails user 
			) {
		

		//내용 불러와서
		//샐러리 객체에 넣어서
		
		
		
		return "management/contractWrite";
		
	}
	
	
}
