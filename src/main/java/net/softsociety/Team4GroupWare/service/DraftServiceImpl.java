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
import net.softsociety.Team4GroupWare.domain.DraftBox;
import net.softsociety.Team4GroupWare.domain.DraftOpnion;
import net.softsociety.Team4GroupWare.domain.Employee;

@Service
@Slf4j
public class DraftServiceImpl implements DraftService {
	
	@Autowired
	DraftDAO dao;
	
	/**
	 * select : 로그인 한 멤버 객체 가져오기 => 사실 없어도 됨
	 * @param username
	 * @return
	 */
	@Override
	public Employee readEmployee(String username) {
		Employee employee = dao.readEmployee(username);
		
		log.debug("사원 : {}", employee);
		
		return employee;
	}

	/**
	 * insert : create 예비 기안 시퀀스 
	 * @return
	 */
	@Override
	public String createCode() {
		String draft_code = dao.createCode();
		return draft_code;
	}

	/**
	 * select : 기안 코드에 맞는 결재선 턴 코드 총합 리턴
	 * @param draft_code
	 * @return
	 */
	@Override
	public String countDraftCode(String draft_code) {
		String process_turn_code = dao.countDraftCode(draft_code);
		
		return process_turn_code;
	}

	/**
	 * select : 타입에 따른 양식 가져오기
	 * @param doc
	 * @return
	 */
	@Override
	public ArrayList<DocumentForm> selectByType(DocumentForm doc) {
		ArrayList<DocumentForm> docList = dao.selectByType(doc);
		
		return docList;
	}

	/**
	 * select : 클릭한 양식의 내용을 가져오기
	 * @param document_form_code
	 * @return
	 */
	@Override
	public DocumentForm readDocumentForm(String document_form_code) {
		DocumentForm doc = dao.readDocumentForm(document_form_code);
		
		return doc;
	}

	/**
	 * select : 개인 양식 리스트 가져오기
	 * @param doc
	 * @return
	 */
	@Override
	public ArrayList<DocumentForm> selectAllDoc(DocumentForm doc) {
		ArrayList<DocumentForm> docform = dao.selectAllDoc(doc);
		
		return docform;
	}

	/**
	 * insert : 기안 내 첨부파일 insert
	 * @param file
	 * @return
	 */
	@Override
	public int addDraftAttFile(AttachedFile file) {
		int result = dao.addDraftAttFile(file);
		
		return result;
	}

	/**
	 * select : 기안 내 모든 첨부파일 select
	 * @param draft_code 
	 * @return
	 */
	@Override
	public ArrayList<AttachedFile> selectAllDraftFile(String draft_code) {
		ArrayList<AttachedFile> attFileList = dao.selectAllDraftFile(draft_code);
		
		return attFileList;
	}

	/**
	 * insert : 기안에 의견 추가
	 * @param opinion
	 * @return
	 */
	@Override
	public int addOpinion(DraftOpnion opinion) {
		int result = dao.addOpinion(opinion);
		
		return result;
	}

	/**
	 * select : 기안 코드에 맞는 모든 의견
	 * @param draft_code
	 * @return
	 */
	@Override
	public ArrayList<DraftOpnion> selectAllOpinion(String draft_code) {
		ArrayList<DraftOpnion> opList = dao.selectAllOpinion(draft_code);
		
		return opList;
	}

	/**
	 * insert : 기안 자체 추가
	 * @param draft
	 * @return
	 */
	@Override
	public int addDraft(Draft draft) {
		int result = dao.addDraft(draft);
		
		return result;
	}

	/**
	 * select : 모든 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	@Override
	public ArrayList<DraftBox> selectAllDraft(DraftBox draftbox) {
		ArrayList<DraftBox> requestArr = dao.selectAllDraft(draftbox);
				
		return requestArr;
	}

	/**
	 * insert : 모든 결재자 추가하기
	 * @param draftApprover
	 * @return
	 */
	@Override
	public int addDraftApprover(DraftApprover draftApprover) {
		int result = dao.addDraftApprover(draftApprover);
		
		return result;
	}

	/**
	 * select : 결재자 리스트 가져오기
	 * @param draft_code
	 * @return
	 */
	@Override
	public Draft readDraft(String draft_code) {
		Draft draft = dao.readDraft(draft_code);
		
		return draft;
	}

	/**
	 * select : 기안의 결재선을 전부 가져오기
	 * @param draft_code
	 * @return
	 */
	@Override
	public ArrayList<DraftApprover> selectAllDraftApprover(String draft_code) {
		ArrayList<DraftApprover> appList = dao.selectAllDraftApprover(draft_code);
		
		return appList;
	}

	/**
	 * select : 기안 내 모든 첨부파일 가져오기
	 * @param draft_code
	 * @return
	 */
	@Override
	public ArrayList<AttachedFile> selectAllAttachedFile(String document_code) {
		ArrayList<AttachedFile> attList = dao.selectAllAttachedFile(document_code);
		 
		return attList;
	}

	/**
	 * select : 지정 전결자 가져오기
	 * @param document_form_code
	 * @return
	 */
	@Override
	public String findAppByCode(String document_form_code) {
		String finalApprover = dao.findAppByCode(document_form_code);
		
		return finalApprover;
	}

	/**
	 * select : 결재 대기중인 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	@Override
	public ArrayList<DraftBox> findWaitingDraft(DraftBox draftbox) {
		ArrayList<DraftBox> waitingArr = dao.findWaitingDraft(draftbox);
		
		return waitingArr;
	}

	/**
	 * select : 결재 진행중인 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	@Override
	public ArrayList<DraftBox> findProgressDraft(DraftBox draftbox) {
		ArrayList<DraftBox> progressgArr = dao.findProgressDraft(draftbox);
		
		return progressgArr;
	}

	/**
	 * select : 결재 완료된 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	@Override
	public ArrayList<DraftBox> findCompleteDraft(DraftBox draftbox) {
		ArrayList<DraftBox> completegArr = dao.findCompleteDraft(draftbox);
			
		return completegArr;
	}
	
	/**
	 * select : 결재 완료된 기안 가져오기2
	 * @param draftbox
	 * @return
	 */
	@Override
	public ArrayList<DraftBox> findCompleteDraft2(DraftBox draftbox) {
		ArrayList<DraftBox> completegArr2 = dao.findCompleteDraft(draftbox);
			
		return completegArr2;
	}

	/**
	 * select : 결재 반려된 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	@Override
	public ArrayList<DraftBox> findRejectDraft(DraftBox draftbox) {
		ArrayList<DraftBox> rejectArr = dao.findRejectDraft(draftbox);
			
		return rejectArr;
	}

	/**
	 * select : 결재 참조된 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	@Override
	public ArrayList<DraftBox> findReferenceDraft(DraftBox draftbox) {
		ArrayList<DraftBox> referenceArr = dao.findReferenceDraft(draftbox);
				
		return referenceArr;
	}

}
