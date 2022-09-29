package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DraftApprover {
	String draft_code;
	String approver_code;
	String employee_code;
	String approver_name;
	String process_type;
	String process_turn_code;
	int process_enabled;
	String approver_date;

}
