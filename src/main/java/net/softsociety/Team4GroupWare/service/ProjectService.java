package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import org.json.simple.JSONArray;

import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Organization;
import net.softsociety.Team4GroupWare.domain.Project;
import net.softsociety.Team4GroupWare.domain.ProjectMember;
import net.softsociety.Team4GroupWare.domain.ProjectPart;

public interface ProjectService {

    // 프로젝트 생성(프로젝트내용, 멤버, 파트)
    public int insertProject(Project pj, ProjectMember pj_member, ProjectPart pj_part);

    public ArrayList<Project> projectList(String username);

    // 회사코드 찾기
    public Employee readEmployee(String username);

    // 전체사원 불러오기
    public ArrayList<Employee> employeeList(Employee employee);

    // 로그인 한 유저의 회사 불러오기
    public Company readCompany(String company_code);

    // 조직도 불러오기
    public JSONArray readOrg(Company company);

    // 조직도에 맞는 사원 불러오기
    public ArrayList<Employee> findByOrganization(Employee employee);

    public ArrayList<Organization> findByParentId(String parent_id);

    public int addOrganization(Organization org);

    // 프로젝트 리스트 출력

    // 프로젝트 멤버 출력

    // 프로젝트 파트 출력

}
