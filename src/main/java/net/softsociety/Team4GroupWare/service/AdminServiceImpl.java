package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.AdminDAO;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Organization;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminDAO dao;
	
	//비밀번호 단방향 암호화
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public int checkID(String employee_id) {
		int result = dao.checkID(employee_id);
		
		return result;
	}

	@Override
	public int checkEmail(String employee_email) {
		int result = dao.checkEmail(employee_email);
		
		return result;
	}

	@Override
	public int addEmployee(Employee employee) {
		String encodedPassword = passwordEncoder.encode(employee.getEmployee_pwd());
		log.debug("암호화된 비번 {} : {}", employee.getEmployee_pwd(), encodedPassword);
		employee.setEmployee_pwd(encodedPassword);
		
		int result = dao.addEmployee(employee);
		return result;
	}

	@Override
	public Employee readAdmin(String username) {
		Employee admin = dao.readAdmin(username);
		
		return admin;
	}

	@Override
	public ArrayList<Employee> employeeList(Employee employee) {
		ArrayList<Employee> empList = dao.employeeList(employee);
		
		return empList;
	}

	@Override
	public Company readCompany(String company_code) {
		Company company = dao.readCompany(company_code);
		
		return company;
	}

	@Override
	public int updateCompany(Company company) {
		int result = dao.updateCompany(company);
		
		return result;
	}

	@Override
	public int insertLogo(AttachedFile file) {
		int result = dao.insertLogo(file);
		
		return result;
	}

	@Override
	public int updateLogo(Company company) {
		int result = dao.updateLogo(company);
		
		return result;
	}

	@Override
	public int updateAdmin(Employee employee) {
		if(employee.getEmployee_pwd() != null && employee.getEmployee_pwd().length() != 0) {
			String encodedPassword = passwordEncoder.encode(employee.getEmployee_pwd());
			employee.setEmployee_pwd(encodedPassword);
		}
		
		int result = dao.updateAdmin(employee);
		
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONArray readOrg(Company company) {
		ArrayList<Organization> org = dao.readOrg(company);
		
		JSONArray jArray = new JSONArray();//배열이 필요할때
        for (int i = 0; i < org.size(); i++)//배열
        {
            JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
            sObject.put("id", org.get(i).getDepartment_id());
            sObject.put("pId", org.get(i).getParent_id());
            sObject.put("name", org.get(i).getDepartment_name());
            sObject.put("open", org.get(i).getOpen());
            sObject.put("path", org.get(i).getPATH());
            sObject.put("level", org.get(i).getLEVEL());
            jArray.add(sObject);
        }
        
		return jArray;
	}

	@Override
	public ArrayList<Employee> findByOrganization(Employee employee) {
		ArrayList<Employee> empList = dao.findByOrganization(employee);
		
		return empList;
	}

	@Override
	public ArrayList<Organization> findByParentId(String parent_id) {
		ArrayList<Organization> orgList = dao.findByParentId(parent_id);
		
		return orgList;
	}

	@Override
	public int addOrganization(Organization org) {
		int result = dao.addOrganization(org);
		
		return result;
	}

	@Override
	public int addEmpForOrg(Employee employee) {
		int result = dao.addEmpForOrg(employee);
		
		return result;	
	}

}
