package hu.webuni.hr.mzsombor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.model.Position;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;
import hu.webuni.hr.mzsombor.repository.PositionRepository;

@Service
public class PositionService {

	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	public List<Position> findAll() {
		return positionRepository.findAll();
	}
	
	public Optional<Position> findById(long id) {
		return positionRepository.findById(id);
	}
	
	public Optional<Position> findByName(String name) {
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
	public Position update(Position position) {
		Position oldPosition = positionRepository.findById(position.getId()).get();
		position.setCompany(oldPosition.getCompany());
		position.setEmployees(oldPosition.getEmployees());
		return positionRepository.save(position);
	}
	
	@Transactional
	public void delete(long id) {
		positionRepository.deleteById(id);
	}
}
