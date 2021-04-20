package hu.webuni.hr.mzsombor.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.mzsombor.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findBySalaryGreaterThan(int salary);

	List<Employee> findByTitle(String title);

	List<Employee> findByNameStartingWithIgnoreCase(String name);

	List<Employee> findByEntryDateBetween(LocalDateTime start, LocalDateTime end);

}
