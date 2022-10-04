package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Draft {
	String company_code;
	String draft_code;
	String employee_code;
	String employee_name;
	String employee_organization;
	String draft_title;
	String draft_contents;
	String draft_inputdate;
	String draft_duedate;
	String process_type;
	String draft_saved;
	String process_enabled;
}
