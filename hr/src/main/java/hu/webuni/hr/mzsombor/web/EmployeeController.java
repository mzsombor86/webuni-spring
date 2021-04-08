package hu.webuni.hr.mzsombor.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	private Map<Long, EmployeeDto> employees = new HashMap<>();
	{
		employees.put(1L,
				new EmployeeDto(1, "Sam Mendes", "CEO", 1_000_000, LocalDateTime.parse("1980-03-01T10:00:00")));
		employees.put(2L, new EmployeeDto(2, "John Smith", "CTO", 500_000, LocalDateTime.parse("1990-03-01T10:00:00")));
		employees.put(3L,
				new EmployeeDto(3, "Angela Davidson", "CXO", 500_000, LocalDateTime.parse("2000-03-01T10:00:00")));
		employees.put(4L,
				new EmployeeDto(4, "Peter Knee", "developer", 300_000, LocalDateTime.parse("2010-03-01T10:00:00")));
		employees.put(5L, new EmployeeDto(5, "Anthony Spacy", "adminstrative", 200_000,
				LocalDateTime.parse("2015-03-01T10:00:00")));
		employees.put(6L,
				new EmployeeDto(6, "Richard Pearce", "associate", 200_000, LocalDateTime.parse("2018-09-01T10:00:00")));
		employees.put(7L,
				new EmployeeDto(7, "Megan Baker", "trainee", 100_000, LocalDateTime.parse("2020-09-01T10:00:00")));
	}

	// Az összes alkalmazott kilistázása
	@GetMapping
	public List<EmployeeDto> getAll() {
		return new ArrayList<>(employees.values());
	}

	// Egy alkalmazott kilistázása az ID-ja alapján
	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getById(@PathVariable long id) {
		EmployeeDto employeeDto = employees.get(id);
		if (employeeDto != null)
			return ResponseEntity.ok(employeeDto);
		else
			return ResponseEntity.notFound().build();
	}

	// Egy új alkalmazott hozzáadása
	@PostMapping
	public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employee) {
		employees.put(employee.getId(), employee);
		return employee;
	}

	// Létező alkalmazott módosítása
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> modifyEmployee(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {
		if (!employees.containsKey(id))
			return ResponseEntity.notFound().build();

		employeeDto.setId(id);
		employees.put(id, employeeDto);
		return ResponseEntity.ok(employeeDto);
	}

	// Alkamlmazott törlése
	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {
		employees.remove(id);
	}

	// Bizonyos fizetés fölötti alkalmazottak kilistázása
	@GetMapping(params = "aboveSalary")
	public List<EmployeeDto> getAboveASalary(@RequestParam int aboveSalary) {
		return employees.values().stream().filter(e -> e.getSalary() > aboveSalary).collect(Collectors.toList());
	}

	// Beküldött alkalmazott fizetésemelésének mértékének meghatározása.
	@PostMapping("/getraisepercentage")
	public int getSalaryRaisePrecentage(@RequestBody Employee employee) {
		return employeeService.getPayRaisePercent(employee);
	}

}
