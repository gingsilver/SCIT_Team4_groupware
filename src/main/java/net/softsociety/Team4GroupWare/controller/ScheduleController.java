package net.softsociety.Team4GroupWare.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Schedule;
import net.softsociety.Team4GroupWare.service.EmployeeService;
import net.softsociety.Team4GroupWare.service.ScheduleService;

@Controller
@RequestMapping("schedule")
@Slf4j
public class ScheduleController {
	
	@Autowired
	EmployeeService employeeService;

	@Autowired
	ScheduleService scheduleService;
	
	//스케줄 메인화면 출력(테스트)
	@GetMapping("/main")
	public String main(@AuthenticationPrincipal UserDetails user) {
		
		log.debug("{}", user);
		
		return "schedule/calendar";
	}
	
	//스케줄화면 출력
	@GetMapping("/list")
	public String list(Schedule schedule, @AuthenticationPrincipal UserDetails user, Model model) {
		
		log.debug("{}", user);
		
		Employee employee = employeeService.getEmployeeById(user.getUsername());
		
		String company_code = employee.getCompany_code();
		model.addAttribute("company_code", company_code);
		
		return "schedule/schedule_list";
	}
	
	//스케줄리스트 출력
	@ResponseBody
	@PostMapping("/selectScheduleList")
	public List<Schedule> postSelectScheduleList(@RequestParam String company_code, 
					@AuthenticationPrincipal UserDetails user, Model model) {
		log.debug("postSelectScheduleList() called");
		log.debug("company_code : {}", company_code);
		
		List<Schedule> scheduleList = scheduleService.readEmployeeSchedule(company_code, user.getUsername());
		log.debug("scheduleList : {}", scheduleList);
		
		model.addAttribute("scheduleList", scheduleList);
		
		return scheduleList;
	}
	
	//스케줄 저장
	@PostMapping("/save")
	public String save(Schedule schedule, @AuthenticationPrincipal UserDetails user) {
		schedule.setSchedule_startDate(schedule.getSchedule_startDate().replace("T", " "));
		schedule.setSchedule_endDate(schedule.getSchedule_endDate().replace("T", " "));
		if(schedule.getSchedule_startDate().substring(0, 10).equals(schedule.getSchedule_endDate().substring(0, 10)))
			schedule.setSchedule_allDay("0");
		else
			schedule.setSchedule_allDay("1");
		log.debug("저장할 스케줄:{}", schedule);
		log.debug(schedule.getSchedule_startDate().substring(0, 10));
		
		schedule.setSchedule_writer_id(user.getUsername());
		
		Employee employee = employeeService.getEmployeeById(user.getUsername());
		
		String company_code = employee.getCompany_code();
		
		schedule.setCompany_code(company_code);
		
		int result;
		if(user.getAuthorities().iterator().next().toString().equals("ROLE_USER"))
			result = scheduleService.saveEmployeeSchedule(schedule);
		else
			result = scheduleService.saveCompanySchedule(schedule);
		
		if(result != 1)
			log.debug("저장이 되지 않았습니다!");
		
		return "redirect:/schedule/list";
		
	}
	
	//스케줄 삭제
	@PostMapping("deleteSchedule")
	public String deleteSchedule(int schedule_num, @AuthenticationPrincipal UserDetails user) {
		
		int result = scheduleService.deleteSchedule(schedule_num);
		
		return "redirect:/schedule/list";
		
	}
	
	
	
	
	
}
