package hu.webuni.hr.mzsombor.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.mzsombor.dto.EmployeeDto;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

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

	@GetMapping
	public List<EmployeeDto> getAll() {
		return new ArrayList<>(employees.values());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getById(@PathVariable long id) {
		EmployeeDto employeeDto = employees.get(id);
		if (employeeDto != null)
			return ResponseEntity.ok(employeeDto);
		else
			return ResponseEntity.notFound().build();
	}

	@PostMapping
	public EmployeeDto createEmployee(@RequestBody EmployeeDto airportDto) {
		employees.put(airportDto.getId(), airportDto);
		return airportDto;
	}

	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> modifyEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {
		if (!employees.containsKey(id))
			return ResponseEntity.notFound().build();

		employeeDto.setId(id);
		employees.put(id, employeeDto);
		return ResponseEntity.ok(employeeDto);
	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {
		employees.remove(id);
	}

	@RequestMapping(method = RequestMethod.GET, params = "aboveSalary")
	public List<EmployeeDto> getAboveASalary(@RequestParam(name = "aboveSalary") int salary) {
		return new ArrayList<>(
				employees.values().stream().filter(e -> e.getSalary() > salary).collect(Collectors.toList()));
	}
}
