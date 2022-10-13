package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.FreeBoard;
import net.softsociety.Team4GroupWare.domain.FreeReply;
import net.softsociety.Team4GroupWare.domain.LoginUser;
import net.softsociety.Team4GroupWare.service.FreeBoardService;
import net.softsociety.Team4GroupWare.util.PageNavigator;

/**
 * 게시판 관련 콘트롤러
 */
@Slf4j
@RequestMapping("freeboard")
@Controller
public class FreeBoardController {

	// 게시판 목록의 페이지당 글 수
	@Value("${user.board.page}")
	int countPerPage;

	// 게시판 목록의 페이지 이동 링크 수
	@Value("${user.board.group}")
	int pagePerGroup;

	@Autowired
	FreeBoardService service;

	/**
	 * 게시판 글목록으로 이동
	 * 
	 * @param page       현재 페이지
	 * @param type       검색 대상
	 * @param searchWord 검색어
	 */
	@GetMapping("list")
	public String list(Model model, @AuthenticationPrincipal UserDetails user,
			@RequestParam(name = "page", defaultValue = "1") int page, String type,
			String searchWord) {

		// log.debug("전달받은 company_code: {}", user.getCompany_code());

		// company_code = user.getCompany_code();

		Employee employee = service.findEmployee(user.getUsername());

		String company_code = employee.getCompany_code();

		PageNavigator navi = service.getPageNavigator(pagePerGroup, countPerPage, page, type, searchWord, company_code);

		ArrayList<FreeBoard> boardlist = service.list(navi, type, searchWord, company_code);

		model.addAttribute("employee", employee);
		model.addAttribute("navi", navi);
		log.debug("navi : {}", navi);
		model.addAttribute("boardlist", boardlist);
		model.addAttribute("type", type);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("company_code", company_code);

		return "/freeboard/list";
	}

	// 글쓰기폼으로 이동
	@GetMapping("write")
	public String write(@AuthenticationPrincipal UserDetails user,Model model) {
		Employee employee = service.findEmployee(user.getUsername());
		
		model.addAttribute("employee", employee);
		return "/freeboard/writeForm";
	}

	// 글쓰기 처리
	@PostMapping("write")
	public String write(FreeBoard board, @AuthenticationPrincipal UserDetails user,Model model) {

		log.debug("저장할 글정보 : {}", board);

		board.setEmployee_id(user.getUsername());

		Employee employee = service.findEmployee(user.getUsername());

		String company_code = employee.getCompany_code();

		board.setCompany_code(company_code);

		model.addAttribute("employee", employee);
		
		int result = service.write(board);
		return "redirect:/freeboard/list";
	}

	/**
	 * 글 읽기
	 * 
	 * @param boardnum 읽을 글번호
	 */
	@GetMapping("read")
	public String read(Model model, @RequestParam(name = "free_code", defaultValue = "fr0") String free_code, @AuthenticationPrincipal UserDetails user) {

		Employee employee = service.findEmployee(user.getUsername());
		
		FreeBoard board = service.read(free_code);
		if (board == null) {
			return "redirect:/freeboard/list"; // 글이 없으면 목록으로
		}

		// 현재 글에 달린 리플들
		ArrayList<FreeReply> replylist = service.listReply(free_code);

		// 결과를 모델에 담아서 HTML에서 출력
		model.addAttribute("board", board);
		model.addAttribute("replylist", replylist);
		model.addAttribute("employee", employee);

		return "/freeboard/read";
	}

	// 글 삭제
	@GetMapping("delete")
	public String delete(String free_code, @AuthenticationPrincipal UserDetails user) {

		// 해당 번호의 글 정보 조회
		FreeBoard board = service.read(free_code);

		if (board == null) {
			return "redirect:/freeboard/list";
		}

		// 로그인 아이디를 board객체에 저장
		board.setEmployee_id(user.getUsername());

		// 글 삭제
		int result = service.delete(board);

		return "redirect:/freeboard/list";
	}

	// 글 수정화면으로
	@GetMapping("update")
	public String update(String free_code, Model model) {

		FreeBoard board = service.read(free_code);
		model.addAttribute("board", board);

		return "/freeboard/updateForm";
	}

	// 글 수정처리
	@PostMapping("update")
	public String update(
			FreeBoard board, @AuthenticationPrincipal UserDetails user) {

		log.debug("저장할 글정보 : {}", board);

		// 작성자 아이디 추가
		board.setEmployee_id(user.getUsername());

		int result = service.update(board);

		return "redirect:/freeboard/read?free_code=" + board.getFree_code();
	}

	/**
	 * 리플 저장
	 * 
	 * @param reply 저장할 리플 정보
	 */
	@PostMapping("writeReply")
	public String writeReply(
			FreeReply reply, @AuthenticationPrincipal UserDetails user) {

		reply.setEmployee_id(user.getUsername());

		log.debug("저장할 리플 정보 : {}", reply);
		int result = service.writeReply(reply);

		return "redirect:/freeboard/read?free_code=" + reply.getFree_code();
	}

	/**
	 * 리플 삭제
	 * 
	 * @param reply 저장할 리플 정보
	 */
	@GetMapping("deleteReply")
	public String replyWrite(
			FreeReply reply, @AuthenticationPrincipal UserDetails user) {

		reply.setEmployee_id(user.getUsername());
		int result = service.deleteReply(reply);

		return "redirect:/freeboard/read?free_code=" + reply.getFree_code();
	}

}
