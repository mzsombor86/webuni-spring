package hu.webuni.hr.mzsombor.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.repository.CompanyRepository;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;

@Service
public class InitDbService {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	CompanyService companyService;

	public void clearDB() {
		employeeRepository.deleteAll();
		companyRepository.deleteAll();
	}

	public void insertTestData() {

		List<Company> companies = new ArrayList<>();
		companies.add(new Company(1, "JavaWorks", "1111 Budapest, Java Street 1.", null));
		companies.add(new Company(2, "SpringWorks", "2222 Budapest, Spring Street 1.", null));
		companies.add(new Company(3, "HtmlWorks", "3333 Budapest, Html Street 1.", null));
		companies.add(new Company(4, "CSSWorks", "4444 Budapest, CSS Street 1.", null));
		companies.add(new Company(5, "NodeJSWorks", "5555 Budapest, NodeJS Street 1.", null));

		int i = 0;
		
		for (Company company : companies) {
			Company newCompany = companyService.save(company);
			List<Employee> employees = new ArrayList<>();
			employees.add(
					new Employee(1, "Sam Mendes", "CEO", 1_000_000 + i, LocalDateTime.parse("1980-03-01T10:00:00"), null));
			employees.add(
					new Employee(2, "John Smith", "CTO", 500_000 + i, LocalDateTime.parse("1990-03-01T10:00:00"), null));
			employees.add(new Employee(3, "Angela Davidson", "CXO", 500_000 + i, LocalDateTime.parse("2000-03-01T10:00:00"),
					null));
			employees.add(new Employee(4, "Peter Knee", "developer", 300_000 + i,
					LocalDateTime.parse("2010-03-01T10:00:00"), null));
			employees.add(new Employee(5, "Anthony Spacy", "adminstrative", 200_000 + i,
					LocalDateTime.parse("2015-03-01T10:00:00"), null));
			employees.add(new Employee(6, "Richard Pearce", "associate", 200_000 + i,
					LocalDateTime.parse("2018-09-01T10:00:00"), null));
			employees.add(new Employee(7, "Megan Baker", "trainee", 100_000 + i, LocalDateTime.parse("2020-09-01T10:00:00"),
					null));
			i += 50_000;

			for (Employee employee : employees) {
				companyService.addEmployee(newCompany.getRegistrationNumber(), employee);
			}
		}

	}

}
