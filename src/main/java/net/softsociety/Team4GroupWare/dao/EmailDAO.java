package net.softsociety.Team4GroupWare.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Email;
import net.softsociety.Team4GroupWare.domain.MailProcess;
import net.softsociety.Team4GroupWare.domain.Mailinfo;

@Mapper
public interface EmailDAO {

	//메일 작성 내용 저장
	public int sendToMailbox(Email email);

	//첨부파일 저장
	public int insertMailAtteched(AttachedFile file);
	
	//임시 저장
	public void sendMaildraft(Email email);

	//수신인 저장
	public void insertReceiver(HashMap<String, String> map);
	
	//참조인 저장
	public void insertCCreceiver(HashMap<String, String> map);

	//보낸 메일함
	public ArrayList<Mailinfo> selectSentmail(String email_sender);

	//보낸 메일함 페이지수 나누기
	public int sentmailCount(String email_sender);

	//전체 메일함
	public ArrayList<Mailinfo> readAllmail(String email_receiver);

	//메일 1개 불러오기 - 메일내용
	public Mailinfo selectOne(String email_code);

	//메일 1개 불러오기 - 첨부파일
	public AttachedFile MailAttachedfile(String email_code);

	//메일저장
	public int insertMailTotal(Email email);

	//메일프로세스저장
	public int insertMailProcess(MailProcess mail_process);


}
