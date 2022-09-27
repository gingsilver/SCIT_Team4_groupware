package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Organization;
import net.softsociety.Team4GroupWare.domain.Project;
import net.softsociety.Team4GroupWare.domain.ProjectMember;
import net.softsociety.Team4GroupWare.domain.ProjectPart;
import net.softsociety.Team4GroupWare.service.ProjectService;

@Slf4j
@RequestMapping("project")
@Controller
public class ProjectController {

    @Autowired
    ProjectService pj_service;

    // 프로젝트 메인 페이지 출력
    @GetMapping("main")
    public String main(Model model, @AuthenticationPrincipal UserDetails user) {
        System.out.println(user + "유저");
        // 프로젝트 진행도

        // 프로젝트 페이지
        // ArrayList<Project> projectList = pj_service.projectList(user.getUsername());

        // model.addAttribute("projectList", projectList);
        return "projectView/main";

    }

    // 프로젝트 작성 페이지 출력
    @GetMapping("create")
    public String create() {

        return "projectView/create";
    }

    // 작성된 프로젝트 등록
    @PostMapping("create")
    public String create(Project pj, ProjectMember pj_member, ProjectPart pj_part) {

        pj_service.insertProject(pj, pj_member, pj_part);
        return "redirect:/projectView/main";
    }

    // 프로젝트 팝업페이지 출력
    @GetMapping("popup")
    public String popup(@AuthenticationPrincipal UserDetails user, Model model) {
        // 회사코드, 관리자 내용 가져오기
        Employee employee = pj_service.readEmployee(user.getUsername());
        Company company = pj_service.readCompany(employee.getCompany_code());
        JSONArray json = pj_service.readOrg(company);

        ArrayList<Employee> empList = pj_service.employeeList(employee);

        for (int i = 0; i < empList.size(); i++) {
            if (empList.get(i).getRole_name().equals("ROLE_ADMIN")) {
                empList.remove(i);
            }
        }
        model.addAttribute("employee", employee);
        model.addAttribute("company ", company);
        model.addAttribute("json", json);
        return "projectView/popup";
    }

    // 조직도 불러오는 ajax 컨트롤러
    @PostMapping("readOrg")
    public JSONArray readOrg(@AuthenticationPrincipal UserDetails user) {

        // 회사코드, 관리자 내용 가져오기
        Employee employee = pj_service.readEmployee(user.getUsername());
        Company company = pj_service.readCompany(employee.getCompany_code());

        JSONArray json = pj_service.readOrg(company);

        return json;
    }

    // 조직도 내 사원 목록 불러오는 ajax 컨트롤러
    @PostMapping("searchEmployee")
    public ArrayList<Employee> searchEmployee(@AuthenticationPrincipal UserDetails user, String organization) {

        Employee employee = pj_service.readEmployee(user.getUsername());
        Company company = pj_service.readCompany(employee.getCompany_code());

        String realOrg = organization.substring(company.getCompany_name().length() + 2);

        log.debug("가져온 팀명 : {}", realOrg);

        employee.setOrganization(realOrg);

        ArrayList<Employee> empList = pj_service.findByOrganization(employee);

        return empList;
    }

    @PostMapping("addOrganization")
    public int addOrganization(Organization org, @AuthenticationPrincipal UserDetails user) {
        log.debug("가져온 조직 : {}", org);
        // 1. 컴퍼니 아이디 가져와서 저장
        Employee employee = pj_service.readEmployee(user.getUsername());
        org.setCompany_code(employee.getCompany_code());

        // 2. 부모 아이디에 가지고 있는 부서 아이디의 끝번호를 가져와서.. 저장? 그리고 그 번호 +1해서 org에 부서 아이디로 저장
        ArrayList<Organization> orgList = pj_service.findByParentId(org.getParent_id());

        String department_node = null;
        for (int i = 0; i < orgList.size(); i++) {
            department_node = orgList.get(i).getDepartment_id();
        }
        org.setDepartment_id(department_node);

        int result = pj_service.addOrganization(org);

        return result;
    }

    // 작성된 프로젝트 수정 (리더만 수정가능)

    // 프로젝트 메인페이지 나의 프로젝트 리스트 출력

    // @GetMapping("/create/member")
    // public String settingOrg(@AuthenticationPrincipal UserDetails user, Model
    // model) {
    // // 회사코드, 관리자 내용 가져오기
    // Employee admin = service.readAdmin(user.getUsername());
    // Company company = service.readCompany(admin.getCompany_code());
    // JSONArray json = service.readOrg(company);

    // model.addAttribute("admin", admin);
    // model.addAttribute("company", company);
    // model.addAttribute("json", json);

    // return "projectView/main";
    // }
    // 프로젝트 내용 읽기

    // 프로젝트 메인페이지 프로젝트 리스트내의
    // 프로젝트 메인페이지 파트 출력

    /*
     * 프로젝트 진행도 출력
     */
    /*
     * 나의 파트 진행도 출력
     * ex)나의 프로젝트 1,2,3,4중에
     * 1번 프로젝트의 파트 전체개수를 받고
     * 전체 갯수중에 파트 완성한 갯수만 받아서 완성갯수/전체갯수
     * step 으로 step전체갯수
     * step 갯수 된만큼 처리
     */

    // 프로젝트 작성페이지 조직도 출력

}
