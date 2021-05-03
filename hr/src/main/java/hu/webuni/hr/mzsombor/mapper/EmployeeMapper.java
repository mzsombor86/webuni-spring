package hu.webuni.hr.mzsombor.mapper;

import java.util.List;

import javax.validation.Valid;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.mzsombor.dto.EmployeeDto;
import hu.webuni.hr.mzsombor.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	@Mapping(source="position.name", target="title")
	@Mapping(source="company.name", target="companyName")
	List<EmployeeDto> employeesToDtos(List<Employee> employees);
	

	@Mapping(source="position.name", target="title")
	@Mapping(source="company.name", target="companyName")
	EmployeeDto employeeToDto(Employee employee);

	@Mapping(source = "title", target = "position.name")
	@Mapping(source = "companyName", target = "company.name")
	Employee dtoToEmployee(@Valid EmployeeDto employeeDto);

	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
	

}
