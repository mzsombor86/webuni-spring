package hu.webuni.hr.mzsombor.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hu.webuni.hr.mzsombor.model.Employee;

@Controller
public class EmployeeTLController {

	private List<Employee> allEmployees = new ArrayList<>();
	{
		allEmployees.add(new Employee(1, "Sam Mendes", "CEO", 1_000_000, LocalDateTime.parse("1980-03-01T10:00:00")));
		allEmployees.add(new Employee(2, "John Smith", "CTO", 500_000, LocalDateTime.parse("1990-03-01T10:00:00")));
		allEmployees.add(new Employee(3, "Angela Davidson", "CXO", 500_000, LocalDateTime.parse("2000-03-01T10:00:00")));
		allEmployees.add(new Employee(4, "Peter Knee", "developer", 300_000, LocalDateTime.parse("2010-03-01T10:00:00")));
		allEmployees.add(
				new Employee(5, "Anthony Spacy", "adminstrative", 200_000, LocalDateTime.parse("2015-03-01T10:00:00")));
		allEmployees.add(
				new Employee(6, "Richard Pearce", "associate", 200_000, LocalDateTime.parse("2018-09-01T10:00:00")));
		allEmployees.add(new Employee(7, "Megan Baker", "trainee", 100_000, LocalDateTime.parse("2020-09-01T10:00:00")));
	}



	@GetMapping("/employees")
	public String listEmployees(Map<String, Object> model) {
		model.put("employees", allEmployees);
		model.put("newEmployee", new Employee());
		return "employees";
	}

	@PostMapping("/employees")
	public String addEmployee(Employee employee) {
		allEmployees.add(employee);
		return "redirect:employees";
	}

}
