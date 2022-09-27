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
public class Schedule {
	int 	schedule_num;	
	int 	schedule_type; // 0이면 개인일정, 1이면 회사일정
	String 	company_code;		
	String 	schedule_writer_code;			
	String 	schedule_writer_id;		
	String 	schedule_title;		
	String 	schedule_contents;			
	String 	schedule_startDate;	
	String 	schedule_endDate;		
	String	schedule_allDay;
	String	schedule_textColor;
	String	schedule_backgroundColor;
	String	schedule_borderColor;
	

}
