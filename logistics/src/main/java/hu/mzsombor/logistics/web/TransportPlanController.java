package hu.mzsombor.logistics.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.mzsombor.logistics.dto.DelayDto;
import hu.mzsombor.logistics.repository.MilestoneRepository;
import hu.mzsombor.logistics.repository.TransportPlanRepository;

@RestController
@RequestMapping("/api/transportPlans")
public class TransportPlanController {

	@Autowired
	TransportPlanRepository transportPlanRepository;

	@Autowired
	MilestoneRepository milestoneRepository;

	@PostMapping("/{id}/delay")
	public void addDelayToATransportPlan(@RequestParam Long id, @RequestBody DelayDto delay) {
		if (transportPlanRepository.findById(id).isEmpty() || 
				milestoneRepository.findById(delay.getMilestoneId()).isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
	}

}
