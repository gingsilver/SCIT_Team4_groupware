package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DraftBox {
	String draft_code;
	String draft_title;
	String draft_writedate;
	String employee_name;
	String employee_code;
	String approver_code;
	String process_enabled;
	String process_type;
}
