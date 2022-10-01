package net.softsociety.Team4GroupWare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.EmployeeDAO;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Employee;


@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {
	 @Autowired
	    private EmployeeDAO employeeDAO;
	  @Autowired
	    private PasswordEncoder passwordEncoder;

	@Override
	public int insertEmployee(Employee employee) {
		String encodedPassword = passwordEncoder.encode(employee.getEmployee_pwd());
		log.debug("암호화된 비밀번호 {} : {}", employee.getEmployee_pwd(), encodedPassword);
		employee.setEmployee_pwd(encodedPassword);
		
		int result = employeeDAO.insertEmployee(employee);
		return result;
	}

	@Override
	public String findId(Employee employee) {
		String result = employeeDAO.findId(employee);
		return result;
	}

	@Override
	public int renewPw(Employee employee) {
		String encodedPassword = passwordEncoder.encode(employee.getEmployee_pwd());
		log.debug("암호화된 비밀번호 {} : {}", employee.getEmployee_pwd(), encodedPassword);
		employee.setEmployee_pwd(encodedPassword);
		
		int result = employeeDAO.renewPw(employee);
		return result;
	}

	@Override
	public int checkRole(Employee employee) {
		int result = employeeDAO.checkRole(employee);
		
		return result;
	}

	@Override
	public Employee getEmployeeById(String employee_id) {
		Employee result = employeeDAO.getEmployeeById(employee_id);
		return result;
	}

	@Override
	public int insertPhoto(AttachedFile file) {
		int result = employeeDAO.insertPhoto(file);
		return result;
	}

	@Override
	public int updatePhoto(Employee employee) {
		int result = employeeDAO.updatePhoto(employee);
		return result;
	}

	@Override
	public int updateinfo(Employee employee) {
		String encodedPassword = passwordEncoder.encode(employee.getEmployee_pwd());
		log.debug("암호화된 비밀번호 {} : {}", employee.getEmployee_pwd(), encodedPassword);
		employee.setEmployee_pwd(encodedPassword);
		
		int result = employeeDAO.updateinfo(employee);
		return result;
	}

	@Override
	public Employee getEmployeeByEmail(String email_sender) {
		Employee employee = employeeDAO.getEmployeeByEmail(email_sender);
		return employee;
	}

}
