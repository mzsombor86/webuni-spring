package hu.webuni.hr.mzsombor.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.model.LegalForm;
import hu.webuni.hr.mzsombor.model.Position;
import hu.webuni.hr.mzsombor.repository.CompanyRepository;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;
import hu.webuni.hr.mzsombor.repository.LegalFormRepository;
import hu.webuni.hr.mzsombor.repository.PositionRepository;

@Service
public class InitDbService {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	LegalFormRepository legalFormRepository;
	
	@Autowired
	PositionRepository positionRepository;

	@Autowired
	CompanyService companyService;
	
	@Autowired
	PositionService positionService;

	public void clearDB() {
		employeeRepository.deleteAll();
		positionRepository.deleteAll();
		companyRepository.deleteAll();
		legalFormRepository.deleteAll();
		
	}

	public void insertTestData() {

		legalFormRepository.save(new LegalForm(1, "nyrt"));
		legalFormRepository.save(new LegalForm(2, "zrt"));
		legalFormRepository.save(new LegalForm(3, "bt"));
		legalFormRepository.save(new LegalForm(4, "kft"));

		List<Company> companies = new ArrayList<>();
		companies.add(new Company(1, "JavaWorks", "1111 Budapest, Java Street 1.",
				legalFormRepository.findByForm("nyrt").get(), null, null));
		companies.add(new Company(2, "SpringWorks", "2222 Budapest, Spring Street 1.",
				legalFormRepository.findByForm("zrt").get(), null, null));
		companies.add(new Company(3, "HtmlWorks", "3333 Budapest, Html Street 1.",
				legalFormRepository.findByForm("kft").get(), null, null));
		companies.add(new Company(4, "CSSWorks", "4444 Budapest, CSS Street 1.",
				legalFormRepository.findByForm("bt").get(), null, null));
		companies.add(new Company(5, "NodeJSWorks", "5555 Budapest, NodeJS Street 1.",
				legalFormRepository.findByForm("nyrt").get(), null, null));

		int i = 0;

		for (Company company : companies) {
			Company newCompany = companyService.save(company);
			List<Position> positions = new ArrayList<>();
			positions.add(new Position(1, "CEO","MSc", 1_000_000, newCompany, null));
			positions.add(new Position(2, "CTO","MSc", 500_000, newCompany, null));
			positions.add(new Position(3, "CXO","MSc", 500_000, newCompany, null));
			positions.add(new Position(4, "developer","", 300_000, newCompany, null));
			positions.add(new Position(5, "administrative","graduate", 200_000, newCompany, null));
			positions.add(new Position(5, "associate","BSc", 200_000, newCompany, null));
			positions.add(new Position(5, "trainee","", 100_000, newCompany, null));
			for (Position position : positions) {
				positionService.save(position);
			}
			
			
			List<Employee> employees = new ArrayList<>();
			employees.add(new Employee(1, "Sam Mendes", positionService.findByNameAndCompany("CEO", newCompany).get(), 1_000_000 + i,
					LocalDateTime.parse("1980-03-01T10:00:00"), null));
			employees.add(new Employee(2, "John Smith", positionService.findByNameAndCompany("CTO", newCompany).get(), 500_000 + i, LocalDateTime.parse("1990-03-01T10:00:00"),
					null));
			employees.add(new Employee(3, "Angela Davidson", positionService.findByNameAndCompany("CXO", newCompany).get(), 500_000 + i,
					LocalDateTime.parse("2000-03-01T10:00:00"), null));
			employees.add(new Employee(4, "Peter Knee", positionService.findByNameAndCompany("developer", newCompany).get(), 300_000 + i,
					LocalDateTime.parse("2010-03-01T10:00:00"), null));
			employees.add(new Employee(5, "Anthony Spacy", positionService.findByNameAndCompany("administrative", newCompany).get(), 200_000 + i,
					LocalDateTime.parse("2015-03-01T10:00:00"), null));
			employees.add(new Employee(6, "Richard Pearce", positionService.findByNameAndCompany("associate", newCompany).get(), 200_000 + i,
					LocalDateTime.parse("2018-09-01T10:00:00"), null));
			employees.add(new Employee(7, "Megan Baker", positionService.findByNameAndCompany("trainee", newCompany).get(), 100_000 + i,
					LocalDateTime.parse("2020-09-01T10:00:00"), null));
			i += 50_000;

			for (Employee employee : employees) {
				Employee newEmployee = positionService.addEmployee(employee.getPosition().getId(), employee);
				companyService.addEmployee(newCompany.getRegistrationNumber(), newEmployee);
			}
		}

	}

}
