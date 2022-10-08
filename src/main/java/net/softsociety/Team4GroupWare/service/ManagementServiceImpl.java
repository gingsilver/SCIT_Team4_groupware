package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.EmailDAO;
import net.softsociety.Team4GroupWare.dao.ManagementDAO;
import net.softsociety.Team4GroupWare.domain.ConfirmedVacation;
import net.softsociety.Team4GroupWare.domain.Salary;
import net.softsociety.Team4GroupWare.domain.Tax;
import net.softsociety.Team4GroupWare.domain.TimeSheet;
import net.softsociety.Team4GroupWare.domain.Vacation;

@Slf4j
@Service
public class ManagementServiceImpl implements ManagementService {

	@Autowired
	ManagementDAO managementDAO;

	@Override
	public Salary seleteSalaryOne(String employee_code) {
		Salary salary = managementDAO.seleteSalaryOne(employee_code);
		return salary;
	}

	@Override
	public Tax selectTaxInfo(String employee_code, String tax_year) {
		HashMap<String, String> map = new HashMap<>();
		
		map.put("employee_code", employee_code);
		map.put("tax_year", tax_year);
		
		
		Tax tax = managementDAO.selectTaxInfo(map);
		return tax;
	}

	@Override
	public TimeSheet selectTimesheetOne(String employee_code, String time_sheet_code) {
		HashMap<String, String> map = new HashMap<>();
		
		map.put("employee_code", employee_code);
		map.put("time_sheet_code", time_sheet_code);
		
		TimeSheet  timesheet = managementDAO.selectTimesheetOne(map);
		
		return timesheet;
	}

	@Override
	public TimeSheet selectTimesheetOnlyTime(String employee_code) {
		TimeSheet  timesheet = managementDAO.selectTimesheetOnlyTime(employee_code);
		return timesheet;
	}

	@Override
	public ArrayList<TimeSheet> selectTimesheetOneMonth(String employee_code) {
		ArrayList<TimeSheet> tlist = managementDAO.selectTimesheetOneMonth(employee_code);
		
		return tlist;
	}

	@Override
	public Vacation getVacationdays(String employee_code) {
		Vacation vacation = managementDAO.getVacationdays(employee_code);
		return vacation;
	}

	@Override
	public int updateMinusVacation(int vacation_annual_minus, String employee_code) {
        Map<String, Object> map = new HashMap<>();
		
        map.put("employee_code", employee_code);
		map.put("vacation_annual_minus", vacation_annual_minus);
		
		int result = managementDAO.updateMinusVacation(map);
		return result;
	}

	@Override
	public int insertConfirmedVacation(ConfirmedVacation confirmedvacation) {
		int result = managementDAO.insertConfirmedVacation(confirmedvacation);
		return result;
	}

	@Override
	public int insertStartTime(TimeSheet timesheet) {
		int result = managementDAO.insertStartTime(timesheet);
		return result;
	}

	@Override
	public int insertEndTime(String resultTimeSheetCode) {
		
		int result = managementDAO.insertEndTime(resultTimeSheetCode);
		return result;
	}

	@Override
	public int adjustTimeRecord(String time_sheet_code) {
		int result = managementDAO.adjustTimeRecord(time_sheet_code);
		return result;
	}

	@Override
	public String findTimeSheetCode(String employee_code) {
		String result = managementDAO.findTimeSheetCode(employee_code);
		return result;
	}
}
