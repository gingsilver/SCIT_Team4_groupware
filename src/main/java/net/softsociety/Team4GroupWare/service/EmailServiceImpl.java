package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.softsociety.Team4GroupWare.dao.EmailDAO;
import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Email;
import net.softsociety.Team4GroupWare.domain.MailProcess;
import net.softsociety.Team4GroupWare.domain.Mailinfo;
import net.softsociety.Team4GroupWare.util.PageNavigator;

@Slf4j
@Service
@AllArgsConstructor
public class EmailServiceImpl implements EmailService {

	@Autowired
	EmailDAO emailDAO;


	//메일 쓰기

	@Override
	public int sendMailWithFiles(Email email, MultipartFile upload, MailProcess mail_process) throws Exception {

        int mail_return = emailDAO.sendToMailbox(email);
        return mail_return;
	}
	
	//첨부파일 저장
	@Override
	public void insertMailAtteched(AttachedFile file) {
		emailDAO.insertMailAtteched(file);
		
	}

	//임시 저장
	@Override
	public void sendMaildraft(Email email) {
		emailDAO.sendMaildraft(email);

	}
	
	//수신인 저장하기
	@Override
	public void insertReceiver(String email_code, String company_code, String receiver) {
		HashMap<String, String> map = new HashMap<>();
		
		map.put("email_code", email_code);
		map.put("company_code", company_code);
		map.put("email_receiver", receiver);
		
		emailDAO.insertReceiver(map);
		
	}


	//참조인 저장하기
	@Override
	public void insertCCreceiver(String email_code, String company_code, String ccReceiver) {
		HashMap<String, String> map = new HashMap<>();
		
		map.put("email_code", email_code);
		map.put("company_code", company_code);
		map.put("email_cc_receiver", ccReceiver);
		
		emailDAO.insertCCreceiver(map);
	}

	//보낸 메일함 불러오기
	@Override
	public ArrayList<Mailinfo> selectSentmail(String email_sender) {
		ArrayList<Mailinfo> mailinfo = emailDAO.selectSentmail(email_sender);
				
		return mailinfo;
	}
	
	//페이지 나누기
	@Override
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String email_sender) {
		//전체 글 개수
		int total = emailDAO.sentmailCount(email_sender);
		
		PageNavigator navi = new PageNavigator(pagePerGroup, countPerPage, page, total);
		return navi;
	}
	
	//전체 메일함
	@Override
	public ArrayList<Mailinfo> readAllmail(String email_receiver) {
		
		ArrayList<Mailinfo> mailinfo = emailDAO.readAllmail(email_receiver);
		
		return mailinfo;
	}

	@Override
	public Mailinfo selectOne(String email_code) {
		Mailinfo mailinfo = emailDAO.selectOne(email_code);
		
		return mailinfo;
	}













}
