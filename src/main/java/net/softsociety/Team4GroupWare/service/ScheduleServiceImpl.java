package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.ScheduleDAO;
import net.softsociety.Team4GroupWare.domain.Schedule;


@Service
@Slf4j
public class ScheduleServiceImpl implements ScheduleService {
	@Autowired
	private ScheduleDAO scheduleDAO;

	@Override
	public int saveCompanySchedule(Schedule schedule) {
		int result = scheduleDAO.saveCompanySchedule(schedule);
		
		return result;
	}

	@Override
	public int saveEmployeeSchedule(Schedule schedule) {
		int result = scheduleDAO.saveEmployeeSchedule(schedule);
		
		return result;
	}

	@Override
	public List<Schedule> readEmployeeSchedule(String company_code, String schedule_writer_id) {
		Map<String, String> map = new HashMap<>();
		map.put("company_code", company_code);
		map.put("schedule_writer_id", schedule_writer_id);
		List<Schedule> companyScheduleList = scheduleDAO.selectCompanySchedule(map);
		List<Schedule> employeeScheduleList = scheduleDAO.selectEmployeeSchedule(map);
		List<Schedule> allSchedule = new ArrayList<>();
		allSchedule.addAll(companyScheduleList);
		allSchedule.addAll(employeeScheduleList);
		return allSchedule;
	}

}
