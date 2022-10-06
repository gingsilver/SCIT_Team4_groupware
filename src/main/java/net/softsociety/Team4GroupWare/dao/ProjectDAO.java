package net.softsociety.Team4GroupWare.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Organization;
import net.softsociety.Team4GroupWare.domain.Project;
import net.softsociety.Team4GroupWare.domain.ProjectMember;
import net.softsociety.Team4GroupWare.domain.ProjectPart;

@Mapper
public interface ProjectDAO {

    // 프로젝트 저장
    public int insertPj(Project pj);

    // 프로젝트 멤버 저장
    public int insertPj_member(List<ProjectMember> members);

    // 프로젝트 파트 저장
    public int insertPj_part(ProjectPart pj_part);

    // 프로젝트 리스트 불러오기
    public List<Project> selectProjectList(String employee_id);

    public Project findProject(String pj_code);

    public List<ProjectMember> selectPj_member(String pj_code);

    public Employee getEmployeeById(String employee_code);

    // 로그인 유저의 정보 불러오기
    public Employee readEmployee(String username);

    // 로그인 유저 회사 불러오기
    public Company readCompany(String company_code);

    public ArrayList<Organization> readOrg(Company company);

    public ArrayList<Employee> findByOrganization(Employee employee);

    public ArrayList<Employee> employeeList(Employee employee);

    public ArrayList<Organization> findByParentId(String parent_id);

    public int addOrganization(Organization org);

}
