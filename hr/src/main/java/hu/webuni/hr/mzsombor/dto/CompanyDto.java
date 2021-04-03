package hu.webuni.hr.mzsombor.dto;

import java.util.List;
import java.util.stream.Collectors;

public class CompanyDto {
	private long registrationNumber;
	private String name;
	private String address;
	private List<EmployeeDto> employees;
	
	public CompanyDto(long registrationNumber, String name, String address, List<EmployeeDto> employees) {
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.address = address;
		this.employees = employees;
	}

	public long getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(long registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<EmployeeDto> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeDto> employees) {
		this.employees = employees;
	}
	
	public EmployeeDto getEmployee(long id) {
		return employees.stream().filter(e -> e.getId() == id).collect(Collectors.toList()).get(0);
	}
	
	public void addEmployee(EmployeeDto employee) {
		employees.add(employee);
	}
	
	public void deleteEmployee(long id) {
		employees.removeIf(e -> e.getId() == id);
	}
	

}
