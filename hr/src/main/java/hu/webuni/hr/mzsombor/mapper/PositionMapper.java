package hu.webuni.hr.mzsombor.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.mzsombor.dto.PositionDto;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.model.Position;

@Mapper(componentModel = "spring")
public interface PositionMapper {
	
	@Mapping(source = "company.name", target="company")
	@Mapping(target = "employees", ignore = true)
	PositionDto positionToDto(Position position);
	
	List<PositionDto> positionsToDtos(List<Position> positions);
	
	@Mapping(target = "company", ignore = true)
	@Mapping(target = "employees", ignore = true)
	Position dtoToPosition(PositionDto positionDto);

	List<Position> dtosToPositions(List<PositionDto> positionDtos);
		
	default String employeeToString(Employee employee) {
		return employee.getName();
	};
}
