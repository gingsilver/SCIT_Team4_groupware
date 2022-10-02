package net.softsociety.Team4GroupWare.dao;

import java.util.HashMap;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.Team4GroupWare.domain.Salary;
import net.softsociety.Team4GroupWare.domain.Tax;

@Mapper
public interface ManagementDAO {
	//개인의 연봉정보 불러오기
	Salary seleteSalaryOne(String employee_code);

	//개인의 세금정보 불러오기
	Tax selectTaxInfo(HashMap<String, String> map);

}
