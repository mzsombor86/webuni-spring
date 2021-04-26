package hu.webuni.hr.mzsombor.web;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.mzsombor.dto.PositionDto;
import hu.webuni.hr.mzsombor.mapper.PositionMapper;
import hu.webuni.hr.mzsombor.model.Position;
import hu.webuni.hr.mzsombor.service.CompanyService;
import hu.webuni.hr.mzsombor.service.PositionService;

@RestController
@RequestMapping("/api/positions")
public class PositionController {

	@Autowired
	PositionService positionService;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	PositionMapper positionMapper;
	
	@GetMapping
	public List<PositionDto> getPositions() {
		return positionMapper.positionsToDtos(positionService.findAll());
	}
	
	@GetMapping(params="name")
	public List<PositionDto> getPositionsByName(String name) {
		return positionMapper.positionsToDtos(positionService.findByName(name));
	}
	
	@GetMapping("/raiseMinSalary")
	public List<PositionDto> raiseMinSalary(@RequestParam int minSalary, @RequestParam String positionName, @RequestParam(required = false) Long registrationNumber) {
		List<PositionDto> positionDtos;
		if (registrationNumber == null) {
			positionDtos = positionMapper.positionsToDtos(positionService.setMinSalaryAndRaiseSalaryToMinByPositionName(minSalary,positionName));
		} else {
			positionDtos = new ArrayList<>();
			positionDtos.add(positionMapper.positionToDto(positionService.setMinSalaryAndRaiseSalaryToMinByPositionNameAndCompaniId(minSalary,positionName,registrationNumber)));
		}
		return positionDtos;
	}
	
	@PostMapping
	public PositionDto addPosition(@RequestBody PositionDto positionDto) {
		Position position = positionMapper.dtoToPosition(positionDto);
		if (positionDto.getCompany() != null)
			position.setCompany(companyService.findByName(positionDto.getCompany()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
		return positionMapper.positionToDto(positionService.save(position));
	}
	
	@PutMapping("/{id}")
	public PositionDto changePosition(@RequestBody PositionDto positionDto, @PathVariable long id ) {
		positionService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		positionDto.setId(id);
		Position position = positionMapper.dtoToPosition(positionDto);
		if (positionDto.getCompany() != null)
			position.setCompany(companyService.findByName(positionDto.getCompany()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
		return positionMapper.positionToDto(positionService.update(position));
	}
	
	@DeleteMapping("/{id}")
	public void deletePosition(@PathVariable long id) {
		positionService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		positionService.delete(id);
	}
}
