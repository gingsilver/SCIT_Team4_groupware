package net.softsociety.Team4GroupWare.service;

import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;

public interface CompanyService {

	/**
	 * insert : 회사 내용을 insert하기 위한 메소드
	 * @param company : 사용자가 입력한 company 내용
	 * @return insert 된 갯수
	 */
	public int insertCompany(Company company);

	/**
	 * select : 사업자번호로 회사 정보 select하기 위한 메소드
	 * @param company_business_num
	 * @return
	 */
	public Company readCompany(String company_business_num);

	/**
	 * insert : 관리자 insert하기 위한 메소드
	 * @param employee : 사용자가 입력한 관리자 내용
	 * @return insert 된 갯수
	 */
	public int insertAdmin(Employee employee);

	/**
	 * select : 아이디 중복 체크
	 * @param employee_id
	 * @return
	 */
	public int checkID(String employee_id);

	/**
	 * select : 사업자 번호 중복 체크
	 * @param company_business_num
	 * @return
	 */
	public int checkBusinessNum(String company_business_num);

	/**
	 * select : 회사 코드에 맞는 관리자 찾기
	 * @param company_code
	 * @return
	 */
	public Employee readAdmin(String company_code);

}
