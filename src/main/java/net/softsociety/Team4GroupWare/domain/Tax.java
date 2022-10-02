package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tax {
	String company_code;
	String employee_code;
	String saraly_code;
	
	String tax_code;
	String tax_year;
	int tax_income;
	int tax_inhabitants;
	int tax_national;
	int tax_health;
	int tax_employment;
}
