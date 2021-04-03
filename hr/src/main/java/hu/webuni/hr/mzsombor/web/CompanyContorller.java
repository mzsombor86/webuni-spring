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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.mzsombor.dto.CompanyDto;
import hu.webuni.hr.mzsombor.dto.EmployeeDto;

@RestController
@RequestMapping("/api/companies")
public class CompanyContorller {

	private List<EmployeeDto> employees = new ArrayList<>();
	private Map<Long, CompanyDto> companies = new HashMap<>();
	{
		employees.add(new EmployeeDto(1, "Sam Mendes", "CEO", 1_000_000, LocalDateTime.parse("1980-03-01T10:00:00")));
		employees.add(new EmployeeDto(2, "John Smith", "CTO", 500_000, LocalDateTime.parse("1990-03-01T10:00:00")));
		employees
				.add(new EmployeeDto(3, "Angela Davidson", "CXO", 500_000, LocalDateTime.parse("2000-03-01T10:00:00")));
		employees.add(
				new EmployeeDto(4, "Peter Knee", "developer", 300_000, LocalDateTime.parse("2010-03-01T10:00:00")));
		employees.add(new EmployeeDto(5, "Anthony Spacy", "adminstrative", 200_000,
				LocalDateTime.parse("2015-03-01T10:00:00")));
		employees.add(
				new EmployeeDto(6, "Richard Pearce", "associate", 200_000, LocalDateTime.parse("2018-09-01T10:00:00")));
		employees
				.add(new EmployeeDto(7, "Megan Baker", "trainee", 100_000, LocalDateTime.parse("2020-09-01T10:00:00")));

		companies.put(1L, new CompanyDto(1, "JavaWorks", "1111 Budapest, Java Street 1.", employees));
		companies.put(2L, new CompanyDto(2, "SpringWorks", "2222 Budapest, Spring Street 1.", employees));
		companies.put(3L, new CompanyDto(3, "HtmlWorks", "3333 Budapest, Html Street 1.", employees));
		companies.put(4L, new CompanyDto(4, "CSSWorks", "4444 Budapest, CSS Street 1.", employees));
		companies.put(5L, new CompanyDto(5, "NodeJSWorks", "5555 Budapest, NodeJS Street 1.", employees));
	}

	// Az összes cég kilistázása, full paraméter megléte esetén az alkalmazottak
	// adataival együtt.
	@GetMapping
	public List<CompanyDto> getAllCompanies(@RequestParam(required = false) String full) {
		if (full == null || full.equals("false"))
			return companies.values().stream().map(c -> new CompanyDto(c.getRegistrationNumber(), c.getName(),
					c.getAddress(), new ArrayList<EmployeeDto>())).collect(Collectors.toList());
		else
			return companies.values().stream().collect(Collectors.toList());
	}

	// Egy bizonyos cég kilistázása, full paraméter megléte esetén az alkalmazottak
	// adataival együtt.
	@GetMapping("/{id}")
	public ResponseEntity<CompanyDto> getCompanyById(@PathVariable long id,
			@RequestParam(required = false) String full) {
		CompanyDto companyDto = companies.get(id);
		if (companyDto != null) {
			if (full == null || full.equals("false")) {
				return ResponseEntity.ok(new CompanyDto(companyDto.getRegistrationNumber(), companyDto.getName(),
						companyDto.getAddress(), new ArrayList<EmployeeDto>()));
			} else {
				return ResponseEntity.ok(companyDto);
			}
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Egy új cég hozzáadása
	@PostMapping
	public CompanyDto addCompany(@RequestBody CompanyDto companyDto) {
		companies.put(companyDto.getRegistrationNumber(), companyDto);
		return companyDto;
	}

	// Egy adott cég módosítása
	@PutMapping("/{registrationNumber}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long registrationNumber,
			@RequestBody CompanyDto CompanyDto) {
		if (!companies.containsKey(registrationNumber))
			return ResponseEntity.notFound().build();

		CompanyDto.setRegistrationNumber(registrationNumber);
		companies.put(registrationNumber, CompanyDto);
		return ResponseEntity.ok(CompanyDto);
	}

	// Adott cég törlése
	@DeleteMapping("/{registrationNumber}")
	public void deleteCompany(@PathVariable long registrationNumber) {
		companies.remove(registrationNumber);
	}

	// Alkalmazott hozzáadása egy céghez
	@PostMapping("/{registrationNumber}/addemployee")
	public CompanyDto addEmployeeToACompany(@PathVariable long registrationNumber,
			@RequestBody EmployeeDto employeeDto) {
		companies.get(registrationNumber).addEmployee(employeeDto);
		return companies.get(registrationNumber);
	}

	// Egy cég alkalmazottainak módosítása
	@PutMapping("/{registrationNumber}/modifyemployees")
	public ResponseEntity<CompanyDto> modifyEmployeesOfACompany(@PathVariable long registrationNumber,
			@RequestBody List<EmployeeDto> employees) {
		if (!companies.containsKey(registrationNumber))
			return ResponseEntity.notFound().build();
		companies.get(registrationNumber).setEmployees(employees);
		return ResponseEntity.ok(companies.get(registrationNumber));
	}

	// Egy cég egy bizonyos alkalmazottjának törlése
	@DeleteMapping("/{registrationNumber}/deleteemployee/{id}")
	public void deleteEmployeeFromACompany(@PathVariable long registrationNumber, @PathVariable long id) {
		companies.get(registrationNumber).deleteEmployee(id);
	}

}
