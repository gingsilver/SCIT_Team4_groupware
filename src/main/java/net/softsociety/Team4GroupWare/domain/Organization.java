package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organization {
	String company_code;
	String department_id;
	String parent_id;
	String department_name;
	String DEPT_NAME;
	String PATH;
	String LEVEL;
	String open;
}
