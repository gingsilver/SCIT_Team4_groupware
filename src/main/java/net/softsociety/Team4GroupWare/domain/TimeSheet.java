package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSheet {
	String company_code;
	String employee_code;
	String time_sheet_code;
	String time_sheet_start;
	String time_sheet_end;
	String time_sheet_overtime;
	
}
