package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import net.softsociety.Team4GroupWare.domain.Salary;
import net.softsociety.Team4GroupWare.domain.Tax;
import net.softsociety.Team4GroupWare.domain.TimeSheet;
import net.softsociety.Team4GroupWare.domain.Vacation;

public interface ManagementService {

	/**
	 * 개인의 연봉정보 불러오기
	 * @param company_code 사원 번호
	 * @return Salary 연봉정보
	 */
	public Salary seleteSalaryOne(String employee_code);

	/**
	 * 개인의 올해 세금정보 불러오기
	 * @param employee_code 사원 번호
	 * @param tax_year 해당 년도 
	 * @return 세금정보
	 */
	public Tax selectTaxInfo(String employee_code, String tax_year);

	/**
	 * 개인의 정해져있는 출퇴근 정시 정보 불러오기
	 * @param employee_code
	 * @param time_sheet_code 근로계약서에서 설정한 출퇴근시간
	 * @return 출퇴근 정시정보(2019.09.12의 형태)
	 */
	public TimeSheet selectTimesheetOne(String employee_code, String time_sheet_code);
	
	/**
	 * 개인의 정해져있는 출퇴근 정시 정보 불러오기
	 * @param employee_code
	 * @return 풀퇴근 정시정보(9:00, 18:00의 형태)
	 */

	public TimeSheet selectTimesheetOnlyTime(String employee_code);

	/**
	 * 개인의 한 달간의 출퇴근 기록 불러오기
	 * @param employee_code
	 * @return ArrayList<TimeSheet>
	 */
	public ArrayList<TimeSheet> selectTimesheetOneMonth(String employee_code);

	/**
	 * 개인의 남은 연차정보 불러오기
	 * @param employee_code
	 * @return Vacation 개인의 연차정보
	 */
	public Vacation getVacationdays(String employee_code);


}
