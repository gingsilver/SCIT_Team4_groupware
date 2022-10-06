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
public class Employee implements UserDetails {

	private static final long serialVersionUID = -836144816010140016L;
	String employee_code;
	String company_code;
	String employee_id;
	String employee_pwd;
	String employee_name;
	String employee_gender;
	String employee_email;
	String employee_birthdate;
	String employee_phone;
	String employee_address;
	String employee_address2;
	String employee_photo;
	String role_name;
	String position_type;
	String organization;
	boolean enabled;
	String hiredate;
	String retiredate;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		return this.getEmployee_pwd();
	}

	@Override
	public String getUsername() {
		return this.getEmployee_id();
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
		return this.enabled;
	}

}
