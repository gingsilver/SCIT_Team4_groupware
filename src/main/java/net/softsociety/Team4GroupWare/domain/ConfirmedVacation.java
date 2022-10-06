package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmedVacation {
	String company_code;
	String employee_code;
	String vacation_code;
	String confirmed_vacation_date;
	String confirmed_vacation_Enddate;
	int confirmed_vacation_days;
	String confirmed_vacation_type;
	String confirmed_vacation_reason;
}
