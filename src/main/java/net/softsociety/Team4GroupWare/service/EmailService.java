package net.softsociety.Team4GroupWare.service;

import java.util.ArrayList;

import org.springframework.web.multipart.MultipartFile;

import net.softsociety.Team4GroupWare.domain.AttachedFile;
import net.softsociety.Team4GroupWare.domain.Email;
import net.softsociety.Team4GroupWare.domain.MailProcess;
import net.softsociety.Team4GroupWare.domain.Mailinfo;
import net.softsociety.Team4GroupWare.util.PageNavigator;

public interface EmailService {

	/**
	 * 메일함에 메일 저장하기
	 * @param email '메일 쓰기'에서 작성한 메일 내용
	 * @param upload '메일 쓰기'에서 첨부한 첨부파일
	 * @param mail_process '메일 쓰기'에서 설정한 받는 사람, 참조인
	 * @return 메일 발송 성공 여부
	 * @throws Exception void랑 int만 받을 수 있음
	 */
	public int sendMailWithFiles(Email email, MultipartFile upload, MailProcess mail_process) throws Exception;

	/**
	 * 임시저장
	 * @param email 메일 내용과 작성자만 임시 저장
	 * 받는 사람 및 참조인까지 저장시 불러오기에 오류 발생할 가능성 높으로 메일 내용
	 */
	public void sendMaildraft(Email email);

	/**
	 * 첨부파일 저장하기
	 * @param file
	 * @return 
	 */
	public int insertMailAtteched(AttachedFile file);
	
	/**
	 * 수신인 저장
	 * @param email_code
	 * @param company_code
	 * @param receiver
	 */
	public void insertReceiver(String email_code, String company_code, String receiver);

	/**
	 * 참조인 저장
	 * @param email_code
	 * @param company_code
	 * @param ccReceiver
	 */
	public void insertCCreceiver(String email_code, String company_code, String ccReceiver);

	/**
	 * 보낸 메일함
	 * @param email_sender
	 * @return Mailinfo에 모든 정보를 담아서 가져옴
	 */
	public ArrayList<Mailinfo> selectSentmail(String email_sender);

	/**
	 * 보낸 메일함 페이지 수 나누기
	 * @param pagePerGroup
	 * @param countPerPage
	 * @param page
	 * @param email_sender
	 * @return 전체 글 개수
	 */
	public PageNavigator getPageNavigator(int pagePerGroup, int countPerPage, int page, String email_sender);

	/**
	 * 전체 메일함
	 * @param email_receiver 수신인
	 * @param email_cc_receiver 참조인
	 * @return '수신인', '참조인'으로서 받은 메일 
	 */
	public ArrayList<Mailinfo> readAllmail(String email_receiver);

	/**
	 * 메일 1개 읽어오기
	 * @param email_code 메일 번호
	 * @return mailinfo 메일 정보
	 */
	public Mailinfo selectOne(String email_code);

	/**
	 * 메일 1개 읽기의 첨부파일 불러오기
	 * @param email_code
	 * @return attachedfile
	 */
	public AttachedFile MailAttachedfile(String email_code);

	/**
	 * insert : 메일 저장
	 * @param email
	 * @return
	 */
	public int insertMailTotal(Email email);

	/**
	 * insert : 메일 프로세스 저장
	 * @param mail_process
	 * @return
	 */
	public int insertMailProcess(MailProcess mail_process);

	
}
