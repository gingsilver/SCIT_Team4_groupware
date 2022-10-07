package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.Project;
import net.softsociety.Team4GroupWare.domain.ProjectMember;
import net.softsociety.Team4GroupWare.domain.ProjectPart;
import net.softsociety.Team4GroupWare.service.AdminService;
import net.softsociety.Team4GroupWare.service.EmployeeService;
import net.softsociety.Team4GroupWare.service.ProjectService;

@Slf4j
@RequestMapping("project")
@Controller
public class ProjectController {

    @Autowired
    ProjectService pj_service;
    @Autowired
    AdminService service;
    @Autowired
    EmployeeService em_service;

    // 프로젝트 메인 페이지 출력
    @GetMapping("main")
    public String main(Model model, @AuthenticationPrincipal UserDetails user) {

        // 프로젝트 진행도

        // 프로젝트 페이지
        Employee employee = em_service.getEmployeeById(user.getUsername());
        String employee_code = employee.getEmployee_code();
        List<Project> projectList = pj_service.projectList(user.getUsername());
        projectList.forEach(Project::setDate);
        model.addAttribute("employee_code", employee_code);
        model.addAttribute("projectList", projectList);
        return "projectView/main";
    }

    @GetMapping("update")
    public String update(Model model, @AuthenticationPrincipal UserDetails user, String pj_code) {

        List<ProjectPart> projectPart = pj_service.selectPj_part(pj_code);
        Project projectFind = pj_service.projectFind(pj_code);
        List<ProjectMember> selectPj_member = pj_service.selectPj_member(pj_code);
        for (ProjectMember m : selectPj_member) {
            Employee employee = pj_service.getEmployeeById(m.getEmployee_code());
            m.setEmployee_id(employee.getEmployee_id());
            m.setEmployee_name(employee.getEmployee_name());
            m.setPosition_type(employee.getPosition_type());
            m.setOrganization(employee.getOrganization());
        }
        System.out.println(projectPart + "테스트");
        System.out.println(selectPj_member);
        System.out.println(projectFind);
        model.addAttribute("projectFind", projectFind);
        model.addAttribute("selectPj_member", selectPj_member);
        model.addAttribute("projectPart", projectPart);
        return "projectView/update";
    }

    // 작성된 프로젝트 등록
    /*
     * @PostMapping("create")
     * public String create(Project pj, ProjectMember pj_member, ProjectPart
     * pj_part) {
     * 
     * pj_service.insertProject(pj, pj_member, pj_part);
     * return "redirect:/projectView/main";
     * }
     */

    // 작성된 프로젝트 등록
    @PostMapping("create")
    public String create(MemberListForm form, Project pj) {
        System.out.println(pj);
        System.out.println("memberList = " + form);
        List<ProjectMember> memberList = form.getMemberList();
        pj.setEmployee_code(memberList.stream().filter(i -> i.getPosition_type().equals("리더")).findFirst().get()
                .getEmployee_code());
        System.out.println(form.getMemberList());
        pj_service.insertProject(pj, memberList);
        return "redirect:/project/main";
    }

    // 프로젝트 출력
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

    @PostMapping("addMember")
    public String addMember(Employee employee, Model model) {
        System.out.println("확인");
        Employee emp = new Employee();

        model.addAttribute("emp", emp);
        return "redirect:/projectView/main";
    }

    // 프로젝트 작성페이지 조직도 출력
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    static private class MemberListForm {
        private List<ProjectMember> memberList;
    }

}
