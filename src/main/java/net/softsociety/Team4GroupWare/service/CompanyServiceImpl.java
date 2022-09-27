package net.softsociety.Team4GroupWare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.CompanyDAO;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;

@Slf4j
@Service
public class CompanyServiceImpl implements CompanyService {
	
	//dao
	@Autowired
	CompanyDAO dao;
	
	//비밀번호 단방향 암호화
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	//회사 생성
	@Override
	public int insertCompany(Company company) {
		int result = dao.insertCompany(company);		
		
		return result;
	}

	//회사 조회
	@Override
	public Company readCompany(String company_business_num) {
		Company company = dao.readCompany(company_business_num);
		
		return company;
	}

	//관리자 생성
	@Override
	public int insertAdmin(Employee employee) {
		String encodedPassword = passwordEncoder.encode(employee.getEmployee_pwd());
		log.debug("암호화된 비번 {} : {}", employee.getEmployee_pwd(), encodedPassword);
		employee.setEmployee_pwd(encodedPassword);
	
		int result = dao.insertAdmin(employee);		
		return result;
	}

	//관리자 아이디 중복 체크
	@Override
	public int checkID(String employee_id) {
		int result = dao.checkID(employee_id);
		
		return result;
	}

	//사업자 번호 중복 체크
	@Override
	public int checkBusinessNum(String company_business_num) {
		int result = dao.checkBusinessNum(company_business_num);
		
		return result;
	}

	//관리자 찾기
	@Override
	public Employee readAdmin(String company_code) {
		Employee employee = dao.readAdmin(company_code);
		
		return employee;
	}

}
