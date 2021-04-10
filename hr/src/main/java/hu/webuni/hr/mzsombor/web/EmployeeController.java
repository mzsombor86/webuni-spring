package hu.webuni.hr.mzsombor.web;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.mzsombor.dto.EmployeeDto;
import hu.webuni.hr.mzsombor.mapper.EmployeeMapper;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	EmployeeMapper employeeMapper;


	// Az összes alkalmazott kilistázása
	@GetMapping
	public List<EmployeeDto> getAll() {
		return employeeMapper.employeesToDtos(employeeService.findAll());
	}

	// Egy alkalmazott kilistázása az ID-ja alapján
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getById(@PathVariable long id) {
		Employee employee = employeeService.findById(id);
		if (employee != null)
			return ResponseEntity.ok(employeeMapper.employeeToDto(employee));
		else
			return ResponseEntity.notFound().build();
	}

	// Egy új alkalmazott hozzáadása
	@PostMapping
	public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
		Employee employee = employeeService.save(employeeMapper.dtoToEmployee(employeeDto));
		return employeeMapper.employeeToDto(employee);
	}

	// Létező alkalmazott módosítása
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> modifyEmployee(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {
		if (employeeService.findById(id) == null)
			return ResponseEntity.notFound().build();

		employeeDto.setId(id);
		Employee employee = employeeService.save(employeeMapper.dtoToEmployee(employeeDto));
		return ResponseEntity.ok(employeeMapper.employeeToDto(employee));
	}

	// Alkamlmazott törlése
	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {
		employeeService.delete(id);
	}

	// Bizonyos fizetés fölötti alkalmazottak kilistázása
	@GetMapping(params = "aboveSalary")
	public List<EmployeeDto> getAboveASalary(@RequestParam int aboveSalary) {
		return employeeMapper.employeesToDtos(employeeService.findAboveASalary(aboveSalary));
	}

	// Beküldött alkalmazott fizetésemelésének mértékének meghatározása.
	@PostMapping("/getraisepercentage")
	public int getSalaryRaisePrecentage(@RequestBody EmployeeDto employeeDto) {
		return employeeService.getPayRaisePercent(employeeMapper.dtoToEmployee(employeeDto));
	}

}
