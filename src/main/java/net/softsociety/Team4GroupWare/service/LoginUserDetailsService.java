package net.softsociety.Team4GroupWare.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.EmployeeDAO;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.domain.LoginUser;

@Slf4j
@Service
public class LoginUserDetailsService implements UserDetailsService {

	@Autowired
	EmployeeDAO dao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("로그인폼에서 서버로 전달된 유저네임:{}", username);	
		
		Employee employee = dao.getEmployeeById(username); 
		
		if(employee == null) {
			throw new UsernameNotFoundException(username);
			
		}
		
		//세션에 저장할 객체 (UserDetails를 구현한) 생성하여 리턴
		LoginUser user = new LoginUser();
		user.setEmployee(employee);
		
		return user;
	}
}

