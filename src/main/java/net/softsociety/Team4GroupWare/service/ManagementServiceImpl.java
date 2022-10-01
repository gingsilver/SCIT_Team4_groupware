package net.softsociety.Team4GroupWare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.EmailDAO;
import net.softsociety.Team4GroupWare.dao.ManagementDAO;
import net.softsociety.Team4GroupWare.domain.Salary;

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
}
