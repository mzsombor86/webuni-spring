package hu.mzsombor.logistics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.mzsombor.logistics.model.Milestone;

public interface MilestoneRepository extends JpaRepository<Milestone, Long>{
	
	List<Milestone> findByAddressId(long id);

}
