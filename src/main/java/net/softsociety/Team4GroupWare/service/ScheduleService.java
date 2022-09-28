package net.softsociety.Team4GroupWare.service;

import java.util.List;

import net.softsociety.Team4GroupWare.domain.Schedule;

public interface ScheduleService {
	
	// 회사일정 저장
	public int saveCompanySchedule(Schedule schedule);
	
	// 개인일정 저장
	public int saveEmployeeSchedule(Schedule schedule);
	
	// 회사일정 + 개인일정 불러오기
	public List<Schedule> readEmployeeSchedule(String company_code, String schedule_writer_id);
	
	//일정 삭제기능
	public int deleteSchedule(int schedule_num);

}
