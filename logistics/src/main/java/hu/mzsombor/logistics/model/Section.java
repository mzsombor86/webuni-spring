package hu.mzsombor.logistics.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Section {

	@Id
	@GeneratedValue
	private long id;
	
	@ManyToOne
	@JoinColumn(name="fromMilestone_id")
	private Milestone fromMilestone;
	@ManyToOne
	@JoinColumn(name="toMilestone_id")
	private Milestone toMilestone;
	
	@ManyToOne
	@JoinColumn(name="transportPlan_id")
	private TransportPlan transportPlan;
	
	private int number;
	
	public Section() {	
	}

	public Section(long id, Milestone fromMilestone, Milestone toMilestone, int number, TransportPlan transportPlan) {
		this.id = id;
		this.fromMilestone = fromMilestone;
		this.toMilestone = toMilestone;
		this.number = number;
		this.transportPlan = transportPlan;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Milestone getFromMilestone() {
		return fromMilestone;
	}

	public void setFromMilestone(Milestone fromMilestone) {
		this.fromMilestone = fromMilestone;
	}

	public Milestone getToMilestone() {
		return toMilestone;
	}

	public void setToMilestone(Milestone toMilestone) {
		this.toMilestone = toMilestone;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public TransportPlan getTransportPlan() {
		return transportPlan;
	}

	public void setTransportPlan(TransportPlan transportPlan) {
		this.transportPlan = transportPlan;
	}
	
}
