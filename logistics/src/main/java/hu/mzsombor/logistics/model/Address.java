package hu.mzsombor.logistics.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
public class Address {
	@Id
	@GeneratedValue
	private long id;

	@NotEmpty
	private String countryCode;
	@NotEmpty
	private String city;
	@NotEmpty
	private String street;
	@NotEmpty
	private String zipCode;
	@NotEmpty
	private String streetNumber;

	private Double longitude;
	private Double latitude;

	public Address() {
	}

	public Address(long id, @NotEmpty String countryCode, @NotEmpty String city, @NotEmpty String street,
			@NotEmpty String zipCode, @NotEmpty String streetNumber, Double longitude, Double latitude) {
		this.id = id;
		this.countryCode = countryCode;
		this.city = city;
		this.street = street;
		this.zipCode = zipCode;
		this.streetNumber = streetNumber;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

}
