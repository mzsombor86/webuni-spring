package hu.mzsombor.logistics.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Milestone {

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name="address_id")
	private Address address;
	private LocalDateTime plannedTime;
	
	public Milestone() {
	}

	public Milestone(long id, Address address, LocalDateTime dateTime) {
		this.id = id;
		this.address = address;
		this.plannedTime = dateTime;
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
	
	
	
	
}
