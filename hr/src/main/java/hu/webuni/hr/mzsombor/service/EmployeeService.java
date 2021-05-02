package hu.webuni.hr.mzsombor.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.model.Position;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;

public abstract class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	PositionService positionService;
	
	@Autowired
	CompanyService companyService;

	public abstract int getPayRaisePercent(Employee employee);

	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}

	public Optional<Employee> findById(long id) {
		return employeeRepository.findById(id);
	}

	public List<Employee> findAboveASalary(int aboveSalary) {
		return employeeRepository.findBySalaryGreaterThan(aboveSalary);
	}
	
	public Page<Employee> findAboveASalary(int aboveSalary, Pageable pageable) {
		return employeeRepository.findBySalaryGreaterThan(aboveSalary, pageable);
	}

	public List<Employee> findByPosition(String title) {
		List<Position> positions = positionService.findByName(title);
		List<Employee> employees = new ArrayList<>();
		for (Position position : positions) {
			employees.addAll(employeeRepository.findByPosition(position));
		}
		return employees;
		
	}

	public List<Employee> findByName(String name) {
		return employeeRepository.findByNameStartingWithIgnoreCase(name);
	}

	public List<Employee> findByEntryDate(LocalDateTime startDate, LocalDateTime endDate) {
		return employeeRepository.findByEntryDateBetween(startDate, endDate);
	}

	@Transactional
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}
	
	@Transactional
	public Employee addEmployee(Employee employee, String companyName, String positionName) {
		Company company = companyService.findByName(companyName).get();
		company.addEmployee(employee);
		employee.setCompany(company);
		Position position = positionService.findByNameAndCompany(positionName, company).get();
		position.addEmployee(employee);
		employee.setPosition(position);
		return employeeRepository.save(employee);
	}
	
	@Transactional
	public Employee updateEmployee(Employee employee) {
		if (!employeeRepository.existsById(employee.getId()))
			throw new NoSuchElementException();
		return employeeRepository.save(employee);
	}
	

	@Transactional
	public void delete(long id) {
		employeeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		employeeRepository.deleteById(id);
	}

	@Transactional
	public void deleteAll() {
		employeeRepository.deleteAll();
	}

}
