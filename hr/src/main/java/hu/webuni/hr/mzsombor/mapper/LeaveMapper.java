package hu.webuni.hr.mzsombor.mapper;

import java.util.List;

import javax.validation.Valid;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.mzsombor.dto.LeaveDto;
import hu.webuni.hr.mzsombor.model.Leave;

@Mapper(componentModel = "spring")
public interface LeaveMapper {

	
	List<LeaveDto> leavesToDtos(List<Leave> leaves);	
	
	@Mapping(source = "employee.id", target = "employeeId")
	@Mapping(source = "approver.id", target = "approverId")	
	LeaveDto leaveToDto(Leave leave);

	@Mapping(target = "employee", ignore = true)
	@Mapping(target = "approver", ignore = true)
	Leave dtoToLeave(@Valid LeaveDto leaveDto);

	List<Leave> dtosToLeaves(List<LeaveDto> leaveDtos);
	
//	@Mapping(source = "company.name", target = "companyName")
//	@Mapping(source = "position.name", target = "title")
//	EmployeeDto employeeToDto(Employee employee);
//	
//	@Mapping(source = "title", target = "position.name")
//	@Mapping(source = "companyName", target = "company.name")
//	@Mapping(target = "leaves", ignore = true)
//	Employee dtoToEmployee(@Valid EmployeeDto employeeDto);
	

}
