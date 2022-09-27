package net.softsociety.Team4GroupWare.dao;

import org.apache.ibatis.annotations.Mapper;

import net.softsociety.Team4GroupWare.domain.Employee;

@Mapper
public interface DraftDAO {

	public Employee readEmployee(String username);

}
