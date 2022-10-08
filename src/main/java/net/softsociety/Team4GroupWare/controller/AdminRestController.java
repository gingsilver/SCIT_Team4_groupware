package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.AdminBoard;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Organization;
import net.softsociety.Team4GroupWare.service.AdminService;
import oracle.jdbc.proxy.annotation.Post;

@Slf4j
@Controller
@RequestMapping("admin")
@ResponseBody
public class AdminRestController {

	// 서비스 선언
	@Autowired
	AdminService service;

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
	
}
