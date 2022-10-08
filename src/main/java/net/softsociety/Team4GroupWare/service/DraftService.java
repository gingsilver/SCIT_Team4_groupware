package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
import net.softsociety.Team4GroupWare.domain.Draft;
import net.softsociety.Team4GroupWare.domain.DraftApprover;
import net.softsociety.Team4GroupWare.domain.DraftBox;
import net.softsociety.Team4GroupWare.domain.DraftOpnion;
import net.softsociety.Team4GroupWare.domain.Employee;

public interface DraftService {

	/**
	 * select : 로그인 한 멤버 객체 가져오기 => 사실 없어도 됨
	 * @param username
	 * @return
	 */
	public Employee readEmployee(String username);

	/**
	 * insert : create 예비 기안 시퀀스 
	 * @return
	 */
	public String createCode();

	/**
	 * select : 기안 코드에 맞는 결재선 턴 코드 총합 리턴
	 * @param draft_code
	 * @return
	 */
	public String countDraftCode(String draft_code);

	/**
	 * select : 타입에 따른 양식 가져오기
	 * @param doc
	 * @return
	 */
	public ArrayList<DocumentForm> selectByType(DocumentForm doc);

	/**
	 * select : 클릭한 양식의 내용을 가져오기
	 * @param document_form_code
	 * @return
	 */
	public DocumentForm readDocumentForm(String document_form_code);

	/**
	 * select : 개인 양식 리스트 가져오기
	 * @param doc
	 * @return
	 */
	public ArrayList<DocumentForm> selectAllDoc(DocumentForm doc);

	/**
	 * insert : 기안 내 첨부파일 insert
	 * @param file
	 * @return
	 */
	public int addDraftAttFile(AttachedFile file);

	/**
	 * select : 기안 내 모든 첨부파일 select
	 * @param draft_code 
	 * @return
	 */
	public ArrayList<AttachedFile> selectAllDraftFile(String draft_code);

	/**
	 * insert : 기안에 의견 추가
	 * @param opinion
	 * @return
	 */
	public int addOpinion(DraftOpnion opinion);

	/**
	 * select : 기안 코드에 맞는 모든 의견
	 * @param draft_code
	 * @return
	 */
	public ArrayList<DraftOpnion> selectAllOpinion(String draft_code);

	/**
	 * insert : 기안 자체 추가
	 * @param draft
	 * @return
	 */
	public int addDraft(Draft draft);

	/**
	 * select : 모든 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	public ArrayList<DraftBox> selectAllDraft(DraftBox draftbox);

	/**
	 * insert : 모든 결재자 추가하기
	 * @param draftApprover
	 * @return
	 */
	public int addDraftApprover(DraftApprover draftApprover);

	/**
	 * select : 결재자 리스트 가져오기
	 * @param draft_code
	 * @return
	 */
	public Draft readDraft(String draft_code);

	/**
	 * select : 기안의 결재선을 전부 가져오기
	 * @param draft_code
	 * @return
	 */
	public ArrayList<DraftApprover> selectAllDraftApprover(String draft_code);

	/**
	 * select : 기안 내 모든 첨부파일 가져오기
	 * @param draft_code
	 * @return
	 */
	public ArrayList<AttachedFile> selectAllAttachedFile(String document_code);

	/**
	 * select : 지정 전결자 가져오기
	 * @param document_form_code
	 * @return
	 */
	public String findAppByCode(String document_form_code);

	/**
	 * select : 결재 대기중인 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	public ArrayList<DraftBox> findWaitingDraft(DraftBox draftbox);

	/**
	 * select : 결재 진행중인 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	public ArrayList<DraftBox> findProgressDraft(DraftBox draftbox);

	/**
	 * select : 결재 완료된 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	public ArrayList<DraftBox> findCompleteDraft(DraftBox draftbox);

	/**
	 * select : 결재 완료된 기안 가져오기2
	 * @param draftbox
	 * @return
	 */
	public ArrayList<DraftBox> findCompleteDraft2(DraftBox draftbox);

	/**
	 * select : 결재 반려된 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	public ArrayList<DraftBox> findRejectDraft(DraftBox draftbox);

	/**
	 * select : 결재 참조된 기안 가져오기
	 * @param draftbox
	 * @return
	 */
	public ArrayList<DraftBox> findReferenceDraft(DraftBox draftbox);
	
}
