package net.softsociety.Team4GroupWare.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.EmailDAO;
import net.softsociety.Team4GroupWare.dao.ManagementDAO;
import net.softsociety.Team4GroupWare.domain.Salary;
import net.softsociety.Team4GroupWare.domain.Tax;

@Slf4j
@Service
public class ManagementServiceImpl implements ManagementService {

	@Autowired
	ManagementDAO managementDAO;

	@Override
	public Salary seleteSalaryOne(String employee_code) {
		Salary salary = managementDAO.seleteSalaryOne(employee_code);
		return salary;
	}

	@Override
	public Tax selectTaxInfo(String employee_code, String tax_year) {
		HashMap<String, String> map = new HashMap<>();
		
		map.put("employee_code", employee_code);
		map.put("tax_year", tax_year);
		
		
		Tax tax = managementDAO.selectTaxInfo(map);
		return tax;
	}
}
