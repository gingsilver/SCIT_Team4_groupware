package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Email {
	String email_code;				//편지 번호
	String company_code;			//회사 구분 코드
	String email_title;				//메일 제목
	String email_content;			//메일 내용
	String email_sender;			//보내는 사람
	String[] email_receiver;		//받는 사람
	String[] email_cc_receiver;		//참조인
	String email_date;				//수송신시간
	String email_draft;				//임시보관여부 0 : 발송, 1: 임시보관
	String email_important;			//중요메일여부 0 : 보통 메일, 1 : 중요 메일
	String email_trash;				//휴지통 여부 0 : 보관 메일, 1: 휴지통 메일
	
}
