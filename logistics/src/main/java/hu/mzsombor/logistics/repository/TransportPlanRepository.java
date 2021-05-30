package hu.mzsombor.logistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.mzsombor.logistics.model.TransportPlan;

public interface TransportPlanRepository extends JpaRepository<TransportPlan, Long> {
	
}
