package net.softsociety.Team4GroupWare.service;

import net.softsociety.Team4GroupWare.domain.Salary;

public interface ManagementService {

	/**
	 * 개인의 연봉정보 불러오기
	 * @param company_code
	 * @return Salary 연봉정보
	 */
	Salary seleteSalaryOne(String employee_code);

}
