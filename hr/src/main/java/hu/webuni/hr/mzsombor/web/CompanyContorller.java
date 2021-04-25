package hu.webuni.hr.mzsombor.web;

import java.util.List;
import java.util.NoSuchElementException;

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

import hu.webuni.hr.mzsombor.dto.AvgSalaryDto;
import hu.webuni.hr.mzsombor.dto.CompanyDto;
import hu.webuni.hr.mzsombor.dto.EmployeeDto;
import hu.webuni.hr.mzsombor.mapper.CompanyMapper;
import hu.webuni.hr.mzsombor.mapper.EmployeeMapper;
import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.repository.CompanyRepository;
import hu.webuni.hr.mzsombor.service.CompanyService;
import hu.webuni.hr.mzsombor.service.LegalFormService;

@RestController
@RequestMapping("/api/companies")
public class CompanyContorller {

	@Autowired
	CompanyService companyService;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	LegalFormService legalFormService;

	@Autowired
	CompanyMapper companyMapper;

	@Autowired
	EmployeeMapper employeeMapper;

	// Az összes cég kilistázása, full paraméter megléte esetén az alkalmazottak
	// adataival együtt.
	@GetMapping
	public List<CompanyDto> getAllCompanies(@RequestParam(required = false) String full) {
		List<Company> allCompanies = companyService.findAll();
		if (full == null || full.equals("false")) {
			return companyMapper.companiesToSummaryDtos(allCompanies);
		} else
			return companyMapper.companiesToDtos(allCompanies);
	}

	// Azon cégek kilistázása, melyek egy bizonyos fizetés feletti alkalmazottat
	// alkalmaznak.
	// Full paraméter megléte esetén az alkalmazottak adataival együtt.
	@GetMapping(params = "aboveSalary")
	public List<CompanyDto> getCompaniesAboveASalary(@RequestParam int aboveSalary,
			@RequestParam(required = false) String full) {
		List<Company> allCompanies = companyRepository.findWhereEmployeeSalaryIsGreaterThan(aboveSalary);
		if (full == null || full.equals("false")) {
			return companyMapper.companiesToSummaryDtos(allCompanies);
		} else
			return companyMapper.companiesToDtos(allCompanies);
	}

	// Azon cégek kilistázása, melyek egy bizonyos látszám feletti alkalmazottat
	// alkalmaznak.
	// Full paraméter megléte esetén az alkalmazottak adataival együtt.
	@GetMapping(params = "aboveEmployeeNumber")
	public List<CompanyDto> getCompaniesAboveEmployeeNumber(@RequestParam Long aboveEmployeeNumber,
			@RequestParam(required = false) String full) {
		List<Company> filteredCompanies = companyRepository.findWhereEmployeeNumberIsAbove(aboveEmployeeNumber);
		if (full == null || full.equals("false")) {
			return companyMapper.companiesToSummaryDtos(filteredCompanies);
		} else
			return companyMapper.companiesToDtos(filteredCompanies);
	}

	// Egy bizonyos cég kilistázása, full paraméter megléte esetén az alkalmazottak
	// adataival együtt.
	@GetMapping("/{id}")
	public CompanyDto getCompanyById(@PathVariable long id, @RequestParam(required = false) String full) {
		Company company = companyService.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		CompanyDto companyDto = null;
		if (full == null || full.equals("false"))
			companyDto = companyMapper.companyToSummaryDto(company);
		else
			companyDto = companyMapper.companyToDto(company);
		return companyDto;
	}

	// Egy adott vállalat alkalmazottainak átlagfizetése, titulusuk szerint
	// csoportosítva.
	@GetMapping(value = "/{id}", params = "avgSalaryByTitle")
	public List<AvgSalaryDto> getAverageSalariesByTitleAtACompany(@PathVariable long id,
			@RequestParam boolean avgSalaryByTitle) {
		if (avgSalaryByTitle) {
			companyService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
			return companyRepository.listAverageSalaryiesGroupedByTitlesAtACompany(id);
		}
		return null;
	}

	// Egy új cég hozzáadása
	@PostMapping
	public CompanyDto addCompany(@RequestBody CompanyDto companyDto) {
		Company company = companyMapper.dtoToCompany(companyDto);
		company.setLegalForm(legalFormService.findByForm(companyDto.getLegalForm())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY)));
		return companyMapper.companyToDto(companyService.save(company));
	}

	// Egy adott cég módosítása
	@PutMapping("/{registrationNumber}")
	public ResponseEntity<CompanyDto> modifyCompany(@PathVariable long registrationNumber,
			@RequestBody CompanyDto companyDto) {
		companyDto.setRegistrationNumber(registrationNumber);
		Company company = companyMapper.dtoToCompany(companyDto);
		company.setLegalForm(legalFormService.findByForm(companyDto.getLegalForm())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.FAILED_DEPENDENCY)));
		Company updatedCompany = companyService.update(company);
		if (updatedCompany == null)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok(companyMapper.companyToDto(companyService.save(updatedCompany)));
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
		try {
			return companyMapper.companyToDto(
					companyService.addEmployee(registrationNumber, employeeMapper.dtoToEmployee(employeeDto)));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	// Egy cég alkalmazottainak módosítása
	@PutMapping("/{registrationNumber}/employee")
	public CompanyDto modifyEmployeesOfACompany(@PathVariable long registrationNumber,
			@RequestBody List<EmployeeDto> employeeDtos) {
		try {
			return companyMapper.companyToDto(
					companyService.replaceEmployees(registrationNumber, employeeMapper.dtosToEmployees(employeeDtos)));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

	// Egy cég egy bizonyos alkalmazottjának törlése
	@DeleteMapping("/{registrationNumber}/employee/{id}")
	public CompanyDto deleteEmployeeFromACompany(@PathVariable long registrationNumber, @PathVariable long id) {
		try {
			return companyMapper.companyToDto(companyService.deleteEmployee(registrationNumber, id));
		} catch (NoSuchElementException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
