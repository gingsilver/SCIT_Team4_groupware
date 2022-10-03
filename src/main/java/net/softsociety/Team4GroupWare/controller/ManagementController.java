package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;
import java.util.Date;

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
import net.softsociety.Team4GroupWare.domain.Tax;
import net.softsociety.Team4GroupWare.domain.TimeSheet;
import net.softsociety.Team4GroupWare.domain.Vacation;
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
	
	/*------------------------------ 출퇴근 전체보기 페이지 ------------------------------*/

	
	//출퇴근 기록 페이지로 이동
	@GetMapping("timerecord")
	public String timerecorde(
			Model model
			, @AuthenticationPrincipal UserDetails user	) {
		
		Employee employee= employservice.getEmployeeById(user.getUsername());			
		ArrayList<TimeSheet> tlist  = managementservice.selectTimesheetOneMonth(employee.getEmployee_code());
		
		
        log.debug("tlist 값 넘어오는지 확인:{}", tlist);
        
        
		model.addAttribute("tlist", tlist);
		model.addAttribute("employee", employee);
		
		
		
		
		
		return "management/timerecord";
	}
	
	/*------------------------------ 시간 정정 페이지 ------------------------------*/

	
	//휴무 신청 페이지로 이동
	@GetMapping("changetime")
	public String changetime() {
		return "management/changetime";
	}
	
	
	/*------------------------------ 휴무 신청 페이지 ------------------------------*/

	
	//휴무 신청 페이지로 이동
	@GetMapping("dayoff")
	public String dayoff(
			Model model
			, @AuthenticationPrincipal UserDetails user) {
		
		Employee employee= employservice.getEmployeeById(user.getUsername());	
		Vacation vacation = managementservice.getVacationdays(employee.getEmployee_code());
		
        log.debug("연차정보가 나오는지:{}", vacation);
		
		model.addAttribute("vacation", vacation);  
		model.addAttribute("employee", employee);
		
		
		return "management/dayoff";
	}
	
	//휴무 신청서 저장
//	@PostMapping("write")
//	public String write(
//			Board board
//			, @AuthenticationPrincipal UserDetails user
//			, MultipartFile upload
//			) {		
//		
//		//로그인되어있는 아이디정보 가져오기, 확인
//		String id = user.getUsername();
//		log.debug("해당 아이디가 맞나요 : {}", id);
//		
//		//게시글에 로그인한 아이디 넣어주기
//		board.setMemberid(id);
//		
//		
//			
//		//전달된 게시글 제목, 내용 출력
//		log.debug("입력된 게시글 내용2 :{}", board);
//		
//		
//		//DB로 보내기
//		int result = service.writeBoard(board);
//		
//		//1이 오면 입력완료
//		if(result == 1) {
//			System.out.println("휴무 신청서 insert 완료");
//		}else {
//			System.out.println("휴무 신청서 insert 실패!");
//		}
//		
//		return "redirect:/board/list";				
//	}
	
	/*------------------------------ 급여정산서 페이지 ------------------------------*/

	
	//급여 정산서 페이지로 이동
	@GetMapping("salary")
	public String salary(
			Model model
			, @AuthenticationPrincipal UserDetails user	) {

		Date date = new Date();
		@SuppressWarnings("deprecation")
		int year = date.getYear() -100;
        String tax_year = Integer.toString(year);
        log.debug("올해정보나오는지:{}", tax_year);
		
		Employee employee= employservice.getEmployeeById(user.getUsername());	
		Company company = companyservice.findCompanyByCompanycode(employee.getCompany_code());
		Salary salary = managementservice.seleteSalaryOne(employee.getEmployee_code());
		Tax tax = managementservice.selectTaxInfo(employee.getEmployee_code(), tax_year);
		
	    log.debug("올해 세금 정보:{}", tax);
	    
	    model.addAttribute("tax", tax);
		model.addAttribute("salary", salary);
		model.addAttribute("company", company);
		model.addAttribute("employee", employee);
			
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
		
		//근로계약서 작성시 처음으로 기록되는 출퇴근 코드는 모든 회원이 동일하게 TS0001이므로 여기서 값을 TS0001로 고정
		String time_sheet_code = "TS0001";
		TimeSheet timesheet = managementservice.selectTimesheetOne(employee.getEmployee_code(), time_sheet_code);
		
		log.debug("DB에서 넘어온 timesheet 정보 : {} ", timesheet);
		String startTime = timesheet.getTime_sheet_start().substring(11, 16);
		String endTime = timesheet.getTime_sheet_end().substring(11, 16);
		
		log.debug("startTime : {} ", startTime);
		log.debug("endTime : {} ", endTime);
		
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
	
		model.addAttribute("timesheet", timesheet);
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
