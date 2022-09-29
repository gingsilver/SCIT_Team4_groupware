package net.softsociety.Team4GroupWare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.DraftDAO;
import net.softsociety.Team4GroupWare.domain.DraftApprover;
import net.softsociety.Team4GroupWare.domain.Employee;

@Service
@Slf4j
public class DraftServiceImpl implements DraftService {
	
	@Autowired
	DraftDAO dao;
	
	@Override
	public Employee readEmployee(String username) {
		Employee employee = dao.readEmployee(username);
		
		log.debug("사원 : {}", employee);
		
		return employee;
	}

	@Override
	public int addApprover(DraftApprover approver) {
		int result = dao.addApprover(approver);
		
		return result;
	}

	@Override
	public String createCode() {
		String draft_code = dao.createCode();
		return draft_code;
	}

	@Override
	public String countDraftCode(String draft_code) {
		String process_turn_code = dao.countDraftCode(draft_code);
		
		return process_turn_code;
	}

}
