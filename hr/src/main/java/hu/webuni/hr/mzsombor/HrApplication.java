package hu.webuni.hr.mzsombor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	SalaryService salaryService;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee(1, "Sam Mendes", "CEO", 1_000_000, LocalDateTime.parse("1980-03-01T10:00:00")));
		employees.add(new Employee(2, "John Smith", "CTO", 500_000, LocalDateTime.parse("1990-03-01T10:00:00")));
		employees.add(new Employee(3, "Angela Davidson", "CXO", 500_000, LocalDateTime.parse("2000-03-01T10:00:00")));
		employees.add(new Employee(4, "Peter Knee", "developer", 300_000, LocalDateTime.parse("2010-03-01T10:00:00")));
		employees.add(new Employee(5, "Anthony Spacy", "adminstrative", 200_000, LocalDateTime.parse("2015-03-01T10:00:00")));
		employees.add(new Employee(6, "Richard Pearce", "associate", 200_000, LocalDateTime.parse("2018-09-01T10:00:00")));
		employees.add(new Employee(7, "Megan Baker", "trainee", 100_000, LocalDateTime.parse("2020-09-01T10:00:00")));

//		for (Employee employee : employees) {
//			System.out.println(employee);
//			System.out.println(employee.getName() + "'s salary before the raise: " + employee.getSalary());
//			salaryService.setRaise(employee);
//			System.out.println(employee.getName() + "'s salary after the raise: " + employee.getSalary());
//			System.out.println("");
//		}

	}
	
	// próba

}
