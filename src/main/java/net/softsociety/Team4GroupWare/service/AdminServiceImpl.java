package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.AdminDAO;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Organization;

@Slf4j
@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	AdminDAO dao;
	
	//비밀번호 단방향 암호화
	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * select : 아이디 중복 체크
	 * 
	 * @param employee_id
	 * @return
	 */
	@Override
	public int checkID(String employee_id) {
		int result = dao.checkID(employee_id);
		
		return result;
	}

	/**
	 * select : 이메일 중복 체크
	 * 
	 * @param employee_email
	 * @return
	 */
	@Override
	public int checkEmail(String employee_email) {
		int result = dao.checkEmail(employee_email);
		
		return result;
	}

	/**
	 * insert : 사원 추가
	 * 
	 * @param employee
	 * @return
	 */
	@Override
	public int addEmployee(Employee employee) {
		// 비밀번호 암호화 작업
		String encodedPassword = passwordEncoder.encode(employee.getEmployee_pwd());
		log.debug("암호화된 비번 {} : {}", employee.getEmployee_pwd(), encodedPassword);
		// 암호화 된 비밀번호로 변경
		employee.setEmployee_pwd(encodedPassword);
		
		int result = dao.addEmployee(employee);
		return result;
	}
	
	/**
	 * select : 관리자가 관리하고 있는 회사 코드 찾기
	 * 
	 * @param username
	 * @return
	 */
	@Override
	public Employee readAdmin(String username) {
		Employee admin = dao.readAdmin(username);
		
		return admin;
	}

	/**
	 * select : 전체 사원 불러오기
	 * 
	 * @param admin
	 * @return
	 */
	@Override
	public ArrayList<Employee> employeeList(Employee employee) {
		ArrayList<Employee> empList = dao.employeeList(employee);
		
		return empList;
	}

	/**
	 * select : 로그인한 관리자의 회사를 불러오기
	 * 
	 * @param company_code
	 * @return
	 */
	@Override
	public Company readCompany(String company_code) {
		Company company = dao.readCompany(company_code);
		
		return company;
	}

	/**
	 * update : 회사 내용 수정
	 * 
	 * @param company
	 * @return
	 */
	@Override
	public int updateCompany(Company company) {
		int result = dao.updateCompany(company);
		
		return result;
	}
	
	/**
	 * insert : 첨부파일 테이블에 회사 로고 업로드
	 * 
	 * @param file
	 * @return
	 */
	@Override
	public int insertLogo(AttachedFile file) {
		int result = dao.insertLogo(file);
		
		return result;
	}

	/**
	 * update : 회사 로고 수정
	 * 
	 * @param company
	 * @return
	 */
	@Override
	public int updateLogo(Company company) {
		int result = dao.updateLogo(company);
		
		return result;
	}

	/**
	 * update : 관리자 정보 수정
	 * 
	 * @param employee
	 * @return
	 */
	@Override
	public int updateAdmin(Employee employee) {
		// 비밀번호 암호화 작업 : 비밀번호칸에 입력된 데이터가 null일 시 변경되지 않음
		if(employee.getEmployee_pwd() != null && employee.getEmployee_pwd().length() != 0) {
			String encodedPassword = passwordEncoder.encode(employee.getEmployee_pwd());
			// 암호화 된 비밀번호로 변경
			employee.setEmployee_pwd(encodedPassword);
		}
		
		int result = dao.updateAdmin(employee);
		
		return result;
	}

	/**
	 * select : 조직도 불러오기
	 * 
	 * @param company_code
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JSONArray readOrg(Company company) {
		// 조직도 내용 가져오기
		ArrayList<Organization> org = dao.readOrg(company);
		
		// 가져온 조직도 내용 jsonarray에 넣기
		JSONArray jArray = new JSONArray();//배열이 필요할때
		
		// jsonarray에 넣을 jsonobject를 만들고 가지고 온 조직도 내용을 넣기
        for (int i = 0; i < org.size(); i++)//배열
        {
        	//배열 내에 들어갈 jsonobject
            JSONObject sObject = new JSONObject();
            
            //object에 가지고 온 조직도 내용 넣기
            sObject.put("id", org.get(i).getDepartment_id());
            sObject.put("pId", org.get(i).getParent_id());
            sObject.put("name", org.get(i).getDepartment_name());
            sObject.put("open", org.get(i).getOpen());
            sObject.put("path", org.get(i).getPATH());
            sObject.put("level", org.get(i).getLEVEL());
            jArray.add(sObject);
        }
        
		return jArray;
	}

	/**
	 * select : 조직도에 맞는 사원 불러오기
	 * 
	 * @param employee
	 * @return
	 */
	@Override
	public ArrayList<Employee> findByOrganization(Employee employee) {
		ArrayList<Employee> empList = dao.findByOrganization(employee);
		
		return empList;
	}

	/**
	 * select : 부모아이디에 들어있는 자식(=부서) 아이디 내용 불러오기
	 * @param parent_id
	 * @return
	 */
	@Override
	public ArrayList<Organization> findByParentId(String parent_id) {
		ArrayList<Organization> orgList = dao.findByParentId(parent_id);
		
		return orgList;
	}

	/**
	 * insert : 새로운 조직 생성
	 * @param org
	 * @return
	 */
	@Override
	public int addOrganization(Organization org) {
		int result = dao.addOrganization(org);
		
		return result;
	}

	/**
	 * update : 멤버에 조직 추가
	 * @param employee
	 * @return
	 */
	@Override
	public int addEmpForOrg(Employee employee) {
		int result = dao.addEmpForOrg(employee);
		
		return result;	
	}

	/**
	 * insert : 양식함에 양식 추가
	 * @param docform
	 * @return
	 */
	@Override
	public int addDocumentForm(DocumentForm docform) {
		int result = dao.addDocumentForm(docform);
		
		return result;
	}

	/**
	 * select : 양식함 내용 가져오기
	 * @param doc
	 * @return
	 */
	@Override
	public ArrayList<DocumentForm> readDocumentForm(DocumentForm doc) {
		ArrayList<DocumentForm> docform = dao.readDocumentForm(doc);
		
		return docform;
	}
	
	/**
	 * select : 양식함 글 읽기
	 * @param document_form_code
	 * @return
	 */
	@Override
	public DocumentForm findDocByCode(String document_form_code) {
		DocumentForm docform = dao.findDocByCode(document_form_code);
		
		return docform;
	}

}
