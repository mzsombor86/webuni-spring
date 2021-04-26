package hu.webuni.hr.mzsombor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.mzsombor.model.Company;
import hu.webuni.hr.mzsombor.model.Position;

public interface PositionRepository extends JpaRepository<Position, Long>{
	
	Optional<Position> findByName(String name);

	Optional<Position> findByNameAndCompany(String name, Company company);
	

}
