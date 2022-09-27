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
public class FreeBoard {
	String 	free_code;		
	String 	company_code;		
	String 	employee_code;			
	String 	employee_id;		
	String 	free_title;		
	String 	free_contents;			
	String 	hitcounts;	
	String 	inputdate;		
	String	updatedate;
	String	category;

}
