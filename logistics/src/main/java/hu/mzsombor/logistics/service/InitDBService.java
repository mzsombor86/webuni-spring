package hu.mzsombor.logistics.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.mzsombor.logistics.model.Address;
import hu.mzsombor.logistics.model.Milestone;
import hu.mzsombor.logistics.model.Section;
import hu.mzsombor.logistics.model.TransportPlan;

@Service
public class InitDBService {

	@Autowired
	AddressService addressService;

	@Autowired
	MilestoneService milestoneService;

	@Autowired
	SectionService sectionService;

	@Autowired
	TransportPlanService transportPlanService;

	public TransportPlan init() {
		transportPlanService.deleteAll();
		sectionService.deleteAll();
		milestoneService.deleteAll();
		addressService.deleteAll();

		Address address1 = addressService
				.addNewAddress(new Address(1, "HU", "Budapest", "Java Street", "1234", "1.", null, null));
		Address address2 = addressService
				.addNewAddress(new Address(2, "HU", "Győr", "Transport Street", "9000", "2.", null, null));
		Address address3 = addressService
				.addNewAddress(new Address(3, "HU", "Hegyeshalom", "Border Street", "9876", "3.", null, null));
		Address address4 = addressService
				.addNewAddress(new Address(4, "AT", "Wien", " Street", "23456", "4.", null, null));
		Address address5 = addressService
				.addNewAddress(new Address(5, "AT", "Salzburg", "Mozart Street", "54321", "5.", null, null));

		Milestone milestone1 = milestoneService
				.addNewMilestone(new Milestone(1, address1, LocalDateTime.of(2021, 05, 10, 11, 0)));
		Milestone milestone2 = milestoneService
				.addNewMilestone(new Milestone(2, address2, LocalDateTime.of(2021, 05, 11, 10, 0)));
		Milestone milestone3 = milestoneService
				.addNewMilestone(new Milestone(3, address2, LocalDateTime.of(2021, 05, 11, 11, 0)));
		Milestone milestone4 = milestoneService
				.addNewMilestone(new Milestone(4, address3, LocalDateTime.of(2021, 05, 12, 10, 0)));
		Milestone milestone5 = milestoneService
				.addNewMilestone(new Milestone(5, address3, LocalDateTime.of(2021, 05, 12, 11, 0)));
		Milestone milestone6 = milestoneService
				.addNewMilestone(new Milestone(6, address4, LocalDateTime.of(2021, 05, 13, 10, 0)));
		Milestone milestone7 = milestoneService
				.addNewMilestone(new Milestone(7, address4, LocalDateTime.of(2021, 05, 13, 11, 0)));
		Milestone milestone8 = milestoneService
				.addNewMilestone(new Milestone(8, address5, LocalDateTime.of(2021, 05, 14, 10, 0)));
		Milestone milestone9 = milestoneService
				.addNewMilestone(new Milestone(9, address5, LocalDateTime.of(2022, 05, 14, 10, 0)));

		List<Section> sections = new ArrayList<>();

		sections.add(sectionService.addNewSection(new Section(1, milestone1, milestone2, 0, null)));
		sections.add(sectionService.addNewSection(new Section(2, milestone3, milestone4, 1, null)));
		sections.add(sectionService.addNewSection(new Section(3, milestone5, milestone6, 2, null)));
		sections.add(sectionService.addNewSection(new Section(4, milestone7, milestone8, 3, null)));

		return transportPlanService.addNewTransportPlan(new TransportPlan(1, sections, 10_000L));
	}
}
