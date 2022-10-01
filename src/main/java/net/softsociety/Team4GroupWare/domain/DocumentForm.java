package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentForm {
	String company_code;
	String document_form_code;
	String document_form_type;
	String document_form_name;
	String document_form_title;
	String document_form_contents;
	String final_approver_code;
	String final_approver_name;
	String process_type;
}
