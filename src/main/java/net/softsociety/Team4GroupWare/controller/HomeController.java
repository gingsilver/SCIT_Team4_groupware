package net.softsociety.Team4GroupWare.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.LoginUser;

@Controller
@Slf4j
public class HomeController {
	//홈화면 출력
	@GetMapping({"", "/"})
	public String home() {
		return "home";
	}
	
	//로그인 시 권한에 따라 다른 페이지 나오게 하기
	@RequestMapping("/default")
	public String defaultAfterLogin(HttpServletRequest request) {
		if(request.isUserInRole("ROLE_ADMIN")) {
			return "redirect:/admin";
		}
		
		return "redirect:/index";
	}
	
	//사원 메인화면 출력
	@GetMapping({"index"})
	public String index(@AuthenticationPrincipal UserDetails user) {
		
		log.debug("{}", user);
		
		return "index";
	}
	
	//관리자 메인화면 출력
	@GetMapping({"admin"})
	public String admin(@AuthenticationPrincipal UserDetails user) {
		return "adminView/adminIndex";
	}
	
}
