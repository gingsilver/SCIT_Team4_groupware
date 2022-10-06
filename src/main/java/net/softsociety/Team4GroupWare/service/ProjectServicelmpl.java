package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.ProjectDAO;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Organization;
import net.softsociety.Team4GroupWare.domain.Project;
import net.softsociety.Team4GroupWare.domain.ProjectMember;

@Transactional
@Service
@Slf4j
public class ProjectServicelmpl implements ProjectService {

    @Autowired
    private ProjectDAO projectDAO;

    @Override
    @Transactional
    public int insertProject(Project pj, List<ProjectMember> members) {
        int result = projectDAO.insertPj(pj);

        projectDAO.insertPj_member(members);

        return result;
    }

    @Override
    public List<Project> projectList(String employee_id) {
        return projectDAO.selectProjectList(employee_id);
    }

    @Override
    public Project projectFind(String pj_code) {
        return projectDAO.findProject(pj_code);
    }

    @Override
    public Employee getEmployeeById(String employee_code) {
        return projectDAO.getEmployeeById(employee_code);
    }

    @Override
    public List<ProjectMember> selectPj_member(String pj_code) {
        return projectDAO.selectPj_member(pj_code);
    }

    @Override
    public Employee readEmployee(String username) {
        Employee employee = projectDAO.readEmployee(username);
        return employee;
    }

    @Override
    public Company readCompany(String company_code) {
        Company company = projectDAO.readCompany(company_code);
        return company;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JSONArray readOrg(Company company) {
        ArrayList<Organization> org = projectDAO.readOrg(company);

        JSONArray jArray = new JSONArray();// 배열이 필요할때
        for (int i = 0; i < org.size(); i++)// 배열
        {
            JSONObject sObject = new JSONObject();// 배열 내에 들어갈 json
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

    @Override
    public ArrayList<Employee> findByOrganization(Employee employee) {
        ArrayList<Employee> empList = projectDAO.findByOrganization(employee);
        return empList;
    }

    @Override
    public ArrayList<Employee> employeeList(Employee employee) {
        ArrayList<Employee> empList = projectDAO.employeeList(employee);
        return empList;
    }

    @Override
    public ArrayList<Organization> findByParentId(String parent_id) {
        ArrayList<Organization> orgList = projectDAO.findByParentId(parent_id);

        return orgList;
    }

    @Override
    public int addOrganization(Organization org) {
        int result = projectDAO.addOrganization(org);

        return result;
    }

}
