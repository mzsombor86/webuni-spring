package hu.webuni.hr.mzsombor.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;

public abstract class EmployeeService {

	@Autowired
	EmployeeRepository employeeRepository;
	
	
//	private Map<Long, Employee> employees = new HashMap<>();
//	{
//		employees.put(1L,
//				new Employee(1, "Sam Mendes", "CEO", 1_000_000, LocalDateTime.parse("1980-03-01T10:00:00")));
//		employees.put(2L, new Employee(2, "John Smith", "CTO", 500_000, LocalDateTime.parse("1990-03-01T10:00:00")));
//		employees.put(3L,
//				new Employee(3, "Angela Davidson", "CXO", 500_000, LocalDateTime.parse("2000-03-01T10:00:00")));
//		employees.put(4L,
//				new Employee(4, "Peter Knee", "developer", 300_000, LocalDateTime.parse("2010-03-01T10:00:00")));
//		employees.put(5L, new Employee(5, "Anthony Spacy", "adminstrative", 200_000,
//				LocalDateTime.parse("2015-03-01T10:00:00")));
//		employees.put(6L,
//				new Employee(6, "Richard Pearce", "associate", 200_000, LocalDateTime.parse("2018-09-01T10:00:00")));
//		employees.put(7L,
//				new Employee(7, "Megan Baker", "trainee", 100_000, LocalDateTime.parse("2020-09-01T10:00:00")));
//	}
	

	public abstract int getPayRaisePercent(Employee employee);


	public List<Employee> findAll() {
		return employeeRepository.findAll();
		//return new ArrayList<Employee>(employees.values());
	}
	
	public Optional<Employee> findById(long id) {
		return employeeRepository.findById(id);
		//return employees.get(id);
	}
	
	public List<Employee> findAboveASalary(int aboveSalary) {
		return employeeRepository.findBySalaryGreaterThan(aboveSalary);
		//return employees.values().stream().filter(e -> e.getSalary() > aboveSalary).collect(Collectors.toList());
	}
	
	@Transactional
	public Employee save(Employee employee) {
		return employeeRepository.save(employee);
	}

	@Transactional
	public void delete(long id) {
		employeeRepository.deleteById(id);
	}
	
	
	

}
