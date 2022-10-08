package net.softsociety.Team4GroupWare.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.service.AdminService;

@Controller
@RequestMapping("board")
public class BoardController {
	
	// 서비스 선언
	@Autowired
	AdminService service;
	
	@GetMapping("companynotice")
	public String companynotice(@AuthenticationPrincipal UserDetails user, Model model) {
		Employee admin = service.readAdmin(user.getUsername());
		
		model.addAttribute("employee", admin);
		return "boardView/Companynotice";
	}
	
	@GetMapping("programboard")
	public String programboard(@AuthenticationPrincipal UserDetails user, Model model) {
		Employee admin = service.readAdmin(user.getUsername());
		
		model.addAttribute("employee", admin);
		
		return "boardView/programboard";
	}

}
