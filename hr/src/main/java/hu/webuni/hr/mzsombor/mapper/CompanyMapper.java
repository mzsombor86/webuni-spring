package hu.webuni.hr.mzsombor.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.mzsombor.dto.CompanyDto;
import hu.webuni.hr.mzsombor.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	List<CompanyDto> companiesToDtos(List<Company> company);

	CompanyDto companyToDto(Company company);

	Company dtoToCompany(CompanyDto companyDto);

}
