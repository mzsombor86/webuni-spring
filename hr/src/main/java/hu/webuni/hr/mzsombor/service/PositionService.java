package hu.webuni.hr.mzsombor.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.model.Position;
import hu.webuni.hr.mzsombor.repository.CompanyRepository;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;
import hu.webuni.hr.mzsombor.repository.PositionRepository;

@Service
public class PositionService {

	@Autowired
	PositionRepository positionRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	CompanyService companyService;

	public List<Position> findAll() {
		return positionRepository.findAll();
	}

	public Optional<Position> findById(long id) {
		return positionRepository.findById(id);
	}

	public List<Position> findByName(String name) {
		return positionRepository.findByName(name);
	}

	public Optional<Position> findByNameAndCompany(String name, Company company) {
		return positionRepository.findByNameAndCompany(name, company);
	}

	@Transactional
	public Employee addEmployee(Long id, Employee employee) {
		Position position = positionRepository.findById(id).get();
		position.addEmployee(employee);
		return employeeRepository.save(employee);
	}

	@Transactional
	public Position save(Position position) {
		return positionRepository.save(position);
	}
	
	@Transactional
	public Position addPosition(Position position, String companyName) {
		if (companyName != null)
			position.setCompany(companyService.findByName(companyName).orElseThrow(() -> new NoSuchElementException()));
		return save(position);
		
	}

	@Transactional
	public Position updatePosition(Position position) {
		Position oldPosition = findById(position.getId()).get();
		position.setCompany(oldPosition.getCompany());
		position.setEmployees(oldPosition.getEmployees());
		return positionRepository.save(position);
	}

	@Transactional
	public void delete(long id) {
		Position position = positionRepository.findById(id).get();
		position.setCompany(null);
		position.getEmployees().stream().forEach(e -> e.setPosition(null));
		positionRepository.save(position);
		positionRepository.deleteById(id);
	}

	public List<Position> setMinSalaryAndRaiseSalaryToMinByPositionName(int minSalary, String positionName) {
		List<Position> positions = positionRepository.findByName(positionName);
		for (Position position : positions) {
			updatePositionMinSalary(position, minSalary);
		}
		return positions;
	}

	@Transactional
	public Position setMinSalaryAndRaiseSalaryToMinByPositionNameAndCompaniId(int minSalary, String positionName,
			Long registrationNumber) {
		Company company = companyService.findByIdFull(registrationNumber).get();
		Position position = positionRepository.findByNameAndCompany(positionName, company).get();
		updatePositionMinSalary(position, minSalary);
		return position;
	}

	@Transactional
	private void updatePositionMinSalary(Position position, int minSalary) {
		position.setMinSalary(minSalary);
		positionRepository.save(position);
		List<Employee> employees = position.getEmployees();
		employees.stream().filter(e -> e.getSalary() < minSalary).forEach(e -> e.setSalary(minSalary));
		employeeRepository.saveAll(employees);
	}
}
