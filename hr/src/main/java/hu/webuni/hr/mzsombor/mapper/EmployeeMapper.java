package hu.webuni.hr.mzsombor.mapper;

import java.util.List;

import javax.validation.Valid;

import org.mapstruct.Mapper;

import hu.webuni.hr.mzsombor.dto.EmployeeDto;
import hu.webuni.hr.mzsombor.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	List<EmployeeDto> employeesToDtos(List<Employee> findAll);

	EmployeeDto employeeToDto(Employee employee);

	Employee dtoToEmployee(@Valid EmployeeDto employeeDto);

	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
	

}
