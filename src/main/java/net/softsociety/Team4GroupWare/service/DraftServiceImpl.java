package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.DraftDAO;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
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

	@Override
	public DraftApprover readApprover(DraftApprover approver) {
		DraftApprover approver2 = dao.readApprover(approver);
		
		return approver2;
	}

	@Override
	public ArrayList<DocumentForm> selectByType(DocumentForm doc) {
		ArrayList<DocumentForm> docList = dao.selectByType(doc);
		
		return docList;
	}

	@Override
	public DocumentForm readDocumentForm(String document_form_code) {
		DocumentForm doc = dao.readDocumentForm(document_form_code);
		
		return doc;
	}

	@Override
	public ArrayList<DocumentForm> selectAllDoc(DocumentForm doc) {
		ArrayList<DocumentForm> docform = dao.selectAllDoc(doc);
		
		return docform;
	}

}
