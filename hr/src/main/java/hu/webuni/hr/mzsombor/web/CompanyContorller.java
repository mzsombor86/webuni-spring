package hu.webuni.hr.mzsombor.web;

import java.util.List;
import java.util.stream.Collectors;

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

import hu.webuni.hr.mzsombor.dto.CompanyDto;
import hu.webuni.hr.mzsombor.dto.EmployeeDto;
import hu.webuni.hr.mzsombor.mapper.CompanyMapper;
import hu.webuni.hr.mzsombor.mapper.EmployeeMapper;
import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.service.CompanyService;

@RestController
@RequestMapping("/api/companies")
public class CompanyContorller {

	@Autowired
	CompanyService companyService;

	@Autowired
	CompanyMapper companyMapper;

	@Autowired
	EmployeeMapper employeeMapper;

	// Az összes cég kilistázása, full paraméter megléte esetén az alkalmazottak
	// adataival együtt.
	@GetMapping
	public List<CompanyDto> getAllCompanies(@RequestParam(required = false) String full) {
		if (full == null || full.equals("false")) {
			return companyService.findAll().stream()
					.map(c -> companyMapper.companyToDto(c))
					.map(c -> {
						c.setEmployees(null);
						return c;
						})
					.collect(Collectors.toList());
		} else
			return companyMapper.companiesToDtos(companyService.findAll());
	}

	// Egy bizonyos cég kilistázása, full paraméter megléte esetén az alkalmazottak
	// adataival együtt.
	@GetMapping("/{id}")
	public ResponseEntity<CompanyDto> getCompanyById(@PathVariable long id,
			@RequestParam(required = false) String full) {
		Company company = companyService.findById(id);
		if (company != null) {
			CompanyDto companyDto = companyMapper.companyToDto(company);
			if (full == null || full.equals("false"))
				companyDto.setEmployees(null);	
			return ResponseEntity.ok(companyDto);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	// Egy új cég hozzáadása
	@PostMapping
	public CompanyDto addCompany(@RequestBody CompanyDto companyDto) {
		return companyMapper.companyToDto(companyService.save(companyMapper.dtoToCompany(companyDto)));
	}

	// Egy adott cég módosítása
	@PutMapping("/{registrationNumber}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long registrationNumber,
			@RequestBody CompanyDto companyDto) {
		if (companyService.findById(registrationNumber) == null)
			return ResponseEntity.notFound().build();

		companyDto.setRegistrationNumber(registrationNumber);
		companyService.save(companyMapper.dtoToCompany(companyDto));
		return ResponseEntity.ok(companyDto);
	}

	// Adott cég törlése
	@DeleteMapping("/{registrationNumber}")
	public void deleteCompany(@PathVariable long registrationNumber) {
		companyService.delete(registrationNumber);
	}

	// Alkalmazott hozzáadása egy céghez
	@PostMapping("/{registrationNumber}/employee")
	public CompanyDto addEmployeeToACompany(@PathVariable long registrationNumber,
			@RequestBody EmployeeDto employeeDto) {

		Company company = companyService.findById(registrationNumber);
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		company.getEmployees().add(employeeMapper.dtoToEmployee(employeeDto));
		return companyMapper.companyToDto(companyService.findById(registrationNumber));
	}

	// Egy cég alkalmazottainak módosítása
	@PutMapping("/{registrationNumber}/employee")
	public ResponseEntity<CompanyDto> modifyEmployeesOfACompany(@PathVariable long registrationNumber,
			@RequestBody List<EmployeeDto> employeeDtos) {

		Company company = companyService.findById(registrationNumber);
		if (company == null)
			return ResponseEntity.notFound().build();
		company.setEmployees(employeeMapper.dtosToEmployees(employeeDtos));
		return ResponseEntity.ok(companyMapper.companyToDto(company));
	}

	// Egy cég egy bizonyos alkalmazottjának törlése
	@DeleteMapping("/{registrationNumber}/employee/{id}")
	public void deleteEmployeeFromACompany(@PathVariable long registrationNumber, @PathVariable long id) {
		Company company = companyService.findById(registrationNumber);
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		company.getEmployees().removeIf(e -> e.getId() == id);		
	}

}
