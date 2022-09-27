package net.softsociety.Team4GroupWare.controller;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.service.EmployeeService;
import net.softsociety.Team4GroupWare.util.FileService;
/**
 사원의 정보 관련 컨트롤러
 사원의 로그인, 개인정보수정을 해당컨트롤러에서 처리한다.
 */

@Slf4j
@RequestMapping("employee")
@Controller
public class EmployeeController {

	@Autowired
	EmployeeService service;


	//사원 프로필 사진 저장 위치 경로
	@Value("${uploadpath.photo}")
	String uploadPhotoPath;

	//로그인화면 출력
	@GetMapping("/signin")
	public String signin() {
		log.debug("sign in 출력");
		return "employee/signin";
	}

	//관리자만 접근 가능한 가입폼으로 이동
	@GetMapping("signup")
	public String signup() {
		return "/employee/signup";
	}

	//가입 처리
	@PostMapping("signup")
	public String signup(Employee employee) {
		log.debug(" join 실행 가입정보 : {}", employee);
		int result = service.insertEmployee(employee);

		return "redirect:/";
	}

	//계정찾기 화면으로
	@GetMapping("/findout")
	public String findout() {
		log.debug("find out 출력");
		return "employee/findout";
	}

	//아이디 전달받기
	@PostMapping("/findout")
	public String findout(Employee employee, Model model) {
		log.debug("입력받은 정보: {}", employee);
		String result = service.findId(employee);
		log.debug("전달받은 아이디: {}", result);

		model.addAttribute("employee_id", result);

		return "employee/renewPW";
	}


	//비밀번호 재설정 수행
	@PostMapping("/renewpw")
	public String renewpw(Employee employee) {
		log.debug("입력받은 정보: {}", employee);
		int result = service.renewPw(employee);

		return "redirect:/";
	}


	/**
	 * 권한 찾기 메소드
	 * @param radioVal : 회원이 체크한 권한
	 * @param employee_id : 회원이 입력한 아이디
	 * @return 입력한 내용과 맞는 회원의 수(1이 아니면 다 틀림!)
	 */
	@ResponseBody
	@PostMapping("checkRole")
	public int checkRole(String radioVal,  String employee_id) {
		log.debug("입력받은 정보: {}, {}", radioVal, employee_id);

		Employee employee = new Employee();
		employee.setRole_name(radioVal);
		employee.setEmployee_id(employee_id);

		int result = service.checkRole(employee);

		return result;
	}

	//마이페이지 화면으로
	@GetMapping("/mypage")
	public String mypage(Model model, @AuthenticationPrincipal UserDetails user) {
		log.debug("mypage 출력");
		Employee employee = service.getEmployeeById(user.getUsername());

		log.debug("Employee 받은 내용: {}", employee);

		String employee_photo = employee.getEmployee_photo();

		model.addAttribute("employee", employee);
		model.addAttribute("employee_photo", employee_photo);



		return "employee/mypage";
	}

	//프로필 사진 업데이트(사진과 정보는 별개로 받음)
	@PostMapping("updatephoto")
	public String updateLogo(@AuthenticationPrincipal UserDetails user
			, AttachedFile file
			, MultipartFile upload) {
		log.debug("uploadPhotoPath() called");
		//회사코드, 관리자 내용 가져오기
		Employee employee = service.getEmployeeById(user.getUsername());
		log.debug("employee : {}", employee);

		file.setCompany_code(employee.getCompany_code());
		file.setEmployee_code(employee.getEmployee_code());
		file.setEmployee_id(employee.getEmployee_id());
		file.setEmployee_name(employee.getEmployee_name());
		log.debug("file : {}", file);

		if (upload != null && !upload.isEmpty() && upload.getSize()!=0) {

			try {
				String absoluteUploadPath = new ClassPathResource(uploadPhotoPath).getFile().getAbsolutePath();
				log.debug("absoluteUploadPath : {}", absoluteUploadPath);
				String savedfile = FileService.saveFile(upload, absoluteUploadPath);

				//확장자 추출
				String originalFilename = upload.getOriginalFilename();
				int lastIndex = originalFilename.lastIndexOf('.');
				String ext = lastIndex == -1 ? "" : originalFilename.substring(lastIndex);

				file.setAttached_file_ext(ext);
				file.setAttached_file_origin_name(originalFilename);
				file.setAttached_file_save_name(savedfile);

				log.debug("파일 데이터 : {}", file);

				int result = service.insertPhoto(file);
				if(result == 1) {

					employee.setCompany_code(employee.getCompany_code());
					employee.setEmployee_photo(file.getAttached_file_save_name());

					service.updatePhoto(employee);
				}
			} catch (IOException e) {
				log.debug(e.getMessage());
			}
		}

		return "redirect:/employee/mypage";
	}


	//개인정보 업데이트
	@PostMapping("/updateinfo")
	public String updateinfo(Employee employee) {
		log.debug("입력받은 정보: {}", employee);
		int result = service.updateinfo(employee);

		return "redirect:/index";
	}

}






