package net.softsociety.Team4GroupWare.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser implements UserDetails {
	
	private static final long serialVersionUID = 5447109745463233395L;
	Employee employee;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public String getUsername() {
		
		return employee.getEmployee_id();
	}
	
	@Override
	public String getPassword() {

		return employee.getEmployee_pwd();
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return employee.isEnabled();
	}
	
	//이하 추가 내용
	
	public String getCompany_code() {
		
		return employee.getCompany_code();
	}
	
	public String getEmployee_code() {
		
		return employee.getEmployee_code();
	}
	
	public String getRole_name() {
		
		return employee.getRole_name();
	}

}
