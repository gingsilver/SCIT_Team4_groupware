package net.softsociety.Team4GroupWare.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.RowBounds;

import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.FreeBoard;
import net.softsociety.Team4GroupWare.domain.FreeReply;



/**
 * 게시판 관련 매퍼 인터페이스
 */
@Mapper
public interface FreeBoardDAO {
	//회원의 모든 정보를 불러온다(사원코드와 회사코드를 호출목적)
	public Employee findEmployee(String employee_id);
    //글 저장
	public int insertFreeBoard(FreeBoard FreeBoard);
	//글 읽기
	public FreeBoard selectFreeBoard(String free_code);
	//조회수 증가
	public int updateHits(String free_code);
	//전체 글 개수
	public int countFreeBoard(HashMap<String, String> map);
	//현재 페이지 글 목록
	public ArrayList<FreeBoard> selectFreeBoardList(HashMap<String, String> map, RowBounds rb);
	//글 삭제
	public int deleteFreeBoard(FreeBoard FreeBoard);
	//글 수정
	public int updateFreeBoard(FreeBoard FreeBoard);
	//댓글저장
	public int insertReply(FreeReply FreeReply);
    //댓글 목록 조회
    public ArrayList<FreeReply> selectReplyList(String free_code);
    //댓글 삭제
    public int deleteReply(FreeReply reply);
}
