package hu.webuni.hr.mzsombor.web;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.mzsombor.dto.EmployeeDto;
import hu.webuni.hr.mzsombor.mapper.EmployeeMapper;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;
import hu.webuni.hr.mzsombor.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeMapper employeeMapper;

	@Autowired
	EmployeeRepository employeeRepository;

	// Az összes alkalmazott kilistázása
	@GetMapping
	public List<EmployeeDto> getAll() {
		return employeeMapper.employeesToDtos(employeeService.findAll());
	}

	// Egy alkalmazott kilistázása az ID-ja alapján
	@GetMapping("/{id}")
	public EmployeeDto getById(@PathVariable long id) {
		Employee employee = employeeService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return employeeMapper.employeeToDto(employee);

	}

	// Egy új alkalmazott hozzáadása
	@PostMapping
	public EmployeeDto createEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
		Employee employee = employeeService.save(employeeMapper.dtoToEmployee(employeeDto));
		return employeeMapper.employeeToDto(employee);
	}

	// Létező alkalmazott módosítása
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> modifyEmployee(@PathVariable long id,
			@RequestBody @Valid EmployeeDto employeeDto) {
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

	// Bizonyos titulusú alkalmazottak kilistázása
	@GetMapping(params = "title")
	public List<EmployeeDto> getByTitle(@RequestParam String title) {
		return employeeMapper.employeesToDtos(employeeRepository.findByTitle(title));
	}

	// Bizonyos névvel kezdődő alkalmazottak kilistázása
	@GetMapping(params = "name")
	public List<EmployeeDto> getByName(@RequestParam String name) {
		return employeeMapper.employeesToDtos(employeeRepository.findByNameStartingWithIgnoreCase(name));
	}

	// Bizonyos belépési dátumok között belépett alkalmazottak kilistázása
	@GetMapping(params = {"startDate","endDate"})
	public List<EmployeeDto> getByEntryDate(@RequestParam LocalDateTime startDate, @RequestParam LocalDateTime endDate) {
		return employeeMapper.employeesToDtos(employeeRepository.findByEntryDateBetween(startDate, endDate));
	}

}
