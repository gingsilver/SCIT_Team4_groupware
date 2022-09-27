package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.FreeBoard;
import net.softsociety.Team4GroupWare.domain.FreeReply;
import net.softsociety.Team4GroupWare.util.PageNavigator;



public interface FreeBoardService {
	//회원의 모든 정보를 불러온다(사원코드와 회사코드를 호출목적)
	public Employee findEmployee(String employee_id);
	//글 저장
	public int write(FreeBoard board);
//	//글 읽기
	public FreeBoard read(String free_code);
	//페이징 정보
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String type, String searchWord, String company_code);
	//현재 페이지 글 목록
	public ArrayList<FreeBoard> list(PageNavigator navi, String type, String searchWord, String company_code);
	//글 삭제
	public int delete(FreeBoard board);
	//글 수정
	public int update(FreeBoard board);
	//리플 목록조회
	public ArrayList<FreeReply> listReply(String free_code);
	//리플 저장
	public int writeReply(FreeReply reply);
	//리플 삭제
	public int deleteReply(FreeReply reply);

}
