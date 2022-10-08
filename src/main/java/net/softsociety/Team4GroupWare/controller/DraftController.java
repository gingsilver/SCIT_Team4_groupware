package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
import net.softsociety.Team4GroupWare.domain.Draft;
import net.softsociety.Team4GroupWare.domain.DraftApprover;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.service.AdminService;
import net.softsociety.Team4GroupWare.service.DraftService;

@Slf4j
@Controller
@RequestMapping("draft")
public class DraftController {
	// 드래프트 서비스 선언
	@Autowired
	DraftService draftservice;
	// 어드민 서비스 선언
	@Autowired
	AdminService adminservice;
	
	// 전자결재 메인 페이지(페이지 입장)
	@GetMapping("main")
	public String draftmain(@AuthenticationPrincipal UserDetails user, Model model) {
		// 사원 정보 가져오기
		Employee admin = adminservice.readAdmin(user.getUsername());
		
		/* start 양식함 출력 model단 */
		DocumentForm doc = new DocumentForm();
		// 회사 코드와 사원 코드 정보를 통해 내가 작성한 양식 리스트 가져오기
		doc.setCompany_code(admin.getCompany_code());
		doc.setDocument_form_writer_code(admin.getEmployee_code());
		ArrayList<DocumentForm> docform = draftservice.selectAllDoc(doc);
		/* end 양식함 출력 model단 */
		
		// 로그인한 사람의 사원 코드, 양식함 리스트, 로그인 한 사람 정보를 view단에 가져가기
		model.addAttribute("loginCode", admin.getEmployee_code());
		model.addAttribute("docform", docform);
		model.addAttribute("employee", admin);
		
		return "draftView/draftmain";
	}
	
	// 전자결재 - 기안작성 페이지(페이지 입장)
	@GetMapping("write")
	public String writeDdraft(@AuthenticationPrincipal UserDetails user, Model model) {
		// 회사코드, 로그인 한 사원 내용 가져오기
		Employee employee = draftservice.readEmployee(user.getUsername());
		Company company = adminservice.readCompany(employee.getCompany_code());
				
		model.addAttribute("employee", employee);
		model.addAttribute("company", company);
		
		return "draftView/writedraft";
	}
	
	// 전자결재 - 기안읽기 페이지(페이지 입장)
	@GetMapping({"read"})
	public String read(
			@RequestParam(name="draft_code", defaultValue = "0") String draft_code
			, Model model
			, @AuthenticationPrincipal UserDetails user) {
		// 로그인 한 사원 정보 가지고 오기
		Employee admin = adminservice.readAdmin(user.getUsername());
		
		// 주소와 함께 가지고 온 기안코드 확인
		log.debug("게시글 번호 : {}", draft_code);
		// 양식코드에 맞는 양식 가져오기
		Draft draft = draftservice.readDraft(draft_code);
		
		// 기안 코드와 맞는 글이 없을 때는 메인 페이지로 이동
		if(draft.equals(null)) {
			return "redirect:/draft/main";
		}
		
		// 기안 코드와 맞는 글이 있을 때 결과를 모델에 담아서 html에서 출력
		model.addAttribute("draft_code", draft_code);
		model.addAttribute("draft", draft);
		model.addAttribute("employee", admin);
		
		return "draftView/readdraft";
	}
	
	// 전자결재 - 양식작성 페이지(페이지 입장)
	@GetMapping("writedoc")
	public String writedoc(@AuthenticationPrincipal UserDetails user, Model model) {
		// 회사코드, 로그인 한 사원 내용 가져오기
		Employee employee = draftservice.readEmployee(user.getUsername());
		Company company = adminservice.readCompany(employee.getCompany_code());
						
		model.addAttribute("employee", employee);
		model.addAttribute("company", company);
		
		return "draftView/writedoc";
	}
	
	// 전자결재 - 양식 목록 페이지(페이지 입장)
	@GetMapping("readDoc")
	public String readDoc(
			@RequestParam(name="document_form_code", defaultValue = "0") String document_form_code
			, Model model
			, @AuthenticationPrincipal UserDetails user) {
		// 로그인 한 사원 정보 가져오기
		Employee employee = draftservice.readEmployee(user.getUsername());
		
		// 주소와 함께 가지고 온 양식코드 확인
		log.debug("글 번호 : {}", document_form_code);
		// 양식코드에 맞는 양식 가져오기
		DocumentForm docform = adminservice.findDocByCode(document_form_code);
		
		// 양식 코드와 맞는 글이 없을 때는 메인 페이지로 이동
		if(docform.equals(null)) {
			return "redirect:/admin/adminDraft";
		}
		
		// 양식 코드와 맞는 글이 있을 때 결과를 모델에 담아서 html에서 출력
		model.addAttribute("docform", docform);
		model.addAttribute("employee", employee);
		
		return "draftView/readDoc";
	}
}
