package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachedFile {
	String company_code;
	String attached_file_code;
	String document_code;
	String attached_file_origin_name;
	String attached_file_save_name;
	String attached_file_ext;
	String attached_file_date;
	String employee_code;
	String employee_id;
	String employee_name;
}
