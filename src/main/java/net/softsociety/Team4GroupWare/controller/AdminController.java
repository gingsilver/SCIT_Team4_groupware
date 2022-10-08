package net.softsociety.Team4GroupWare.controller;

import java.io.IOException;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.service.AdminService;
import net.softsociety.Team4GroupWare.util.FileService;

@Slf4j
@Controller
@RequestMapping("admin")
public class AdminController {
	// 어드민 서비스 선언
	@Autowired
	AdminService service;

	// 회사 로고 저장 위치 경로
	@Value("${uploadpath.admin}")
	String uploadPhotoPath;

	// 회사 관리 - 회사 정보 수정 페이지(페이지 입장)
	@GetMapping("companyInfo")
	public String companyInfo(@AuthenticationPrincipal UserDetails user, Model model) {
		// 관리자 내용 조회
		Employee admin = service.readAdmin(user.getUsername());
		// 관리자코드로 회사 내용 조회
		Company origincompany = service.readCompany(admin.getCompany_code());
		log.debug("가져온 데이터 : {}", origincompany);
		// view단으로 회사 내용 보내기
		model.addAttribute("company", origincompany);

		return "adminView/companyInfo";
	}

	// 회사 관리 - 회사 정보 수정 페이지(DB 내용 수정)
	@PostMapping("updateCompany")
	public String updateCompany(@AuthenticationPrincipal UserDetails user, Company company) {
		// 회사 코드 가져오기
		Employee admin = service.readAdmin(user.getUsername());
		// 가져온 데이터에 회사 코드 추가
		company.setCompany_code(admin.getCompany_code());
		log.debug("업데이트 데이터 : {}", company);
		// 회사내용 변경
		int result = service.updateCompany(company);

		return "redirect:/admin/companyInfo";
	}

	// 회사 관리 - 회사 로고 수정 페이지(DB 내용 수정)
	@PostMapping("updateLogo")
	public String updateLogo(@AuthenticationPrincipal UserDetails user, AttachedFile file, MultipartFile upload) {
		// 회사코드, 관리자 내용 가져오기
		Employee admin = service.readAdmin(user.getUsername());
		// 파일 객체에 회사코드, 관리자 코드, 관리자 아이디, 관리자 이름 추가
		file.setCompany_code(admin.getCompany_code());
		file.setEmployee_code(admin.getEmployee_code());
		file.setEmployee_id(admin.getEmployee_id());
		file.setEmployee_name(admin.getEmployee_name());
		
		// 파일 객체 업로드 하는 내용
		if (upload != null && !upload.isEmpty() && upload.getSize() != 0) {
			try {
				String absoluteUploadPath = new ClassPathResource(uploadPhotoPath).getFile().getAbsolutePath();
				String savedfile = FileService.saveFile(upload, absoluteUploadPath);
				log.debug("absoluteUploadPath : {}", absoluteUploadPath);

				// 확장자 추출
				String originalFilename = upload.getOriginalFilename();
				int lastIndex = originalFilename.lastIndexOf('.');
				String ext = lastIndex == -1 ? "" : originalFilename.substring(lastIndex);
				// 파일에 확장자, 기존 파일 이름, 생성된 파일 이름 저장
				file.setAttached_file_ext(ext);
				file.setAttached_file_origin_name(originalFilename);
				file.setAttached_file_save_name(savedfile);
				// 저장하기 전 파일 데이터 내용 확인
				log.debug("파일 데이터 : {}", file);
				
				// DB에 파일 데이터 저장
				int result = service.insertLogo(file);
				if (result == 1) {
					// 저장 성공시 해당 내용를 회사 로고 컬럼 변경하기
					Company company = new Company();
					company.setCompany_code(admin.getCompany_code());
					company.setCompany_logo(file.getAttached_file_save_name());
					// 회사의 로고 컬럼을 저장된 파일 이름으로 변경하기
					service.updateLogo(company);
				}
			} catch (IOException e) {
				log.debug(e.getMessage());
			}
		}
		return "redirect:/admin/companyInfo";
	}
	
	// 회사 관리 - 관리자 정보 수정 페이지(페이지 입장)
	@GetMapping("adminInfo")
	public String adminInfo(@AuthenticationPrincipal UserDetails user, Model model) {
		// 관리자 내용 조회
		Employee admin = service.readAdmin(user.getUsername());
		// view단으로 관리자 내용 보내기
		model.addAttribute("admin", admin);

		return "adminView/adminInfo";
	}

	// 회사 관리 - 관리자 정보 수정 페이지(DB 수정)
	@PostMapping("updateAdmin")
	public String updateAdmin(Employee employee) {
		// 사용자가 입력한 내용 확인
		log.debug("가져온 데이터 : {}", employee);
		// 관리자 정보 수정
		int result = service.updateAdmin(employee);

		return "redirect:/admin/adminInfo";
	}
	
	// 회사 관리 - 회사 일정 관리(페이지 입장)
	@GetMapping("companyCalendar")
	public String companyCalendar() {
		return "adminView/companyCalendar";
	}
	
	// 회사 관리 - 회사 일정 관리(DB 저장) → 미구현
	
	// 조직 관리 - 조직원 관리(페이지 입장)
	@GetMapping("employeeList")
	public String employeeList(@AuthenticationPrincipal UserDetails user, Model model) {
		// 관리자 정보 가져오기
		Employee employee = service.readAdmin(user.getUsername());
		// 관리자 정보중 회사 코드로 회사에 속해있는 사원 리스트 가져오기
		ArrayList<Employee> empList = service.employeeList(employee);
		
		// 사원 리스트 중 관리자 계정은 제외하고 출력하기
		for (int i = 0; i < empList.size(); i++) {
			if (empList.get(i).getRole_name().equals("ROLE_ADMIN")) {
				empList.remove(i);
			}
		}
		
		// 관리자 정보, 사원 리스트 view단에 가져가기
		model.addAttribute("admin", employee);
		model.addAttribute("empList", empList);

		return "adminView/employeeList";
	}
	
	// 조직 관리 - 조직원 관리 - 새로운 조직원 추가(DB 저장)
	@PostMapping("addEmployee")
	public String addEmployee(Employee employee) {
		log.debug("가져온 데이터 : {}", employee);
		employee.setEmployee_pwd("000000");

		int result = service.addEmployee(employee);

		return "redirect:/admin/employeeList";
	}

	// 조직 관리 - 조직원 정보 수정(페이지 입장)
	@GetMapping("updateEmployee")
	public String updateEmployee(@AuthenticationPrincipal UserDetails user, Model model) {
		// 관리자 정보 가져오기
		Employee employee = service.readAdmin(user.getUsername());
		// 관리자 정보중 회사 코드로 회사에 속해있는 사원 리스트 가져오기
		ArrayList<Employee> empList = service.employeeList(employee);
		
		// 사원 리스트 중 관리자 계정은 제외하고 출력하기
		for (int i = 0; i < empList.size(); i++) {
			if (empList.get(i).getRole_name().equals("ROLE_ADMIN")) {
				empList.remove(i);
			}
		}
		
		// 관리자 정보, 사원 리스트 view단에 가져가기
		model.addAttribute("admin", employee);
		model.addAttribute("empList", empList);

		return "adminView/updateEmployee";
	}
	
	// 조직 관리 - 조직원 정보 수정(DB 저장) → 미구현

	// 조직 관리 - 조직도 관리 페이지(페이지 입장) → 공용 조직도
	@GetMapping("settingOrg")
	public String settingOrg(@AuthenticationPrincipal UserDetails user, Model model) {
		// 관리자, 회사, 조직도 내용 가져오기
		Employee admin = service.readAdmin(user.getUsername());
		Company company = service.readCompany(admin.getCompany_code());
		// json객체(JsonObject로 만든 객체)들이 들어있는 Array
		JSONArray json = service.readOrg(company);
		
		// 관리자 정보중 회사 코드로 회사에 속해있는 사원 리스트 가져오기
		ArrayList<Employee> empList = service.employeeList(admin);
		// 사원 리스트 중 관리자 계정은 제외하고 출력하기
		for(int i = 0; i < empList.size(); i++) {
			if(empList.get(i).getRole_name().equals("ROLE_ADMIN")) {
				empList.remove(i);
			}
		}
		
		// 관리자 정보, 회사 정보, 조직도 정보, 사원 리스트 view단에 가져가기
		model.addAttribute("admin", admin);
		model.addAttribute("company", company);
		model.addAttribute("json", json);
		model.addAttribute("empList",empList);
		
		return "adminView/settingOrg";
	}

	// 전자결재 입장 페이지
	@GetMapping("adminDraft")
	public String adminDraft(@AuthenticationPrincipal UserDetails user, Model model) {
		Employee admin = service.readAdmin(user.getUsername());
		DocumentForm doc = new DocumentForm();
		doc.setCompany_code(admin.getCompany_code());
		doc.setDocument_form_writer_code(admin.getEmployee_code());
		
		ArrayList<DocumentForm> docform = service.readDocumentForm(doc);
		
		model.addAttribute("docform", docform);
		
		return "adminView/adminDraft";
	}
	
	// 전자결재 1개의 전자결재 읽기 → 미구현(but, 일반 회원 전자결재에서 가지고 오면 됨)
	
	// 전자결재 - 양식함 - 양식 작성 페이지(페이지 입장)
	@GetMapping("writedoc")
	public String writedoc(@AuthenticationPrincipal UserDetails user, Model model) {
		// 회사코드, 관리자 내용 가져오기
		Employee admin = service.readAdmin(user.getUsername());
		Company company = service.readCompany(admin.getCompany_code());
						
		model.addAttribute("employee", admin);
		model.addAttribute("company", company);
		
		return "adminView/writedoc";
	}
	
	// 전자결재 - 양식함 - 1개의 양식 읽기(페이지 입장)
	@GetMapping("readDoc")
	public String readDoc(
			@RequestParam(name="document_form_code", defaultValue = "0") String document_form_code
			, Model model
			, @AuthenticationPrincipal UserDetails user) {
		// 주소와 함께 가지고 온 양식코드 확인
		log.debug("글 번호 : {}", document_form_code);
		// 양식코드에 맞는 양식 가져오기
		DocumentForm docform = service.findDocByCode(document_form_code);
		
		// 양식 코드와 맞는 글이 없을 때는 메인 페이지로 이동
		if(docform.equals(null)) {
			return "redirect:/admin/adminDraft"; 
		}
		
		// 양식 코드와 맞는 글이 있을 때 결과를 모델에 담아서 html에서 출력
		model.addAttribute("docform", docform);
		
		return "adminView/readDoc";
	}
	
	// 게시판 페이지(페이지 입장)
	@GetMapping("adminBoard")
	public String adminBoard() {

		return "adminView/adminBoard";
	}
	
	// 게시판 저장, 수정, 삭제 페이지 및 DB 변경 페이지 → 미구현
	
}
