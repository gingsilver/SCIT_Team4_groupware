package net.softsociety.Team4GroupWare.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Company;
import net.softsociety.Team4GroupWare.domain.DocumentForm;
import net.softsociety.Team4GroupWare.domain.Draft;
import net.softsociety.Team4GroupWare.domain.DraftApprover;
import net.softsociety.Team4GroupWare.domain.DraftOpnion;
import net.softsociety.Team4GroupWare.domain.Employee;
import net.softsociety.Team4GroupWare.service.AdminService;
import net.softsociety.Team4GroupWare.service.DraftService;
import net.softsociety.Team4GroupWare.util.FileService;

@Slf4j
@Controller
@RequestMapping("/draft")
@ResponseBody
public class DraftRestController {

	// 기안 서비스 선언
	@Autowired
	DraftService draftservice;

	// 관리자 서비스 선언
	@Autowired
	AdminService adminservice;

	// 기안 첨부파일 저장 위치 경로
	@Value("${uploadpath.draft}")
	String uploadPhotoPath;

	// 조직도 불러오는 ajax 컨트롤러
	@PostMapping("readOrg")
	public JSONArray readOrg(@AuthenticationPrincipal UserDetails user) {
		// 회사코드, 관리자 내용 가져오기
		Employee admin = adminservice.readAdmin(user.getUsername());
		Company company = adminservice.readCompany(admin.getCompany_code());

		JSONArray json = adminservice.readOrg(company);

		return json;
	}

	// 조직도 내 사원 목록 불러오는 ajax 컨트롤러
	@PostMapping("searchEmployee")
	public ArrayList<Employee> searchEmployee(@AuthenticationPrincipal UserDetails user, String organization) {
		Employee employee = adminservice.readAdmin(user.getUsername());
		Company company = adminservice.readCompany(employee.getCompany_code());

		String realOrg = organization.substring(company.getCompany_name().length() + 2);

		log.debug("가져온 팀명 : {}", realOrg);

		employee.setOrganization(realOrg);

		ArrayList<Employee> empList = adminservice.findByOrganization(employee);

		return empList;
	}
	
	ArrayList<DraftApprover> appList = new ArrayList<>();

	//결재선추가 : 배열에 추가
	@PostMapping("addApproverList")
	public void addApproverList(DraftApprover approver) {
		log.debug("가져온 데이터 : {}", approver);
		
		appList.add(approver);
		
/*		for(int i=0; i<appList.size(); i++) {
			System.out.println(appList.get(i).toString());
		}*/
	}
	
	//결재선 추가 : db저장
	@PostMapping("realAddApp")
	public void realAddApp(@AuthenticationPrincipal UserDetails user) {
		Employee employee = adminservice.readAdmin(user.getUsername());
		
		for(int i=0; i<appList.size(); i++) {
			appList.get(i).setEmployee_code(employee.getEmployee_code());
			appList.get(i).setDraft_code(draft_code);
			appList.get(i).setProcess_enabled(3);
			
			int result = draftservice.addDraftApprover(appList.get(i));
			System.out.println(result);
		}
	}

	String draft_code;
	// 기안 예비 코드 생성
	@PostMapping("addDraftCode")
	public String addDraftCode() {
		draft_code = draftservice.createCode();

		return draft_code;
	}

	// 양식함 추가
	@PostMapping("selectDoc")
	public ArrayList<DocumentForm> selectDoc(@AuthenticationPrincipal UserDetails user, String document_form_type) {
		Employee employee = adminservice.readAdmin(user.getUsername());
		DocumentForm doc = new DocumentForm();

		doc.setCompany_code(employee.getCompany_code());
		doc.setDocument_form_type(document_form_type);

		ArrayList<DocumentForm> docList = draftservice.selectByType(doc);

		log.debug("가져온 객체 : {}", docList);

		return docList;
	}

	// 양식 가져오기
	@PostMapping("showDoc")
	public DocumentForm showDoc(String document_form_code) {
		DocumentForm doc = draftservice.readDocumentForm(document_form_code);

		return doc;
	}

	// 양식 만들기
	@PostMapping("addDocForm")
	public int addDocForm(@AuthenticationPrincipal UserDetails user, DocumentForm docform) {
		Employee admin = adminservice.readAdmin(user.getUsername());
		docform.setCompany_code(admin.getCompany_code());
		docform.setDocument_form_writer_code(admin.getEmployee_code());
		docform.setDocument_form_writer_name(admin.getEmployee_name());
		log.debug("가져온 폼 내용 : {}", docform);

		int result = adminservice.addDocumentForm(docform);

		return result;
	}

	// 파일 업로드
	@PostMapping("uploadFile")
	public ArrayList<AttachedFile> uploadFile(@AuthenticationPrincipal UserDetails user, AttachedFile file,
			MultipartFile[] upload) {
		// 회사코드, 관리자 내용 가져오기
		Employee admin = adminservice.readAdmin(user.getUsername());

		log.debug("가져온 파일 : {}, 가져온 코드 : {}", upload.length, draft_code);
		
		file.setDocument_code(draft_code);
		file.setCompany_code(admin.getCompany_code());
		file.setEmployee_code(admin.getEmployee_code());
		file.setEmployee_id(admin.getEmployee_id());
		file.setEmployee_name(admin.getEmployee_name());
		
		for(MultipartFile multipartFile : upload) {
			log.debug("----------------------------------");
			log.debug("upload file name : " + multipartFile.getOriginalFilename());
			log.debug("upload file size : " + multipartFile.getSize());
			
			if (multipartFile != null && !multipartFile.isEmpty() && multipartFile.getSize() != 0) {
				try {
					String absoluteUploadPath = new ClassPathResource(uploadPhotoPath).getFile().getAbsolutePath();
					String savedfile = FileService.saveFile(multipartFile, absoluteUploadPath);
					log.debug("absoluteUploadPath : {}", absoluteUploadPath);

					// 확장자 추출
					String originalFilename = multipartFile.getOriginalFilename();
					int lastIndex = originalFilename.lastIndexOf('.');
					String ext = lastIndex == -1 ? "" : originalFilename.substring(lastIndex);

					file.setAttached_file_ext(ext);
					file.setAttached_file_origin_name(originalFilename);
					file.setAttached_file_save_name(savedfile);
					
					int result = draftservice.addDraftAttFile(file);

					log.debug("파일 데이터 : {}", file);
				
				} catch (IOException e) {
					log.debug(e.getMessage());
				}
			
			}
		}
		ArrayList<AttachedFile> attFileList = draftservice.selectAllDraftFile(draft_code);

		return attFileList;
	}
	
	//의견 추가
	@PostMapping("addOpinion")
	public int addOpinion(@AuthenticationPrincipal UserDetails user, String opinion_contents, String draft_code) {
		log.debug("가져옴 : {}, {}", opinion_contents, draft_code);
		
		Employee admin = adminservice.readAdmin(user.getUsername());
		DraftOpnion opinion = new DraftOpnion();
		
		opinion.setDraft_code(draft_code);
		opinion.setOpinion_writer_code(admin.getEmployee_code());
		opinion.setOpinion_writer_name(admin.getEmployee_name());
		opinion.setOpinion_contents(opinion_contents);
		
		int result = draftservice.addOpinion(opinion);
		
		return result;
	}
	
	//의견 가져오기
	@PostMapping("readOpinion")
	public ArrayList<DraftOpnion> readOpinion(String draft_code){
		ArrayList<DraftOpnion> opList = draftservice.selectAllOpinion(draft_code);
		
		return opList;
	}
	
	//기안 저장
	@PostMapping("addDraft")
	public int addDraft(@AuthenticationPrincipal UserDetails user, Draft draft) {
		log.debug("가져옴 : {}", draft);
		Employee admin = adminservice.readAdmin(user.getUsername());
		draft.setCompany_code(admin.getCompany_code());
		draft.setEmployee_code(admin.getEmployee_code());
		draft.setEmployee_name(admin.getEmployee_name());
		int result = draftservice.addDraft(draft);
		
		return result;
	}
	
	//결재선 가져오기
	@PostMapping("selectApp")
	public ArrayList<DraftApprover> selectApp(String draft_code) {
		ArrayList<DraftApprover> appList = draftservice.selectAllDraftApprover(draft_code);
	
		return appList;
	}
	
}
