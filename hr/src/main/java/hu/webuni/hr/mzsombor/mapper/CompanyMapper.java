package hu.webuni.hr.mzsombor.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.mzsombor.dto.CompanyDto;
import hu.webuni.hr.mzsombor.dto.EmployeeDto;
import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.model.Employee;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	List<CompanyDto> companiesToDtos(List<Company> company);

	@Mapping(target = "*", qualifiedByName = "summary")
	List<CompanyDto> companiesToSummaryDtos(List<Company> company);

	@Mapping(target = "employees", ignore = true)
	@Named("summary")
	CompanyDto companyToSummaryDto(Company company);

	CompanyDto companyToDto(Company company);

	Company dtoToCompany(CompanyDto companyDto);

	@Mapping(source = "company.name", target = "companyName")
	EmployeeDto employeeToDto(Employee employee);

}
