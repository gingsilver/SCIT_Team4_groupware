package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import net.softsociety.Team4GroupWare.domain.ConfirmedVacation;
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


	/**
	 * 휴무 신청 후 남은 연차갯수 갱신
	 * @param vacation_annual_minus 차감된 연차 갯수
	 * @param string 
	 */
	public int updateMinusVacation(int vacation_annual_minus, String employee_code);

	/**
	 * 휴무 신청서 누적 데이터(히스토리)
	 * @param confirmedvacation
	 * @return 성공 여부(성공 1, 실패 0)
	 */
	public int insertConfirmedVacation(ConfirmedVacation confirmedvacation);

	/**
	 * 출근 시간 입력하기
	 * @param timesheet(회사번호, 사원번호, 출퇴근기록 번호, 출근시간)
	 * @return 성공 여부(성공 1, 실패 0)
	 */
	public int insertStartTime(TimeSheet timesheet);

	/**
	 * 퇴근 시간 입력하기
	 * @param time_sheet_code
	 * @return 성공 여부(성공 1, 실패 0)
	 */
	public int insertEndTime(String resultTimeSheetCode);

	/**
	 * 시간 정정 신청 여부
	 * @param time_sheet_code
	 * @return 성공 여부(성공 1, 실패 0)
	 */
	public int adjustTimeRecord(String time_sheet_code);

	/**
	 * Time_sheet_code 찾아오기
	 * @param employee_code 사원번호
	 * @return 성공 여부(성공 1, 실패 0)
	 */
	public String findTimeSheetCode(String employee_code);


	


}
