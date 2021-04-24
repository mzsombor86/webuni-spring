package hu.webuni.hr.mzsombor.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;

public abstract class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;

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

	public List<Employee> findByTitle(String title) {
		return employeeRepository.findByTitle(title);
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
	public Employee update(Employee employee) {
		if (!employeeRepository.existsById(employee.getId()))
			return null;
		return employeeRepository.save(employee);
	}
	

	@Transactional
	public void delete(long id) {
		employeeRepository.deleteById(id);
	}

	@Transactional
	public void deleteAll() {
		employeeRepository.deleteAll();
	}

}
