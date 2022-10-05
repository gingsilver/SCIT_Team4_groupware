package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.DraftDAO;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
import net.softsociety.Team4GroupWare.domain.Draft;
import net.softsociety.Team4GroupWare.domain.DraftApprover;
import net.softsociety.Team4GroupWare.domain.DraftOpnion;
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

	@Override
	public int addDraftAttFile(AttachedFile file) {
		int result = dao.addDraftAttFile(file);
		
		return result;
	}

	@Override
	public ArrayList<AttachedFile> selectAllDraftFile(String draft_code) {
		ArrayList<AttachedFile> attFileList = dao.selectAllDraftFile(draft_code);
		
		return attFileList;
	}

	@Override
	public int addOpinion(DraftOpnion opinion) {
		int result = dao.addOpinion(opinion);
		
		return result;
	}

	@Override
	public ArrayList<DraftOpnion> selectAllOpinion(String draft_code) {
		ArrayList<DraftOpnion> opList = dao.selectAllOpinion(draft_code);
		
		return opList;
	}

	@Override
	public int addDraft(Draft draft) {
		int result = dao.addDraft(draft);
		
		return result;
	}

	@Override
	public ArrayList<Draft> selectAllDraft(Draft selectDF) {
		ArrayList<Draft> draft = dao.selectAllDraft(selectDF);
		
		return draft;
	}

	@Override
	public int addDraftApprover(DraftApprover draftApprover) {
		int result = dao.addDraftApprover(draftApprover);
		
		return result;
	}

	@Override
	public Draft readDraft(String draft_code) {
		Draft draft = dao.readDraft(draft_code);
		
		return draft;
	}

	@Override
	public ArrayList<DraftApprover> selectAllDraftApprover(String draft_code) {
		ArrayList<DraftApprover> appList = dao.selectAllDraftApprover(draft_code);
		
		return appList;
	}

}
