package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Salary {
	String company_code;
	String employee_code;
	String salary_code;
	int salary_retirement;
	int salary_management;
	int	saraly_regular_bonus;
	String salary_date;
	int salary_basic;
	int salary_incentive;
	int salary_fixed_overtime;
	int salary_approved_overtime;
	int salary_holiday;
	int salary_meal;
	String salary_role;
}
