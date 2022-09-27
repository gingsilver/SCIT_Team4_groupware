package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailProcess {
	String email_code;				//편지 번호
	String company_code;			//회사 구분 코드
	String[] email_receiver;		//받는 사람
	String[] email_cc_receiver;		//참조인
}
