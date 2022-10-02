package net.softsociety.Team4GroupWare.service;

import net.softsociety.Team4GroupWare.domain.Salary;
import net.softsociety.Team4GroupWare.domain.Tax;

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

}
