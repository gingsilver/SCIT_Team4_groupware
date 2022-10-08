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
import net.softsociety.Team4GroupWare.domain.DraftBox;
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
	
	// 전자결재 - 기안 작성 : 예비 기안 코드를 위해 전역 변수로 선언
	String draft_code;
	
	// 전자결재 - 기안 작성 : 기안 예비 코드 생성
	@PostMapping("addDraftCode")
	public String addDraftCode() {
		draft_code = draftservice.createCode();

		return draft_code;
	}

	// 전자결재 - 기안 작성 - 결재선추가 : 조직도 불러오기
	@PostMapping("readOrg")
	public JSONArray readOrg(@AuthenticationPrincipal UserDetails user) {
		// 회사코드, 로그인 한 사원 내용 가져오기
		Employee admin = adminservice.readAdmin(user.getUsername());
		Company company = adminservice.readCompany(admin.getCompany_code());

		// 회사 내용을 토대로 조직도를 JsonArray에 담아서 가져오기
		JSONArray json = adminservice.readOrg(company);

		return json;
	}

	// 전자결재 - 기안 작성 - 결재선추가 : 조직도 내 사원 목록 불러오기
	@PostMapping("searchEmployee")
	public ArrayList<Employee> searchEmployee(@AuthenticationPrincipal UserDetails user, String organization) {
		// 회사코드, 로그인 한 사원 내용 가져오기
		Employee employee = adminservice.readAdmin(user.getUsername());
		Company company = adminservice.readCompany(employee.getCompany_code());

		// 부서명에서 회사이름 제외하기
		log.debug("가져온 원래 팀명 : {}", organization);
		String realOrg = organization.substring(company.getCompany_name().length() + 2);
		log.debug("가져온 팀명 : {}", realOrg);

		// 회사이름 제외된 부서명을 사원의 정보에 담기
		employee.setOrganization(realOrg);
		
		// 로그인 한 관리자의 회사 코드, 부서명을 통해 사원 부서명에 맞는 사원 리스트 가져오기
		ArrayList<Employee> empList = adminservice.findByOrganization(employee);

		return empList;
	}
	
	// 전자결재 - 기안 작성 - 결재선추가 : 결재선 테이블에 가져갈 결재선 리스트를 전역 변수로 선언
	ArrayList<DraftApprover> appList = new ArrayList<>();

	// 전자결재 - 기안 작성 - 결재선추가 : 전역 변수에 선택한 결재선 내용을 배열에 추가
	@PostMapping("addApproverList")
	public void addApproverList(DraftApprover approver) {
		log.debug("가져온 데이터 : {}", approver);
		
		appList.add(approver);
	}
	
	// 전자결재 - 기안 작성 - 결재선추가 : 전역 변수에 선언된 배열안에 있는 결재자들을 DB에 저장
	@PostMapping("realAddApp")
	public void realAddApp(@AuthenticationPrincipal UserDetails user) {
		// 로그인 한 사원의 정보 가져오기
		Employee employee = adminservice.readAdmin(user.getUsername());
		
		// 전역 변수에 있는 결재선 리스트 사이즈만큼 반복
		// 전역 변수 속 결재선 리스트에 회사코드, 기안코드, 결재상태 추가하기
		for(int i=0; i<appList.size(); i++) {
			appList.get(i).setEmployee_code(employee.getEmployee_code());
			appList.get(i).setDraft_code(draft_code);
			appList.get(i).setProcess_enabled(3);
			
			// 1개의 결재선 리스트를 결재선 테이블에 저장
			int result = draftservice.addDraftApprover(appList.get(i));
		}
	}

	// 전자결재 - 기안 작성 - 양식함 리스트 가져오기
	@PostMapping("selectDoc")
	public ArrayList<DocumentForm> selectDoc(@AuthenticationPrincipal UserDetails user, String document_form_type) {
		// 로그인 한 사원의 정보 가져오기
		Employee employee = adminservice.readAdmin(user.getUsername());
		
		// 선택한 양식 타입에 맞는 양식리스트를 가져오기 위해 양식함을 새롭게 선언
		DocumentForm doc = new DocumentForm();
		// 선언한 양식함에 회사코드와 사용자가 선택한 양식 타입을 입력
		doc.setCompany_code(employee.getCompany_code());
		doc.setDocument_form_type(document_form_type);
		// 사용자가 입력한 내용에 맞게 양식함 리스트 가져오기
		ArrayList<DocumentForm> docList = draftservice.selectByType(doc);
		log.debug("가져온 객체 : {}", docList);

		return docList;
	}

	// 전자결재 - 기안 작성 - 양식선택 시 양식 내용 가져오기
	@PostMapping("showDoc")
	public DocumentForm showDoc(String document_form_code) {
		// 선택한 양식의 양식코드로 양식 내용을 가져오기
		DocumentForm doc = draftservice.readDocumentForm(document_form_code);

		return doc;
	}

	// 전자결재 - 기안 작성 - 파일 업로드 : 다중 파일 업로드
	@PostMapping("uploadFile")
	public ArrayList<AttachedFile> uploadFile(@AuthenticationPrincipal UserDetails user, AttachedFile file,
			MultipartFile[] upload) {
		// 회사코드, 관리자 내용 가져오기
		Employee admin = adminservice.readAdmin(user.getUsername());
		log.debug("가져온 파일 : {}, 가져온 코드 : {}", upload.length, draft_code);
		
		// 파일 객체에 회사코드, 기안코드, 사원 코드/아이디/이름 입력하기
		file.setDocument_code(draft_code);
		file.setCompany_code(admin.getCompany_code());
		file.setEmployee_code(admin.getEmployee_code());
		file.setEmployee_id(admin.getEmployee_id());
		file.setEmployee_name(admin.getEmployee_name());
		
		// 반복문으로 모든 모든 파일 저장
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

					// 추출한 확장자, 원래 파일 이름, 저장된 파일 이름을 파일객체에 입력
					file.setAttached_file_ext(ext);
					file.setAttached_file_origin_name(originalFilename);
					file.setAttached_file_save_name(savedfile);
					
					// 입력한 파일 객체를 파일 테이블에 저장
					int result = draftservice.addDraftAttFile(file);
					log.debug("파일 데이터 : {}", file);
				} catch (IOException e) {
					log.debug(e.getMessage());
				}
			}
		}
		
		// 저장한 파일 리스트를 가져오기
		ArrayList<AttachedFile> attFileList = draftservice.selectAllDraftFile(draft_code);

		return attFileList;
	}
	
	// 전자결재 - 기안 작성 - 의견 추가 : 의견 저장
	@PostMapping("addOpinion")
	public int addOpinion(@AuthenticationPrincipal UserDetails user, String opinion_contents, String draft_code) {
		// 의견 내용과 기안 코드를 가져오기
		log.debug("의견 내용 : {}, 기안 코드 : {}", opinion_contents, draft_code);
		// 로그인 한 사원 내용 가져오기
		Employee admin = adminservice.readAdmin(user.getUsername());
		// 의견 추가를 위한 의견 VO 선언
		DraftOpnion opinion = new DraftOpnion();
		// 선언된 의견 VO에 기안코드, 작성자 코드/이름, 의견 내용 입력
		opinion.setDraft_code(draft_code);
		opinion.setOpinion_writer_code(admin.getEmployee_code());
		opinion.setOpinion_writer_name(admin.getEmployee_name());
		opinion.setOpinion_contents(opinion_contents);
		
		// 입력된 의견 내용을 DB에 저장
		int result = draftservice.addOpinion(opinion);
		
		return result;
	}
	
	// 전자결재 - 기안 작성 - 의견 추가 : 의견함 내용 가져오기
	@PostMapping("readOpinion")
	public ArrayList<DraftOpnion> readOpinion(String draft_code){
		// 가져온 기안 코드와 맞는 의견 리스트 가져오기
		ArrayList<DraftOpnion> opList = draftservice.selectAllOpinion(draft_code);
		
		return opList;
	}
	
	// 전자결재 - 기안 저장
	@PostMapping("addDraft")
	public int addDraft(@AuthenticationPrincipal UserDetails user, Draft draft) {
		// 입력된 기안 정보 가져오기
		log.debug("가져온 기안 : {}", draft);
		
		// 로그인한 사원 정보 가져오기
		Employee admin = adminservice.readAdmin(user.getUsername());
		
		// 기안에 회사코드, 로그인한 사원 코드, 이름 입력
		draft.setCompany_code(admin.getCompany_code());
		draft.setEmployee_code(admin.getEmployee_code());
		draft.setEmployee_name(admin.getEmployee_name());
		
		// 입력된 모든 내용을 토대로 기안 DB에 저장
		int result = draftservice.addDraft(draft);
		
		return result;
	}
	
	// 전자결재 - 기안 읽기 - 결재선 가져오기
	@PostMapping("selectApp")
	public ArrayList<DraftApprover> selectApp(String draft_code) {
		// 가져온 기안 코드 확인
		log.debug("기안 코드 : {}", draft_code);
		
		// 기안 코드를 바탕으로 결재선 리스트 가져오기
		ArrayList<DraftApprover> appList = draftservice.selectAllDraftApprover(draft_code);
		log.debug("결재선 배열 : {}", appList);
		 
		return appList;
	}
	
	// 전자결재 - 기안 읽기 - 첨부파일 가져오기
	@PostMapping("readAttachedFile")
	public ArrayList<AttachedFile> readAttachedFile(String document_code){
		// 가져온 기안 코드 확인 : documemt_code인 이유 : 첨부파일 테이블은 코드 이름을 document_code로 받음
		log.debug("양식 코드 : {}", document_code);
		
		// 기안 코드를 바탕으로 첨부파일 리스트 가져오기
		ArrayList<AttachedFile> attList = draftservice.selectAllAttachedFile(document_code);
		log.debug("첨부파일 배열 : {}", attList);
		 
		return attList;
	}
	
	// 전자결재 - 기안 읽기 - 전결자 가져오기
	@PostMapping("selectFinal")
	public String selectFinal(String document_form_code) {
		// 양식 코드를 통해 전결자 내용 가져오기
		String finalApprover = draftservice.findAppByCode(document_form_code);
		log.debug("전결자 : {}", finalApprover);
		
		return finalApprover;
	}
	
	// 전자결재 - 결재 요청함
	// ★코드 수정 필요 : 방금 작성한 결재 내역이 보이지 않았음!!
	@PostMapping("selectAllDraft")
	public ArrayList<DraftBox> selectAllDraft(DraftBox draftbox){
		log.debug("검색 할 데이터 : {}", draftbox);
		ArrayList<DraftBox> requestArr = draftservice.selectAllDraft(draftbox);
		log.debug("결재 진행함 : {}", requestArr);
					
		return requestArr;
	}
	
	// 전자결재 - 결재 대기함
	// ★코드 수정 필요 : 미완성
	@PostMapping("waitingDraft")
	public ArrayList<DraftBox> waitingDraft(DraftBox draftbox){
		ArrayList<DraftBox> waitingArr = draftservice.findWaitingDraft(draftbox);
		log.debug("결재 대기함 : {}", waitingArr);
		
		return waitingArr;
	}
	
	// 전자결재 - 결재 진행함
	// ★코드 수정 필요 : 미완성
	@PostMapping("progressDraft")
	public ArrayList<DraftBox> progressDraft(DraftBox draftbox){
		ArrayList<DraftBox> progressgArr = draftservice.findProgressDraft(draftbox);
		log.debug("결재 진행함 : {}", progressgArr);
		
		return progressgArr;
	}
	
	// 전자결재 - 결재 완료함
	// ★코드 수정 필요 : 미완성
	@PostMapping("completeDraft")
	public ArrayList<DraftBox> completeDraft(DraftBox draftbox){
		ArrayList<DraftBox> completeArr = draftservice.findCompleteDraft(draftbox);
		ArrayList<DraftBox> completeArr2 = draftservice.findCompleteDraft2(draftbox);
		
		ArrayList<DraftBox> completeArr3 = new ArrayList<>();
		
		for(int i=0; i<completeArr.size(); i++) {
			completeArr3.add(completeArr.get(i));
		}
		System.out.println(completeArr3);
		System.out.println("-----------------------------------------------");
		
		for(int i=completeArr.size(); i<completeArr2.size(); i++) {
			completeArr3.add(completeArr2.get(i));
		}
		System.out.println(completeArr3);
		
		log.debug("결재 완료함 : {}", completeArr3);
			
		return completeArr3;
	}
	
	// 전자결재 - 결재 반려함
	// ★코드 수정 필요 : 미완성
	@PostMapping("rejectDraft")
	public ArrayList<DraftBox> rejectDraft(DraftBox draftbox){
		ArrayList<DraftBox> rejectArr = draftservice.findRejectDraft(draftbox);
		log.debug("결재 진행함 : {}", rejectArr);
			
		return rejectArr;
	}
	
	// 전자결재 - 결재 참조함
	// ★코드 수정 필요 : 미완성
	@PostMapping("referenceDraft")
	public ArrayList<DraftBox> referenceDraft(DraftBox draftbox){
		ArrayList<DraftBox> referenceArr = draftservice.findReferenceDraft(draftbox);
		log.debug("결재 진행함 : {}", referenceArr);
				
		return referenceArr;
	}
	
	// 전자결재 - 양식함 - 양식 작성 : 사용자가 입력한 양식 내용 저장
	@PostMapping("addDocForm")
	public int addDocForm(@AuthenticationPrincipal UserDetails user, DocumentForm docform) {
		// 로그인 한 관리자의 내용 가져오기
		Employee admin = adminservice.readAdmin(user.getUsername());
		
		// 가져온 양식 내용에 회사코드, 작성자 코드/아이디를 저장
		docform.setCompany_code(admin.getCompany_code());
		docform.setDocument_form_writer_code(admin.getEmployee_code());
		docform.setDocument_form_writer_name(admin.getEmployee_name());
		log.debug("가져온 폼 내용 : {}", docform);

		// 가져온 양식 내용을 저장
		int result = adminservice.addDocumentForm(docform);

		return result;
	}

}
