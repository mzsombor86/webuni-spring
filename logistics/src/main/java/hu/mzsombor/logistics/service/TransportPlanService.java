package hu.mzsombor.logistics.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.mzsombor.logistics.model.TransportPlan;
import hu.mzsombor.logistics.repository.TransportPlanRepository;

@Service
public class TransportPlanService {

	@Autowired
	TransportPlanRepository transportPlanRepository;
	
	@Autowired 
	SectionService sectionService;
	
	public List<TransportPlan> getAllTransportPlans() {
		return transportPlanRepository.findAll();
	}

	public Optional<TransportPlan> findById(long id) {
		return transportPlanRepository.findById(id);
	}
	
	@Transactional
	public TransportPlan addNewTransportPlan(TransportPlan transportPlan) {
		TransportPlan newTransportPlan = transportPlanRepository.save(transportPlan);
		newTransportPlan.getSections().stream().forEach(s -> s.setTransportPlan(newTransportPlan));
		return newTransportPlan;
		
	}
	
	@Transactional
	public void deleteAll() {
		sectionService.getAllSections().stream().forEach(s -> s.setTransportPlan(null));
		getAllTransportPlans().stream().forEach(t -> t.setSections(null));
		transportPlanRepository.deleteAll();
	}
}
