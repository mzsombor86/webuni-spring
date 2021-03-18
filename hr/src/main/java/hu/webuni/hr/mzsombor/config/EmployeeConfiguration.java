package hu.webuni.hr.mzsombor.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.webuni.hr.mzsombor.service.DefaultEmployeeService;
import hu.webuni.hr.mzsombor.service.EmployeeService;

@Configuration
@Profile("!smart")
public class EmployeeConfiguration {

	@Bean
	public EmployeeService employeeService() {
		return new DefaultEmployeeService();
	}
}
