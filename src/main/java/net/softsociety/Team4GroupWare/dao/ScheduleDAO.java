package net.softsociety.Team4GroupWare.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.Team4GroupWare.domain.Schedule;

@Mapper
public interface ScheduleDAO {
	
	// 회사일정 저장
	public int saveCompanySchedule(Schedule schedule);
	
	// 개인일정 저장
	public int saveEmployeeSchedule(Schedule schedule);
	
	//회사일정 불러오기
	public List<Schedule> selectCompanySchedule(Map<String, String> map);

	//개인일정 불러오기
	public List<Schedule> selectEmployeeSchedule(Map<String, String> map);
	


}
