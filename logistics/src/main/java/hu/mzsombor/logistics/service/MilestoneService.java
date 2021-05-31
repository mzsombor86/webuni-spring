package hu.mzsombor.logistics.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.mzsombor.logistics.model.Milestone;
import hu.mzsombor.logistics.repository.MilestoneRepository;

@Service
public class MilestoneService {

	@Autowired
	MilestoneRepository milestoneRepository;
	
	public List<Milestone> getAllMilestones() {
		return milestoneRepository.findAll();
	}
	
	public List<Milestone> findByAddressId(long id) {
		return milestoneRepository.findByAddressId(id);
	}

	public Optional<Milestone> findById(long id) {
		return milestoneRepository.findById(id);
	}
	
	@Transactional
	public Milestone addNewMilestone(Milestone milestone) {
		return milestoneRepository.save(milestone);
	}
	
	@Transactional
	public void deleteAll() {
		getAllMilestones().stream().forEach(m -> m.setAddress(null));
		milestoneRepository.deleteAll();
	}
	
}
