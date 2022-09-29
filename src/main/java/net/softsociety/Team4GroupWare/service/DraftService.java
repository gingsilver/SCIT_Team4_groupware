package net.softsociety.Team4GroupWare.service;

import net.softsociety.Team4GroupWare.domain.DraftApprover;
import net.softsociety.Team4GroupWare.domain.Employee;

public interface DraftService {

	/**
	 * select : 로그인 한 멤버 객체 가져오기 => 사실 없어도 됨
	 * @param username
	 * @return
	 */
	public Employee readEmployee(String username);

	/**
	 * insert : 결재선 추가
	 * @param approval
	 * @return
	 */
	public int addApprover(DraftApprover approver);

	/**
	 * create : 예비 기안 시퀀스 
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
	 * select : 가져온 데이터에 맞는 결재자 찾기
	 * @param approver
	 * @return
	 */
	public DraftApprover readApprover(DraftApprover approver);

}
