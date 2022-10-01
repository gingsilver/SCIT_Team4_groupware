package net.softsociety.Team4GroupWare.dao;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.Team4GroupWare.domain.Salary;

@Mapper
public interface ManagementDAO {
	//개인의 연봉정보 불러오기
	Salary seleteSalaryOne(String employee_code);

}
