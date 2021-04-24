package hu.webuni.hr.mzsombor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.repository.CompanyRepository;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;

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

	@Transactional
	public Company addEmployee(Long id, Employee employee) {
		Company company = companyRepository.findById(id).get();
		company.addEmployee(employee);
		employeeRepository.save(employee);
		return company;
	}


	@Transactional
	public Company deleteEmployee(long id, long employeeId) {
		Company company = companyRepository.findById(id).get();
		Employee employee = employeeRepository.findById(employeeId).get();
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		employeeRepository.save(employee);
		return company;
	}
	
	@Transactional
	public Company replaceEmployees(long id, List<Employee> employees) {
		Company company = companyRepository.findById(id).get();
		company.getEmployees().stream().forEach(e -> e.setCompany(null));
		company.getEmployees().clear();
		for (Employee employee : employees) {
			company.addEmployee(employee);
			employeeRepository.save(employee);
		}
		return company;
	}



	@Transactional
	public Company save(Company company) {
		return companyRepository.save(company);
	}

	@Transactional
	public Company update(Company company) {
		if (!companyRepository.existsById(company.getRegistrationNumber()))
			return null;
		return companyRepository.save(company);
	}

	@Transactional
	public void delete(long id) {
		companyRepository.deleteById(id);
	}

}
