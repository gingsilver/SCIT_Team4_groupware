package net.softsociety.Team4GroupWare.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.Team4GroupWare.domain.ConfirmedVacation;
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

	//차감된 연차 갱신하기
	int updateMinusVacation(Map<String, Object> map);

	//휴무 신청서 등록
	int insertConfirmedVacation(ConfirmedVacation confirmedvacation);

	//출퇴근 관리 - 출근시간 입력
	int insertStartTime(TimeSheet timesheet);

	//출퇴근 관리 - 퇴근시간 입력
	int insertEndTime(String time_sheet_code);

	//시간 정정 신청 여부
	int adjustTimeRecord(String resultTimeSheetCode);

	//Time Sheet code 찾아오기
	String findTimeSheetCode(String employee_code);

}
