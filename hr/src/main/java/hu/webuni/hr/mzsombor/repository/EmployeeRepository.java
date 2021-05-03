package hu.webuni.hr.mzsombor.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.model.Position;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	List<Employee> findBySalaryGreaterThan(int salary);

	List<Employee> findByPosition(Position position);

	List<Employee> findByNameStartingWithIgnoreCase(String name);

	List<Employee> findByEntryDateBetween(LocalDateTime start, LocalDateTime end);

	Page<Employee> findBySalaryGreaterThan(int salary, Pageable pageable);
}
