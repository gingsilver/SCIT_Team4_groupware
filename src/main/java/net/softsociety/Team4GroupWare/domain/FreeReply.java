package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 게시글 정보
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FreeReply {
	String 	free_code;				
	String 	employee_code;			
	String 	employee_id;
	String  reply_num;
	String 	reply_contents;			
	String 	inputdate;		
	String	updatedate;

}
