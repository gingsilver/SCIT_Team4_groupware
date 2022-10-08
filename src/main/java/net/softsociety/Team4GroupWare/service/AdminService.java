package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import org.json.simple.JSONArray;

import net.softsociety.Team4GroupWare.domain.AdminBoard;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Organization;

public interface AdminService {

	/**
	 * select : 아이디 중복 체크
	 * 
	 * @param employee_id
	 * @return
	 */
	public int checkID(String employee_id);

	/**
	 * select : 이메일 중복 체크
	 * 
	 * @param employee_email
	 * @return
	 */
	public int checkEmail(String employee_email);

	/**
	 * insert : 사원 추가
	 * 
	 * @param employee
	 * @return
	 */
	public int addEmployee(Employee employee);

	/**
	 * select : 관리자가 관리하고 있는 회사 코드 찾기
	 * 
	 * @param username
	 * @return
	 */
	public Employee readAdmin(String username);
	
	/**
	 * select : 전체 사원 불러오기
	 * 
	 * @param admin
	 * @return
	 */
	public ArrayList<Employee> employeeList(Employee employee);

	/**
	 * select : 로그인한 관리자의 회사를 불러오기
	 * 
	 * @param company_code
	 * @return
	 */
	public Company readCompany(String company_code);

	/**
	 * update : 회사 내용 수정
	 * 
	 * @param company
	 * @return
	 */
	public int updateCompany(Company company);

	/**
	 * insert : 첨부파일 테이블에 회사 로고 업로드
	 * 
	 * @param file
	 * @return
	 */
	public int insertLogo(AttachedFile file);

	/**
	 * update : 회사 로고 수정
	 * 
	 * @param company
	 * @return
	 */
	public int updateLogo(Company company);

	/**
	 * update : 관리자 정보 수정
	 * 
	 * @param employee
	 * @return
	 */
	public int updateAdmin(Employee employee);

	/**
	 * select : 조직도 불러오기
	 * 
	 * @param company_code
	 * @return
	 */
	public JSONArray readOrg(Company company);

	/**
	 * select : 조직도에 맞는 사원 불러오기
	 * 
	 * @param employee
	 * @return
	 */
	public ArrayList<Employee> findByOrganization(Employee employee);

	/**
	 * select : 부모아이디에 들어있는 자식(=부서) 아이디 내용 불러오기
	 * @param parent_id
	 * @return
	 */
	public ArrayList<Organization> findByParentId(String parent_id);

	/**
	 * insert : 새로운 조직 생성
	 * @param org
	 * @return
	 */
	public int addOrganization(Organization org);

	/**
	 * update : 멤버에 조직 추가
	 * @param employee
	 * @return
	 */
	public int addEmpForOrg(Employee employee);

	/**
	 * insert : 양식함에 양식 추가
	 * @param docform
	 * @return
	 */
	public int addDocumentForm(DocumentForm docform);

	/**
	 * select : 양식함 내용 가져오기
	 * @param doc
	 * @return
	 */
	public ArrayList<DocumentForm> readDocumentForm(DocumentForm doc);
	
	/**
	 * select : 양식함 글 읽기
	 * @param document_form_code
	 * @return
	 */
	public DocumentForm findDocByCode(String document_form_code);

}
