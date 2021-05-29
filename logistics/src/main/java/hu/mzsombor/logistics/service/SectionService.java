package hu.mzsombor.logistics.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.mzsombor.logistics.model.Section;
import hu.mzsombor.logistics.repository.SectionRepository;

@Service
public class SectionService {

	@Autowired
	SectionRepository sectionRepository;
	
	@Autowired
	MilestoneService milestoneService;
	
	public List<Section> getAllSections() {
		return sectionRepository.findAll();
	}

	public Optional<Section> findById(long id) {
		return sectionRepository.findById(id);
	}
	
	@Transactional
	public Section addNewSection(Section section) {
		Section newSection = sectionRepository.save(section);
		newSection.getFromMilestone().setStartInSection(newSection);
		newSection.getToMilestone().setEndInSection(newSection);
		return newSection;
	}
	
	@Transactional
	public void deleteAll() {
		milestoneService.getAllMilestones().stream().forEach(m -> m.setStartInSection(null));
		milestoneService.getAllMilestones().stream().forEach(m -> m.setEndInSection(null));
		getAllSections().stream().forEach(s -> s.setFromMilestone(null));
		getAllSections().stream().forEach(s -> s.setToMilestone(null));
		sectionRepository.deleteAll();
	}
	
	
}
