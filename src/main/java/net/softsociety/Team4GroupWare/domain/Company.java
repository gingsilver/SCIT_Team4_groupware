package net.softsociety.Team4GroupWare.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Company {
	String company_code; 
	String company_name; 
	String company_business_num;  
	String company_est_date; 
	String company_ceo_name;
	String company_postcode;
	String company_address;
	String company_detailaddress;
	String company_extraaddress;
	String company_logo;
}