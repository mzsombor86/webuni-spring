package hu.mzsombor.logistics.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.mzsombor.logistics.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {
	
	@Query("SELECT s FROM Section s WHERE s.transportPlan.id = :transportPlanId AND (s.fromMilestone.id = :milestoneId OR s.toMilestone.id = :milestoneId)")
	List<Section> findByTransportPlanAndMilestone(long transportPlanId, long milestoneId);

	@Query("SELECT s FROM Section s WHERE s.transportPlan.id = :transportPlanId AND s.number = :number")
	Optional<Section> findByTransportPlanAndNumber(long transportPlanId, int number);

}
