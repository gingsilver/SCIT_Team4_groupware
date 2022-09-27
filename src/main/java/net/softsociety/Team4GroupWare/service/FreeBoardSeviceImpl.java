package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.FreeBoardDAO;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.FreeBoard;
import net.softsociety.Team4GroupWare.domain.FreeReply;
import net.softsociety.Team4GroupWare.util.PageNavigator;



@Transactional
@Service
@Slf4j
public class FreeBoardSeviceImpl implements FreeBoardService {

    @Autowired
    private FreeBoardDAO boardDAO;


	@Override
	public PageNavigator getPageNavigator(
			int pagePerGroup, int countPerPage, int page, String type, String searchWord, String company_code) {
		
		HashMap<String, String> map = new HashMap<>();
		map.put("type", type);
		map.put("searchWord", searchWord);
		map.put("company_code", company_code);
		
		int total = boardDAO.countFreeBoard(map);
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, total);
		
		return navi;
	}

	@Override
	public ArrayList<FreeBoard> list(PageNavigator navi, String type, String searchWord, String company_code) {
		
		HashMap<String, String> map =new HashMap<>();
		map.put("type", type);
		map.put("searchWord", searchWord);
		map.put("company_code", company_code);
		
		RowBounds rb = new RowBounds(navi.getStartRecord(), navi.getCountPerPage());
		ArrayList<FreeBoard> boardlist = boardDAO.selectFreeBoardList(map, rb); 
		
		return boardlist;
	}



	@Override
	public int write(FreeBoard board) {
		int result = boardDAO.insertFreeBoard(board);
		return result;
	}

	@Override
	public FreeBoard read(String free_code) {
		int result = boardDAO.updateHits(free_code);
		FreeBoard board = boardDAO.selectFreeBoard(free_code);
		return board;
	}
	
	@Override
	public int delete(FreeBoard board) {
		int result = boardDAO.deleteFreeBoard(board);
		return result;
	}

	@Override
	public int update(FreeBoard board) {
		int result = boardDAO.updateFreeBoard(board);
		return result;
	}

	@Override
	public int writeReply(FreeReply reply) {
		int result = boardDAO.insertReply(reply);
		return result;
	}

	@Override
	public ArrayList<FreeReply> listReply(String free_code) {
		ArrayList<FreeReply> replylist = boardDAO.selectReplyList(free_code);
		return replylist;
	}

	@Override
	public int deleteReply(FreeReply reply) {
		int result = boardDAO.deleteReply(reply);
		return result;
	}

	@Override
	public Employee findEmployee(String employee_id) {
		Employee result = boardDAO.findEmployee(employee_id);
		return result;
	}

	
}
