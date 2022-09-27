package net.softsociety.Team4GroupWare.service;

import net.softsociety.Team4GroupWare.domain.Employee;

public interface DraftService {

	/**
	 * select : 로그인 한 멤버 객체 가져오기 => 사실 없어도 됨
	 * @param username
	 * @return
	 */
	public Employee readEmployee(String username);

	/**
	 * insert : 예비 기안 코드 번호 생성 메소드
	 * @return
	 */
	public int addDraftSeq();

}
