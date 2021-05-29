package hu.mzsombor.logistics.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class Milestone {

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name="address_id")
	private Address address;
	private LocalDateTime plannedTime;
	
	@OneToOne
	private Section startInSection;
	@OneToOne
	private Section endInSection;
	
	
	public Milestone() {
	}

	

	public Milestone(long id, Address address, LocalDateTime plannedTime, Section startInSection,
			Section endInSection) {
		this.id = id;
		this.address = address;
		this.plannedTime = plannedTime;
		this.startInSection = startInSection;
		this.endInSection = endInSection;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public LocalDateTime getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(LocalDateTime plannedTime) {
		this.plannedTime = plannedTime;
	}



	public Section getStartInSection() {
		return startInSection;
	}



	public void setStartInSection(Section startInSection) {
		this.startInSection = startInSection;
	}



	public Section getEndInSection() {
		return endInSection;
	}



	public void setEndInSection(Section endInSection) {
		this.endInSection = endInSection;
	}
	
	
	
	
	
	
}
