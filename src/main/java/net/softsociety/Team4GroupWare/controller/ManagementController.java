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
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.ConfirmedVacation;
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
			, TimeSheet timesheet
			, @AuthenticationPrincipal UserDetails user	) {
		
		Employee employee= employservice.getEmployeeById(user.getUsername());			
		Company company = companyservice.findCompanyByCompanycode(employee.getCompany_code());
		Salary salary = managementservice.seleteSalaryOne(employee.getEmployee_code());
		
		model.addAttribute("timesheet", timesheet);
		model.addAttribute("salary", salary);
		model.addAttribute("company", company);
		model.addAttribute("employee", employee);
		
		
		
		
		
		return "management/attendance";
	}
	
	//출근 기록 받아오기
		@ResponseBody
		@PostMapping("goWorkTimeRecord")
		public String goWorkTimeRecord(
				String time_sheet_start
				,@AuthenticationPrincipal UserDetails user
				) {		

			Employee employee= employservice.getEmployeeById(user.getUsername());			

			
			log.debug("가져온 개인의 출근 정보 : {}", time_sheet_start);
			
			TimeSheet timesheet = new TimeSheet();

			timesheet.setCompany_code(employee.getCompany_code());
			timesheet.setEmployee_code(employee.getEmployee_code());
			timesheet.setTime_sheet_start(time_sheet_start);
			
			log.debug("가져온 개인의 출근 출퇴근 객체 : {}", timesheet);

			
			int result = managementservice.insertStartTime(timesheet);
			
			if(result == 1) {
				log.debug("출근 시간 insert 완료!");
			}else {
				log.debug("출근 시간 insert 실패..");
			}
			
			return "management/attendance";
		}
		
		//퇴근 기록 받아오기
		@ResponseBody
		@PostMapping("goHomeTimeRecord")
		public String goHomeTimeRecord(
				String time_sheet_end
				, TimeSheet timesheet
				, @AuthenticationPrincipal UserDetails user
				) {		
			
			
			Employee employee= employservice.getEmployeeById(user.getUsername());			

			
			//먼저 찾기(임플로이 코드랑 날짜%%해서 오늘 날짜에 맞는거를 where절로 넣고)

			String resultTimeSheetCode = managementservice.findTimeSheetCode(employee.getEmployee_code());
			
			log.debug("찾아온 time_sheet_code : {}", resultTimeSheetCode);
			
			int result = managementservice.insertEndTime(resultTimeSheetCode);
			
			if(result == 1) {
				log.debug("퇴근 시간 insert 완료!");
			}else {
				log.debug("퇴근 시간 insert 실패..");
			}
			
	

			
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

	
	//시간 정정 페이지로 이동
	@GetMapping("changetime")
	public String changetime(@AuthenticationPrincipal UserDetails user,Model model) {
		Employee employee= employservice.getEmployeeById(user.getUsername());
		
		model.addAttribute("employee", employee);
		
		return "management/changetime";
	}
	
	//시간 정정 후 돌아오는 페이지
	@PostMapping("afterChange")
	public String afterChange(
			@AuthenticationPrincipal UserDetails user,Model model) {
		
		
		Employee employee= employservice.getEmployeeById(user.getUsername());			
		String time_sheet_code = managementservice.findTimeSheetCode(employee.getEmployee_code());

		int result = managementservice.adjustTimeRecord(time_sheet_code);
		
		if(result == 1) {
			System.out.println("시간 정정 update 완료!");
		}else {
			System.out.println("시간 정정 update 실패..");
		}
		
		model.addAttribute("employee", employee);
		
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
		ConfirmedVacation confirmedvacation = new ConfirmedVacation();
		
        log.debug("연차정보가 나오는지:{}", confirmedvacation);
        log.debug("휴무신청서정보가 나오는지:{}", vacation);
        
		
        
		model.addAttribute("vacation", vacation);  
		model.addAttribute("employee", employee);
		
		
		return "management/dayoff";
	}
	
	@ResponseBody
	@PostMapping("vacation")
	public Vacation vacation(
			@AuthenticationPrincipal UserDetails user
			) {
		Employee employee= employservice.getEmployeeById(user.getUsername());	
		Company company = companyservice.findCompanyByCompanycode(employee.getCompany_code());
		Vacation vacation = managementservice.getVacationdays(employee.getEmployee_code());
		
        log.debug("ajax - salary :{}", vacation);
        
			return vacation;	
		}
	
	//휴무 신청서 저장
	@PostMapping("dayoff")
	public String dayoff(
			Vacation vacation
			, ConfirmedVacation confirmedvacation
			,@AuthenticationPrincipal UserDetails user
			) {		
		
		Employee employee= employservice.getEmployeeById(user.getUsername());	
		vacation = managementservice.getVacationdays(employee.getEmployee_code());
		

		log.debug("가져온 개인의 연차 정보 : {}", vacation);
		log.debug("가져온 휴가신청서 정보 : {}", confirmedvacation);
		
		confirmedvacation.setCompany_code(employee.getCompany_code());
		confirmedvacation.setEmployee_code(employee.getEmployee_code());
		
		String endDate = confirmedvacation.getConfirmed_vacation_Enddate();
		String startDate = confirmedvacation.getConfirmed_vacation_date();
		
		String a = endDate.substring(9,10);
		String b = startDate.substring(9,10);		
	    
	    int sum = Integer.parseInt(a) -  Integer.parseInt(b);	
		confirmedvacation.setConfirmed_vacation_days(sum);
		
		//마이너스 연차 차감시키기

		log.debug("차감전 연차 : {}", vacation.getVacation_annual_minus());	
		
		int totalMinus = vacation.getVacation_annual_minus() - sum;
		vacation.setVacation_annual_minus(totalMinus);
		
		//service vacant update랑 confirmed 삽입
		int result1 = managementservice.updateMinusVacation(vacation.getVacation_annual_minus(), employee.getEmployee_code());
			if(result1 == 1) {
				System.out.println("차감된 연차 update 완료!");
				log.debug("갱신된 연차 : {}", vacation.getVacation_annual_minus());
			}else {
				System.out.println("차감된 연차 update 실패..");
			}
		
		int result2 = managementservice.insertConfirmedVacation(confirmedvacation);
		if(result2 == 1) {
			System.out.println("휴무 신청서 insert 완료");
		}else {
			System.out.println("휴무 신청서 insert 실패!");
		}

		
		return "redirect:/management/dayoff";				
	}
	
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
		
        log.debug("객체로 4개 불러옴:{}", salary);
        log.debug("객체로 4개 불러옴:{}", tax);

	    
		model.addAttribute("company", company);
		model.addAttribute("employee", employee);
        
		return "management/salary";
	}
	
	@ResponseBody
	@PostMapping("salary")
	public Salary salary(
			@AuthenticationPrincipal UserDetails user
			) {
		
		Employee employee= employservice.getEmployeeById(user.getUsername());	
		Company company = companyservice.findCompanyByCompanycode(employee.getCompany_code());
		Salary salary = managementservice.seleteSalaryOne(employee.getEmployee_code());
		
        log.debug("ajax - salary :{}", salary);
        
			return salary;	
		}
	@ResponseBody
	@PostMapping("readTax")
	public Tax readTax(
			@AuthenticationPrincipal UserDetails user
			) {
		
		Date date = new Date();
		@SuppressWarnings("deprecation")
		int year = date.getYear() -100;
        String tax_year = Integer.toString(year);
        log.debug("올해정보나오는지:{}", tax_year);
        
		Employee employee= employservice.getEmployeeById(user.getUsername());	
		Company company = companyservice.findCompanyByCompanycode(employee.getCompany_code());
		Tax tax = managementservice.selectTaxInfo(employee.getEmployee_code(), tax_year);
		
        log.debug("ajax - tax : {}", tax);
        
			return tax;	
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
		
//		//근로계약서 작성시 처음으로 기록되는 출퇴근 코드는 모든 회원이 동일하게 TS0001이므로 여기서 값을 TS0001로 고정
//		String time_sheet_code = "TS0001";
//		TimeSheet timesheet = managementservice.selectTimesheetOne(employee.getEmployee_code(), time_sheet_code);
//		
//		log.debug("DB에서 넘어온 timesheet 정보 : {} ", timesheet);
//		String startTime = timesheet.getTime_sheet_start().substring(11, 16);
//		String endTime = timesheet.getTime_sheet_end().substring(11, 16);
//		
//		log.debug("startTime : {} ", startTime);
//		log.debug("endTime : {} ", endTime);
//		
//		model.addAttribute("startTime", startTime);
//		model.addAttribute("endTime", endTime);
//	
//		model.addAttribute("timesheet", timesheet);
//		model.addAttribute("salary", salary);
		model.addAttribute("company", company);
		model.addAttribute("employee", employee);

		
		return "management/contract";
	}
	@ResponseBody
	@PostMapping("contract")
	public Salary contract(
			@AuthenticationPrincipal UserDetails user
			) {
		
		Date date = new Date();
		@SuppressWarnings("deprecation")
		int year = date.getYear() -100;
        String tax_year = Integer.toString(year);
        log.debug("올해정보나오는지:{}", tax_year);
        
		Employee employee= employservice.getEmployeeById(user.getUsername());	
		Company company = companyservice.findCompanyByCompanycode(employee.getCompany_code());
		Salary salary = managementservice.seleteSalaryOne(employee.getEmployee_code());
		Tax tax = managementservice.selectTaxInfo(employee.getEmployee_code(), tax_year);
		
        log.debug("ajax - salary :{}", salary);
        log.debug("ajax - tax : {}", tax);
        
			return salary;	
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
