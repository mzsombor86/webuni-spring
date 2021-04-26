package hu.webuni.hr.mzsombor.dto;

import java.util.List;

public class CompanyDto {
	private long registrationNumber;
	private String name;
	private String address;
	private LegalFormDto legalForm;
	private List<EmployeeDto> employees;
	
	public CompanyDto() {
		
	}
	
	public CompanyDto(long registrationNumber, String name, String address, LegalFormDto legalForm,
			List<EmployeeDto> employees) {
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.address = address;
		this.legalForm = legalForm;
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
	public LegalFormDto getLegalForm() {
		return legalForm;
	}
	public void setLegalForm(LegalFormDto legalForm) {
		this.legalForm = legalForm;
	}
	public List<EmployeeDto> getEmployees() {
		return employees;
	}
	public void setEmployees(List<EmployeeDto> employees) {
		this.employees = employees;
	}


	
}
