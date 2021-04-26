package hu.webuni.hr.mzsombor.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.hr.mzsombor.dto.AvgSalaryDto;
import hu.webuni.hr.mzsombor.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	@Query("SELECT DISTINCT c FROM Company c INNER JOIN c.employees e WHERE e.salary > :salary")
	List<Company> findWhereEmployeeSalaryIsGreaterThan(int salary);
	
	@Query("SELECT c FROM Company c INNER JOIN c.employees e GROUP BY c.registrationNumber HAVING COUNT(e) > :employeeNumber")
	List<Company> findWhereEmployeeNumberIsAbove(Long employeeNumber);
	
	@Query("SELECT new hu.webuni.hr.mzsombor.dto.AvgSalaryDto(e.position.name, avg(e.salary)) FROM Company c INNER JOIN c.employees e WHERE c.registrationNumber = :id GROUP BY e.position.name ORDER BY avg(e.salary) DESC")
	List<AvgSalaryDto> listAverageSalaryiesGroupedByTitlesAtACompany(Long id);
	
	Optional<Company> findByName(String name);
}
