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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.AdminBoard;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Organization;
import net.softsociety.Team4GroupWare.service.AdminService;
import net.softsociety.Team4GroupWare.util.FileService;
import oracle.jdbc.proxy.annotation.Post;

@Slf4j
@Controller
@RequestMapping("admin")
@ResponseBody
public class AdminRestController {

	// 서비스 선언
	@Autowired
	AdminService service;
	
	// 기안 첨부파일 저장 위치 경로
	@Value("${uploadpath.board}")
	String uploadPhotoPath;

	// 신규 회사/조직원 정보(신규 조직원 등록) - 관리자/사원 아이디 중복 검사
	@PostMapping({ "checkid" })
	public int checkid(String employee_id) {
		log.debug("가져온 id : {}", employee_id);
		int result = 0;

		result = service.checkID(employee_id);

		return result;
	}

	// 신규 회사/조직원 정보(신규 조직원 등록) - 관리자/사원 이메일 중복 검사
	@PostMapping({ "checkemail" })
	public int checkemail(String employee_email) {
		log.debug("가져온 id : {}", employee_email);
		int result = 0;

		result = service.checkEmail(employee_email);

		return result;
	}

	// 조직 관리 - 조직도 관리 - 조직도 : 조직도 불러오기
	@PostMapping("readOrg")
	public JSONArray readOrg(@AuthenticationPrincipal UserDetails user) {
		// 회사코드, 관리자 내용 가져오기
		Employee admin = service.readAdmin(user.getUsername());
		Company company = service.readCompany(admin.getCompany_code());
		
		// 회사 내용을 토대로 조직도를 JsonArray에 담아서 가져오기
		JSONArray json = service.readOrg(company);

		return json;
	}

	// 조직 관리 - 조직도 관리 - 사원 목록 : 조직도 내 사원 목록 불러오기
	@PostMapping("searchEmployee")
	public ArrayList<Employee> searchEmployee(@AuthenticationPrincipal UserDetails user, String organization) {
		// 회사코드, 관리자 내용 가져오기
		Employee employee = service.readAdmin(user.getUsername());
		Company company = service.readCompany(employee.getCompany_code());

		// 부서명에서 회사이름 제외하기
		log.debug("가져온 원래 팀명 : {}", organization);
		String realOrg = organization.substring(company.getCompany_name().length() + 2);
		log.debug("변경된 팀명 : {}", realOrg);

		// 회사이름 제외된 부서명을 관리자의 정보에 담기
		employee.setOrganization(realOrg);
		
		// 로그인 한 관리자의 회사 코드, 부서명을 통해 사원 부서명에 맞는 사원 리스트 가져오기
		ArrayList<Employee> empList = service.findByOrganization(employee);

		return empList;
	}
	
	// 조직 관리 - 조직도 관리 - 조직생성 : 조직도 내 조직생성에 부서명 출력, 신규 부서 생성
	// ★코드 수정 필요 : 현재는 부서만 등록 가능, 팀과 사업부도 추가할 수 있게 변경 필요
	@PostMapping("addOrganization")
	public int addOrganization(Organization org, @AuthenticationPrincipal UserDetails user) {
		log.debug("가져온 조직 : {}", org);
		// 1. 컴퍼니 아이디 가져와서 저장
		Employee employee = service.readAdmin(user.getUsername());
		org.setCompany_code(employee.getCompany_code());

		// 2. 부모 아이디에 가지고 있는 부서 아이디의 끝번호를 가져와서.. 저장? 그리고 그 번호 +1해서 org에 부서 아이디로 저장
		ArrayList<Organization> orgList = service.findByParentId(org.getParent_id());
		String department_node = null;
		for (int i = 0; i < orgList.size(); i++) {
			department_node = orgList.get(i).getDepartment_id();
		}
		org.setDepartment_id(department_node);

		// 3. 부서 내용 저장
		int result = service.addOrganization(org);

		return result;
	}

	// 조직 관리 - 조직도 관리 - 사원추가 : 조직도에서 선택한 조직에 사원추가
	@PostMapping("addEmpForOrg")
	public int addEmpForOrg(Employee employee) {
		// 선택된 사원 내용 확인
		log.debug("가져온 멤버 : {}", employee);

		// 사원의 내용을 배열에 저장
		ArrayList<Employee> empList = new ArrayList<>();
		empList.add(employee);
		log.debug("가져온 배열 : {}", empList);
		
		// 배열 가져가서 선택한 조직에 사원 내용 추가
		int result = service.addEmpForOrg(employee);

		return result;
	}

	// 전자결재 - 양식함 - 양식 작성 : 사용자가 입력한 양식 내용 저장
	@PostMapping("addDocForm")
	public int addDocForm(@AuthenticationPrincipal UserDetails user, DocumentForm docform) {
		// 로그인 한 관리자의 내용 가져오기
		Employee admin = service.readAdmin(user.getUsername());
		
		// 가져온 양식 내용에 회사코드, 작성자 코드/아이디를 저장
		docform.setCompany_code(admin.getCompany_code());
		docform.setDocument_form_writer_code(admin.getEmployee_code());
		docform.setDocument_form_writer_name(admin.getEmployee_name());
		log.debug("가져온 폼 내용 : {}", docform);

		// 가져온 양식 내용을 저장
		int result = service.addDocumentForm(docform);

		return result;
	}
	
	// 게시판 저장, 수정, 삭제 페이지 및 DB 변경 페이지 → 미구현
	@PostMapping("saveBoard")
	public String saveBoard(@AuthenticationPrincipal UserDetails user, AdminBoard adminboard) {
		log.debug("게시판 내용 : {}", adminboard);
		
		// 로그인 한 관리자의 내용 가져오기
		Employee admin = service.readAdmin(user.getUsername());
		adminboard.setCompany_code(admin.getCompany_code());
		adminboard.setWriter_code(admin.getEmployee_code());
		
		service.addAdminBoard(adminboard);
		
		return adminboard.getAdmin_board_code();
	}
	
	// 전자결재 - 기안 작성 - 파일 업로드 : 다중 파일 업로드
	@PostMapping("uploadFile")
	public void uploadFile(@AuthenticationPrincipal UserDetails user, AttachedFile file,
							MultipartFile[] upload) {
		// 회사코드, 관리자 내용 가져오기
		Employee admin = service.readAdmin(user.getUsername());
		log.debug("가져온 파일 : {}, 가져온 코드 : {}", upload.length, file.getDocument_code());
			
		// 파일 객체에 회사코드, 기안코드, 사원 코드/아이디/이름 입력하기
		file.setCompany_code(admin.getCompany_code());
		file.setEmployee_code(admin.getEmployee_code());
		file.setEmployee_id(admin.getEmployee_id());
		file.setEmployee_name(admin.getEmployee_name());
			
		// 반복문으로 모든 모든 파일 저장
		for(MultipartFile multipartFile : upload) {
			log.debug("----------------------------------");
			log.debug("upload file name : " + multipartFile.getOriginalFilename());
			log.debug("upload file size : " + multipartFile.getSize());
				
			if (multipartFile != null && !multipartFile.isEmpty() && multipartFile.getSize() != 0) {
				try {
					String absoluteUploadPath = new ClassPathResource(uploadPhotoPath).getFile().getAbsolutePath();
					String savedfile = FileService.saveFile(multipartFile, absoluteUploadPath);
					log.debug("absoluteUploadPath : {}", absoluteUploadPath);

					// 확장자 추출
					String originalFilename = multipartFile.getOriginalFilename();
					int lastIndex = originalFilename.lastIndexOf('.');
					String ext = lastIndex == -1 ? "" : originalFilename.substring(lastIndex);

					// 추출한 확장자, 원래 파일 이름, 저장된 파일 이름을 파일객체에 입력
					file.setAttached_file_ext(ext);
					file.setAttached_file_origin_name(originalFilename);
					file.setAttached_file_save_name(savedfile);
						
					// 입력한 파일 객체를 파일 테이블에 저장
					int result = service.addDraftAttFile(file);
					log.debug("파일 데이터 : {}", file);
				} catch (IOException e) {
					log.debug(e.getMessage());
				}
			}
		}
	}
		
	@PostMapping("selectBoardType")
	public ArrayList<AdminBoard> selectBoardType(String admin_board_type, @AuthenticationPrincipal UserDetails user){
		Employee admin = service.readAdmin(user.getUsername());
		
		log.debug("가져온 데이터 : {}", admin_board_type);
		
		AdminBoard board = new AdminBoard();
		board.setAdmin_board_type(admin_board_type);
		board.setCompany_code(admin.getCompany_code());
		
		ArrayList<AdminBoard> boardList = service.findByType(board);
		
		return boardList;
	}
}
