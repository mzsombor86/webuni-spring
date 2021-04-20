package hu.webuni.hr.mzsombor.service;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.repository.CompanyRepository;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;
import javassist.NotFoundException;

@Service
public class CompanyService {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	public Optional<Company> findById(long id) {
		return companyRepository.findById(id);
	}

	public Company addEmployeeToACompany(Company company, Employee employee) {
		employee.setCompany(company);
		company.getEmployees().add(employee);
		return company;
	}

	public Company addEmployeesToACompany(Company company, List<Employee> employees) {
		Iterator<Employee> it = employees.iterator();
		while (it.hasNext())
			it.next().setCompany(company);
		company.setEmployees(employees);
		return company;

	}

	public Company removeEmployeeFromACompany(Company company, Employee employee) {
		employee.setCompany(null);
		company.getEmployees().removeIf(e -> e.getId() == employee.getId());
		return company;
	}

	public Company removeAllEmployeesFromACompany(Company company) {
		Iterator<Employee> it = company.getEmployees().iterator();
		while (it.hasNext())
			it.next().setCompany(null);
		employeeRepository.deleteInBatch(company.getEmployees());
		company.setEmployees(null);
		return company;
	}

	@Transactional
	public Company save(Company company) {
		employeeRepository.saveAll(company.getEmployees());
		return companyRepository.save(company);
	}

	@Transactional
	public void delete(long id) throws NotFoundException {
		Company company = companyRepository.findById(id).orElseThrow(() -> new NotFoundException(null));
		removeAllEmployeesFromACompany(company);
		companyRepository.deleteById(id);
	}

	@Transactional
	public void deleteAll() {
		companyRepository.deleteAll();
	}

}
