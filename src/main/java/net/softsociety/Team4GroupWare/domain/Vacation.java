package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vacation {
	String company_code;
	String employee_code;

	String vacation_year;
	int vacation_annual_all;
	int vacation_annual_minus;
	int vacation_sick;
	int vacation_army;
	int vacation_occation;
	int vacation_regular;
}
