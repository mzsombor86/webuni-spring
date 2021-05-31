package hu.mzsombor.logistics.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.mzsombor.logistics.dto.DelayDto;
import hu.mzsombor.logistics.service.MilestoneService;
import hu.mzsombor.logistics.service.SectionService;
import hu.mzsombor.logistics.service.TransportPlanService;

@RestController
@RequestMapping("/api/transportPlans")
public class TransportPlanController {

	@Autowired
	TransportPlanService transportPlanService;

	@Autowired
	MilestoneService milestoneService;
	
	@Autowired
	SectionService sectionService;
	
	
	@PostMapping("/{id}/delay")
	public void addDelayToATransportPlan(@PathVariable Long id, @RequestBody DelayDto delay) {
		if (transportPlanService.findById(id).isEmpty() || 
				milestoneService.findById(delay.getMilestoneId()).isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		if (sectionService.findByTransportPlanAndMilestone(id, delay.getMilestoneId()).isEmpty())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		transportPlanService.registerDelay(id, delay.getMilestoneId(),delay.getDelayInMinutes());
	}

}
