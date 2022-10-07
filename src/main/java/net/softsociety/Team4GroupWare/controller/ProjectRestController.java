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
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.ProjectPart;
import net.softsociety.Team4GroupWare.service.AdminService;
import net.softsociety.Team4GroupWare.service.EmployeeService;
import net.softsociety.Team4GroupWare.service.ProjectService;

@Slf4j
@Controller
@RequestMapping("project")
@ResponseBody
public class ProjectRestController {
    // 서비스 선언
    @Autowired
    AdminService service;

    @Autowired
    ProjectService pj_service;

    @Autowired
    EmployeeService em_service;

    // 조직도 불러오는 ajax 컨트롤러
    @PostMapping("readOrg")
    public JSONArray readOrg(@AuthenticationPrincipal UserDetails user) {
        // 회사코드, 관리자 내용 가져오기
        Employee admin = service.readAdmin(user.getUsername());
        Company company = service.readCompany(admin.getCompany_code());

        JSONArray json = service.readOrg(company);

        return json;
    }

    // 조직도 내 사원 목록 불러오는 ajax 컨트롤러
    @PostMapping("searchEmployee")
    public ArrayList<Employee> searchEmployee(@AuthenticationPrincipal UserDetails user, String organization) {
        Employee employee = service.readAdmin(user.getUsername());
        Company company = service.readCompany(employee.getCompany_code());

        String realOrg = organization.substring(company.getCompany_name().length() + 2);

        log.debug("가져온 팀명 : {}", realOrg);

        employee.setOrganization(realOrg);

        ArrayList<Employee> empList = service.findByOrganization(employee);

        return empList;
    }

    @PostMapping("addPart")
    public int addPart(@AuthenticationPrincipal UserDetails user, String pj_code, String employee_id,
            String pj_part_content, String employee_name, ProjectPart projectPart) {

        projectPart.setPj_code(pj_code);
        projectPart.setEmployee_id(employee_id);
        projectPart.setPj_part_content(pj_part_content);
        projectPart.setEmployee_name(employee_name);

        System.out.println(projectPart);
        System.out.println(pj_code + "코드" + employee_id + "아이디" + pj_part_content + "내용");
        int result = pj_service.insertPart(projectPart);

        return result;
    }
    // 조직도를 통해
    // 프로젝트 멤버 추가
    // @PostMapping("addMember")
    // public Employee addMember(String employee_id) {

    // Employee employee = em_service.getEmployeeById(employee_id);

    // return null;

    // }

    // 조직도를 통해 프로젝트 멤버 추가
    // @PostMapping("addMember")
    // public Employee addMember(Project Project, List<String> pj_member_name,
    // List<String> pj_member_department) {

    // }
}
