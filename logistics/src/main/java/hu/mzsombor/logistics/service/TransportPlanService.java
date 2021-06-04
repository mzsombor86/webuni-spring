package hu.mzsombor.logistics.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.mzsombor.logistics.config.LogisticsConfigProperties;
import hu.mzsombor.logistics.model.Milestone;
import hu.mzsombor.logistics.model.Section;
import hu.mzsombor.logistics.model.TransportPlan;
import hu.mzsombor.logistics.repository.TransportPlanRepository;

@Service
public class TransportPlanService {

	@Autowired
	TransportPlanRepository transportPlanRepository;
	
	@Autowired 
	SectionService sectionService;
	
	@Autowired
	MilestoneService milestoneService;
	
	@Autowired
	LogisticsConfigProperties config;
	
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
	public TransportPlan updateTransportPlan(TransportPlan transportPlan) {
		return transportPlanRepository.save(transportPlan);
	}
	
	@Transactional
	public void deleteAll() {
		sectionService.getAllSections().stream().forEach(s -> s.setTransportPlan(null));
		getAllTransportPlans().stream().forEach(t -> t.setSections(null));
		transportPlanRepository.deleteAll();
	}
	
	@Transactional
	public long registerDelay(long transportPlanId, long milestoneId, int delayInMinutes) {
		long newRevenue = adjustRevenue(transportPlanId, delayInMinutes);	
		setDelayInAffectedMilestones(transportPlanId, milestoneId, delayInMinutes);		
		return newRevenue;
	}

	private long adjustRevenue(long transportPlanId, int delayInMinutes) {
		TransportPlan transportPlan = transportPlanRepository.findById(transportPlanId).get();
		long currentRevenue = transportPlan.getExpectedRevenue();
		long adjustedRevenue = currentRevenue;
		
		if (delayInMinutes < 30) {
			adjustedRevenue *= (100-config.getRevenueDropPercentage().getBelow30minutes()) * 0.01;
		} else if (delayInMinutes < 60) {
			adjustedRevenue *= (100-config.getRevenueDropPercentage().getBelow60minutes()) * 0.01;
		} else if (delayInMinutes < 120) {
			adjustedRevenue *= (100-config.getRevenueDropPercentage().getBelow120minutes()) * 0.01;
		} else {
			adjustedRevenue *= (100-config.getRevenueDropPercentage().getAbove120minutes()) * 0.01;
		}
		
		transportPlan.setExpectedRevenue(adjustedRevenue);
		
		return adjustedRevenue;
	}
	
	
	private void setDelayInAffectedMilestones(long transportPlanId, long firstMilestoneId, int delayInMinutes) {
		Milestone currentMilestone = milestoneService.findById(firstMilestoneId).get();
		currentMilestone.setPlannedTime(currentMilestone.getPlannedTime().plusMinutes(delayInMinutes));
		
		Section section = sectionService.findByMilestoneId(firstMilestoneId).get();
		Milestone nextMilestone = null;
		
		if (section.getFromMilestone().equals(currentMilestone)) {
			nextMilestone = section.getToMilestone();
		} else {
			int nextSectionNumber = section.getNumber() + 1;
			Section nextSection = sectionService.findByTransportPlanIdAndNumber(transportPlanId, nextSectionNumber).orElse(null);
			if (nextSection != null) {	
				nextMilestone = nextSection.getFromMilestone();
			}
		}
		
		if (nextMilestone != null) {
			nextMilestone.setPlannedTime(nextMilestone.getPlannedTime().plusMinutes(delayInMinutes));
		}
	}

	
	
}
