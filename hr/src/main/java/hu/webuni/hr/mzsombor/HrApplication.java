package hu.webuni.hr.mzsombor;

import java.time.LocalDateTime;

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
		Employee employee1 = new Employee(1, "Sam Mendes", "CTO", 100_000, LocalDateTime.parse("2021-03-01T10:00:00"));

		System.out.println(employee1.getName() + " fizetése az emelés előtt: " + employee1.getSalary());

		salaryService.setRaise(employee1);

		System.out.println(employee1.getName() + " fizetése az emelés után: " + employee1.getSalary());

	}

}
