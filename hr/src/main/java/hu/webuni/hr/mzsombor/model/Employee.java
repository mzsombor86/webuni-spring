package hu.webuni.hr.mzsombor.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

@Entity
public class Employee {
	@Id
	@GeneratedValue
	private long id;
	@NotEmpty
	private String name;
	
	@ManyToOne
	@JoinColumn(name="position_id")
	private Position position;
	
	@Min(value = 1)
	private int salary;
	@Past
	private LocalDateTime entryDate;
	
	@ManyToOne
	@JoinColumn(name="company_id")
	private Company company;

	public Employee() {

	}

	public Employee(long id, String name, Position position, int salary, LocalDateTime entryDate, Company company) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.entryDate = entryDate;
		this.company = company;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public LocalDateTime getEntryDate() {
		return entryDate;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public void setEntryDate(LocalDateTime entryDate) {
		this.entryDate = entryDate;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", position=" + position + ", salary=" + salary + ", entryDate="
				+ entryDate + "]";
	}

}
