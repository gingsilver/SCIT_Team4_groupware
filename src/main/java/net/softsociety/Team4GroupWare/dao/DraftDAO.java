package net.softsociety.Team4GroupWare.dao;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.Team4GroupWare.domain.DraftApproval;
import net.softsociety.Team4GroupWare.domain.Employee;

@Mapper
public interface DraftDAO {

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
	public int addApproval(DraftApproval approval);

	/**
	 * create : 예비 기안 시퀀스 
	 * @return
	 */
	public String createCode();

	/**
	 * selecct : 기안 코드에 맞는 결재선 턴 코드 총합 리턴
	 * @param draft_code
	 * @return
	 */
	public String countDraftCode(String draft_code);

}
