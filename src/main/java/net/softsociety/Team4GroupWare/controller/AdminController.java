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

	// 서비스 선언
	@Autowired
	AdminService service;

	// 사원 프로필 사진 저장 위치 경로
	@Value("${uploadpath.admin}")
	String uploadPhotoPath;

	// 회사 관리 - 회사 정보 수정 페이지
	@GetMapping("companyInfo")
	public String companyInfo(@AuthenticationPrincipal UserDetails user, Model model) {
		// 관리자 내용 조회
		Employee admin = service.readAdmin(user.getUsername());
		// 관리자코드로 회사 내용 조회
		Company origincompany = service.readCompany(admin.getCompany_code());
		log.debug("가져온 데이터 : {}", origincompany);

		// view단으로 컴퍼니 내용 보내기
		model.addAttribute("company", origincompany);

		return "adminView/companyInfo";
	}

	@PostMapping("updateCompany")
	public String updateCompany(@AuthenticationPrincipal UserDetails user, Company company) {
		// 회사 코드 가져오기
		Employee admin = service.readAdmin(user.getUsername());
		company.setCompany_code(admin.getCompany_code());

		log.debug("업데이트 데이터 : {}", company);

		int result = service.updateCompany(company);

		return "redirect:/admin/companyInfo";
	}

	@PostMapping("updateLogo")
	public String updateLogo(@AuthenticationPrincipal UserDetails user, AttachedFile file, MultipartFile upload) {

		// 회사코드, 관리자 내용 가져오기
		Employee admin = service.readAdmin(user.getUsername());

		file.setCompany_code(admin.getCompany_code());
		file.setEmployee_code(admin.getEmployee_code());
		file.setEmployee_id(admin.getEmployee_id());
		file.setEmployee_name(admin.getEmployee_name());

		if (upload != null && !upload.isEmpty() && upload.getSize() != 0) {
			try {
				String absoluteUploadPath = new ClassPathResource(uploadPhotoPath).getFile().getAbsolutePath();
				String savedfile = FileService.saveFile(upload, absoluteUploadPath);
				log.debug("absoluteUploadPath : {}", absoluteUploadPath);

				// 확장자 추출
				String originalFilename = upload.getOriginalFilename();
				int lastIndex = originalFilename.lastIndexOf('.');
				String ext = lastIndex == -1 ? "" : originalFilename.substring(lastIndex);

				file.setAttached_file_ext(ext);
				file.setAttached_file_origin_name(originalFilename);
				file.setAttached_file_save_name(savedfile);

				log.debug("파일 데이터 : {}", file);

				int result = service.insertLogo(file);
				if (result == 1) {
					Company company = new Company();
					company.setCompany_code(admin.getCompany_code());
					company.setCompany_logo(file.getAttached_file_save_name());

					service.updateLogo(company);
				}
			} catch (IOException e) {
				log.debug(e.getMessage());
			}
		}
		return "redirect:/admin/companyInfo";
	}

	@GetMapping("employeeList")
	public String employeeList(@AuthenticationPrincipal UserDetails user, Model model) {
		Employee employee = service.readAdmin(user.getUsername());

		ArrayList<Employee> empList = service.employeeList(employee);

		for (int i = 0; i < empList.size(); i++) {
			if (empList.get(i).getRole_name().equals("ROLE_ADMIN")) {
				empList.remove(i);
			}
		}

		model.addAttribute("admin", employee);
		model.addAttribute("empList", empList);

		return "adminView/employeeList";
	}

	@GetMapping("updateEmployee")
	public String updateEmployee(@AuthenticationPrincipal UserDetails user, Model model) {
		Employee employee = service.readAdmin(user.getUsername());

		ArrayList<Employee> empList = service.employeeList(employee);

		for (int i = 0; i < empList.size(); i++) {
			if (empList.get(i).getRole_name().equals("ROLE_ADMIN")) {
				empList.remove(i);
			}
		}

		model.addAttribute("admin", employee);
		model.addAttribute("empList", empList);

		return "adminView/updateEmployee";
	}

	// 조직도 관리 페이지 => 조직도 쓰실 분들은 참고
	@GetMapping("settingOrg")
	public String settingOrg(@AuthenticationPrincipal UserDetails user, Model model) {
		// 회사코드, 관리자 내용 가져오기
		Employee admin = service.readAdmin(user.getUsername());
		Company company = service.readCompany(admin.getCompany_code());
		JSONArray json = service.readOrg(company);
		
		ArrayList<Employee> empList = service.employeeList(admin);
		
		for(int i = 0; i < empList.size(); i++) {
			if(empList.get(i).getRole_name().equals("ROLE_ADMIN")) {
				empList.remove(i);
			}
		}
		
		model.addAttribute("admin", admin);
		model.addAttribute("company", company);
		model.addAttribute("json", json);
		model.addAttribute("empList",empList);
		
		return "adminView/settingOrg";
	}

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
	
	@GetMapping({"readDoc"})
	public String readDoc(
			@RequestParam(name="document_form_code", defaultValue = "0") String document_form_code
			, Model model
			, @AuthenticationPrincipal UserDetails user) {
		//db에서 글을 읽어서
		log.debug("글 번호 : {}", document_form_code);
		DocumentForm docform = service.findDocByCode(document_form_code);
		
		if(docform.equals(null)) {
			return "redirect:/admin/adminDraft"; //글이 없을때
		}
		
		//결과를 모델에 담아서 html에서 출력
		model.addAttribute("docform", docform);
		
		return "adminView/readDoc";
	}
	
	
	@GetMapping("writedoc")
	public String writedoc() {
		return "adminView/writedoc";
	}
	
	@GetMapping("adminBoard")
	public String adminBoard() {

		return "adminView/adminBoard";
	}

	@GetMapping("adminInfo")
	public String adminInfo(@AuthenticationPrincipal UserDetails user, Model model) {
		Employee admin = service.readAdmin(user.getUsername());

		model.addAttribute("admin", admin);

		return "adminView/adminInfo";
	}

	@PostMapping("updateAdmin")
	public String updateAdmin(Employee employee) {
		log.debug("가져온 데이터 : {}", employee);

		int result = service.updateAdmin(employee);

		return "redirect:/admin/adminInfo";
	}

	@PostMapping("addEmployee")
	public String addEmployee(Employee employee) {
		log.debug("가져온 데이터 : {}", employee);
		employee.setEmployee_pwd("000000");

		int result = service.addEmployee(employee);

		return "redirect:/admin/employeeList";
	}

	@GetMapping("companyCalendar")
	public String companyCalendar() {
		return "adminView/companyCalendar";
	}
}
