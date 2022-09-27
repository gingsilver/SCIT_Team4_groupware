package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Employee;

/** 
 * 회원정보 관련 서비스
 */
public interface EmployeeService {
	//회원정보 저장
	public int insertEmployee(Employee employee);
	//회원의 아이디조회(1명)
	public String findId(Employee employee);
	//회원의 비밀번호 재설정
	public int renewPw(Employee employee);
	
	/**
	 * select : 회원의 권한과 아이디를 확인 하여 체크한 페이지로 이동할 수 있는지 확인
	 * @param employee : role_name, employee_id
	 * @return 존재하는 사원 수
	 */
	public int checkRole(Employee employee);
	
	//회원의 모든 정보를 불러온다(사원코드와 사원번호를 호출목적)
	public Employee getEmployeeById(String employee_id);
	//첨부파일 테이블에 사원 프로필사진 업로드
	public int insertPhoto(AttachedFile file);
	//사원 프로필사진 수정
	public int updatePhoto(Employee employee);
	//사원 개인정보 업데이트
	public int updateinfo(Employee employee);
	
}
