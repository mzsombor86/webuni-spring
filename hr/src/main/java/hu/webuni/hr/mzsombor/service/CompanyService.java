package hu.webuni.hr.mzsombor.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.repository.CompanyRepository;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;

@Service
public class CompanyService {
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;

//	private List<Employee> employees = new ArrayList<>();
//	private Map<Long, Company> companies = new HashMap<>();
//	{
//		employees.add(new Employee(1, "Sam Mendes", "CEO", 1_000_000, LocalDateTime.parse("1980-03-01T10:00:00")));
//		employees.add(new Employee(2, "John Smith", "CTO", 500_000, LocalDateTime.parse("1990-03-01T10:00:00")));
//		employees
//				.add(new Employee(3, "Angela Davidson", "CXO", 500_000, LocalDateTime.parse("2000-03-01T10:00:00")));
//		employees.add(
//				new Employee(4, "Peter Knee", "developer", 300_000, LocalDateTime.parse("2010-03-01T10:00:00")));
//		employees.add(new Employee(5, "Anthony Spacy", "adminstrative", 200_000,
//				LocalDateTime.parse("2015-03-01T10:00:00")));
//		employees.add(
//				new Employee(6, "Richard Pearce", "associate", 200_000, LocalDateTime.parse("2018-09-01T10:00:00")));
//		employees
//				.add(new Employee(7, "Megan Baker", "trainee", 100_000, LocalDateTime.parse("2020-09-01T10:00:00")));
//
//		companies.put(1L, new Company(1, "JavaWorks", "1111 Budapest, Java Street 1.", employees));
//		companies.put(2L, new Company(2, "SpringWorks", "2222 Budapest, Spring Street 1.", employees));
//		companies.put(3L, new Company(3, "HtmlWorks", "3333 Budapest, Html Street 1.", employees));
//		companies.put(4L, new Company(4, "CSSWorks", "4444 Budapest, CSS Street 1.", employees));
//		companies.put(5L, new Company(5, "NodeJSWorks", "5555 Budapest, NodeJS Street 1.", employees));
//	}
	
	public List<Company> findAll() {
		return companyRepository.findAll();
//		return new ArrayList<Company>(companies.values());
	}
	
	public Optional<Company> findById(long id) {
		return companyRepository.findById(id);
//		return companies.get(id);
	}
	
	@Transactional
	public Company save(Company company) {
		employeeRepository.saveAll(company.getEmployees());
		return companyRepository.save(company);
//		companies.put(company.getRegistrationNumber(), company);
//		return company;
	}

	@Transactional
	public void delete(long id) {
		companyRepository.deleteById(id);
//		companies.remove(id);
	}
	
	

}
