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
import net.softsociety.Team4GroupWare.domain.Project;
import net.softsociety.Team4GroupWare.domain.ProjectMember;
import net.softsociety.Team4GroupWare.domain.ProjectPart;
import net.softsociety.Team4GroupWare.service.AdminService;
import net.softsociety.Team4GroupWare.service.ProjectService;

@Slf4j
@RequestMapping("project")
@Controller
public class ProjectController {

    @Autowired
    ProjectService pj_service;
    @Autowired
    AdminService service;

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
    // @GetMapping("create")
    // public String create() {

    // return "projectView/create";
    // }

    // 작성된 프로젝트 등록
    @PostMapping("create")
    public String create(Project pj, ProjectMember pj_member, ProjectPart pj_part) {

        pj_service.insertProject(pj, pj_member, pj_part);
        return "redirect:/projectView/main";
    }

    // 프로젝트 팝업페이지 출력
    @GetMapping("create")
    public String create(@AuthenticationPrincipal UserDetails user, Model model) {
        // 회사코드, 관리자 내용 가져오기
        Employee admin = service.readAdmin(user.getUsername());
        Company company = service.readCompany(admin.getCompany_code());
        JSONArray json = service.readOrg(company);

        ArrayList<Employee> empList = service.employeeList(admin);

        for (int i = 0; i < empList.size(); i++) {
            if (empList.get(i).getRole_name().equals("ROLE_ADMIN")) {
                empList.remove(i);
            }
        }

        model.addAttribute("admin", admin);
        model.addAttribute("company", company);
        model.addAttribute("json", json);
        model.addAttribute("empList", empList);
        return "projectView/create";
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
