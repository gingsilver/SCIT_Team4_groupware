package net.softsociety.Team4GroupWare.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Security 설정
 */
@Configuration
public class WebSecurityConfig implements WebMvcConfigurer { // 클래스 이름은 사용자 지정
        @Autowired
        private DataSource dataSource;

        // 설정
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http.csrf().disable()
                                .authorizeRequests()
                                .antMatchers("/index").hasRole("USER")
                                .antMatchers("/admin/**").hasRole("ADMIN")
                                .antMatchers("/", "/employee/signup" // 경섭 : 사원 생성 => 곧 지울 예정
                                                , "/employee/checkRole" // 지은 : 로그인 시 권한 확인 ajax 페이지
                                                , "/company/signup" // 지은 : 회사 생성 페이지
                                                , "/company/join" // 지은 : 회사 생성 insert 페이지
                                                , "/company/checkid" // 지은 : 아이디 중복 체크
                                                , "/company/checkBusinessNum" // 지은 : 사업자 번호 중복 체크
                                                , "/company/findCompany" // 지은 : 회사 계정 찾기
                                                , "/employee/findout" // 경섭 : 계정 찾기
                                                , "/employee/renewpw" // 경섭 : 비밀번호 재설정
                                                , "/freeboard/list", "/freeboard/read", "/fullcalendar/**",
                                                "/assets/css/**", "/assets/img/**",
                                                "/assets/js/**", "/assets/lib/**", "/assets/vendor/**")
                                .permitAll() // 설정한 리소스의 접근을 인증절차 없이 허용
                                // .anyRequest().authenticated() // 위의 경로 외에는 모두 로그인을 해야 함
                                .and()
                                .formLogin() // 일반적인 폼을 이용한 로그인 처리/실패 방법을 사용
                                .loginPage("/employee/signin") // 시큐리티에서 제공하는 기본 폼이 아닌 사용자가 만든 폼 사용
                                .loginProcessingUrl("/employee/signin").permitAll() // 인증 처리를 하는 URL을 설정. 로그인 폼의
                                                                                    // action으로 지정
                                .usernameParameter("employee_id") // 로그인폼의 아이디 입력란의 name
                                .passwordParameter("employee_pwd") // 로그인폼의 비밀번호 입력란의 name
                                .defaultSuccessUrl("/default") // 로그인 성공했을 때의 url
                                .and()
                                .logout()
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/").permitAll() // 로그아웃시에 이동할 경로
                                .and()
                                .cors()
                                .and()
                                .httpBasic();

                return http.build();
        }

        // 인증을 위한 쿼리
        @Autowired
        public void configure(AuthenticationManagerBuilder auth) throws Exception {
	        auth.jdbcAuthentication()
	        .dataSource(dataSource)
	        // 인증 (로그인)
	        .usersByUsernameQuery(
	        "select employee_id username, employee_pwd password, enabled " +
	        "from team4_employee " +
	        "where employee_id = ?")
	        // 권한
	        .authoritiesByUsernameQuery(
	        "select employee_id username, role_name role_name " +
	        "from team4_employee " +
	        "where employee_id = ?");
        }

        // 단방향 비밀번호 암호화
        @Bean
        public PasswordEncoder passwordEncoder() {
                return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        }
}
