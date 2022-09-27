package net.softsociety.Team4GroupWare.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Email;
import net.softsociety.Team4GroupWare.domain.MailProcess;
import net.softsociety.Team4GroupWare.domain.Mailinfo;
import net.softsociety.Team4GroupWare.service.EmailService;
import net.softsociety.Team4GroupWare.util.FileService;


@Slf4j
@Controller
@RequestMapping("mailbox")
public class EmailController {

	@Autowired
	EmailService service;

	//첨부파일 저장할 경로
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;
	
	
	/*------------------------------ 메일함 페이지 나누기 ------------------------------*/
	
	
	//게시판 목록의 페이지당 글 수
	@Value("${user.board.page}")
	int countPerPage;
	
	//게시판 목록의 페이지 이동 링크 수 
	@Value("${user.board.group}")
	int pagePerGroup;	

	
	/*------------------------------ 메일 쓰기 ------------------------------*/
	
	//메일 쓰기 페이지로 이동
	@GetMapping("write")
	public String write() {
		return "mailbox/write";
	}
	
	
	//작성된 메일 내용 받아오기
	@PostMapping("write")
	public String sendMail(Email email
			, AttachedFile file
			, MultipartFile upload
			, MailProcess mail_process
			) throws Exception {

		//회사코드, 관리자 내용 가져오기
		/*
		Employee admin = service.readAdmin(user.getUsername());
		
		file.setCompany_code(admin.getCompany_code());
		file.setEmployee_code(admin.getEmployee_code());
		file.setEmployee_id(admin.getEmployee_id());
		file.setEmployee_name(admin.getEmployee_name());
		*/
		String email_sender = "pp2000pooh@naver.com"; 
		email.setEmail_sender(email_sender);
		email.setCompany_code("COM0007"); //로그인 정보 받아와서 넣기 현재는 임시 정보

		/*---------------------이메일 정보를 메일함에 보내기----------------------------*/
		
		int return_email_code = service.sendMailWithFiles(email, upload, mail_process); //성공1, 실패0으로 넘어옴
		String new_email_code = email.getEmail_code();

		System.out.println("컨트롤러에서 값이 잘 넘어오나 확인"+ email.getEmail_code());

		
		/*---------------------청부파일 정보 입력--------------------------*/
		
		if (upload != null && !upload.isEmpty() && upload.getSize()!=0) {
			
				String savedfile = FileService.saveFile(upload, uploadPath+"/mail");

				//확장자 추출
				String originalFilename = upload.getOriginalFilename();
				int lastIndex = originalFilename.lastIndexOf('.');
				String ext = lastIndex == -1 ? "" : originalFilename.substring(lastIndex);
				
				file.setAttached_file_ext(ext);
				file.setAttached_file_origin_name(originalFilename);
				file.setAttached_file_save_name(savedfile);
				
				log.debug("파일 데이터 : {}", file);
				
				service.insertMailAtteched(file);
		}
	
		/*-------------------수신인, 참조인 저장하기----------------------------*/

		if(return_email_code == 1) {

			mail_process.setEmail_code(new_email_code);
			mail_process.setCompany_code("COM0007");

			//맵으로 메일 번호랑 수신인 배열 바라바라 하기
			String[] ReceiverArr = mail_process.getEmail_receiver();

			for(int i=0; i<ReceiverArr.length; i++) {
				@SuppressWarnings("rawtypes")				
				String Receiver = ReceiverArr[i];
				String email_code = mail_process.getEmail_code();
				String company_code = mail_process.getCompany_code();

				service.insertReceiver(email_code, company_code, Receiver);
			}	

			//맵으로 메일 번호랑 참조인 배열 바라바라 하기
			String[] CcReceiverArr = mail_process.getEmail_cc_receiver();

			for(int i=0; i<CcReceiverArr.length; i++) {
				@SuppressWarnings("rawtypes")				
				String ccReceiver = CcReceiverArr[i];
				String email_code = mail_process.getEmail_code();
				String company_code = mail_process.getCompany_code();

				service.insertCCreceiver(email_code, company_code, ccReceiver);
			}			


		}else {
			//실패시, 임시저장
			service.sendMaildraft(email);
			return "mailbox/fail";
		}
		//성공시, 완료페이지 
		return "mailbox/success";


	}

	
	/*------------------------------ 보낸 메일함 ------------------------------*/

	@GetMapping("sentMailbox")
	public String sentMailbox(Model model
						, @RequestParam(name = "page", defaultValue="1")int page
						) {
		
		//여기서 알아야할정보 '나'가 누군지
		//Company_code("COM0007"); 로그인 정보 받아와서 넣기 현재는 임시 정보
		//email_sender는 로그인 정보에서 가져와야함, 여기서는 모르니까 임의로 넣고 진행(pp2000pooh@naver.com)
		//DB에서 글을 읽어서
		String email_sender = "pp2000pooh@naver.com"; 
		
		
		log.debug("페이지당 글 수 : {}, 페이지 이동 링크 수 : {}, 현재 페이지 : {}, 로그인 한 회원 아이디 : {}"
				, countPerPage, pagePerGroup, page, email_sender);
		
		net.softsociety.Team4GroupWare.util.PageNavigator navi = service.getPageNavigator(
				pagePerGroup, countPerPage, page, email_sender);
		
		ArrayList<Mailinfo> mailinfo = service.selectSentmail(email_sender); 

		
		//모델에 담아서 html로 보내주기
		model.addAttribute("navi", navi);
		model.addAttribute("mailinfo", mailinfo);
		
		return "mailbox/sentMailbox";
	}
	
	
	@GetMapping("email-read")
	public String readSentMail() {
		return "mailbox/email-read";
	}
	
	/*------------------------------ 보낸 메일함 - 메일 1개 읽기 ------------------------------*/
	@GetMapping("sentMailboxOne")
	public String readSnetmail(
		String email_code
		, Model model) {
		log.debug("VIEW에서 넘어온 메일 번호 : {}", email_code);
		
		//DB에서 글을 읽어서
		Mailinfo mailinfo = service.selectOne(email_code);
		
		log.debug("DB에서 넘어온 게시글 정보 : {} ", mailinfo);
	
		model.addAttribute("mailinfo", mailinfo);
		
		
		//결과를 모델에 담아서 HTML에서 출력
		return "/mailbox/sentMailboxOne";
	}
	
	/*------------------------------ 전체 메일함 ------------------------------*/

	@GetMapping("readAll")
	public String readAll(Model model
			) {
		
		//여기서 알아야할정보 '나'가 누군지
		//Company_code("COM0007"); 로그인 정보 받아와서 넣기 현재는 임시 정보
		//email_sender는 로그인 정보에서 가져와야함, 여기서는 모르니까 임의로 넣고 진행(pp2000pooh@naver.com)
		//DB에서 글을 읽어서
		String email_receiver = "hongvely0829@gmail.com"; 
		String email_cc_receiver = "hongvely0829@gmail.com";
		
		
		ArrayList<Mailinfo> mailinfo = service.readAllmail(email_receiver, email_cc_receiver); 

		System.out.println(mailinfo);
		//모델에 담아서 html로 보내주기
		model.addAttribute("mailinfo", mailinfo);
		
		return "mailbox/readAll";
	}
	
	/*------------------------------ 전체 메일함 - 메일 1개 읽기 ------------------------------*/
	@GetMapping("readOne")
	public String read(
		String email_code
		, Model model) {
		log.debug("VIEW에서 넘어온 메일 번호 : {}", email_code);
		
		//DB에서 글을 읽어서
		Mailinfo mailinfo = service.selectOne(email_code);
		
		log.debug("DB에서 넘어온 게시글 정보 : {} ", mailinfo);
	
		model.addAttribute("mailinfo", mailinfo);
		
		
		//결과를 모델에 담아서 HTML에서 출력
		return "/mailbox/readOne";
	}
	
	
}