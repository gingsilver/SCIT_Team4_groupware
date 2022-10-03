package net.softsociety.Team4GroupWare.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.Team4GroupWare.domain.Salary;
import net.softsociety.Team4GroupWare.domain.Tax;
import net.softsociety.Team4GroupWare.domain.TimeSheet;
import net.softsociety.Team4GroupWare.domain.Vacation;

@Mapper
public interface ManagementDAO {
	//개인의 연봉정보 불러오기
	Salary seleteSalaryOne(String employee_code);

	//개인의 세금정보 불러오기
	Tax selectTaxInfo(HashMap<String, String> map);

	//개인의 정시 출퇴근 시간 불러오기(2019.09.12의 형태)
	TimeSheet selectTimesheetOne(HashMap<String, String> map);
	
	//개인의 정시 출퇴근 시간 불러오기(9:00, 18:00의 형태)
	TimeSheet selectTimesheetOnlyTime(String employee_code);

	//개인의 한 달간 출퇴근 기록 불러오기
	ArrayList<TimeSheet> selectTimesheetOneMonth(String employee_code);

	//개인의 연차 사용 정보 불러오기
	Vacation getVacationdays(String employee_code);

}
