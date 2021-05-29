package hu.mzsombor.logistics.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.mzsombor.logistics.dto.AddressExampleDto;
import hu.mzsombor.logistics.model.Address;
import hu.mzsombor.logistics.repository.AddressRepository;

@Service
public class AddressService {

	@Autowired
	AddressRepository addressRepository;

	public List<Address> getAllAddresses() {
		return addressRepository.findAll();
	}

	public Optional<Address> findById(long id) {
		return addressRepository.findById(id);
	}

	@Transactional
	public Address addNewAddress(Address address) {
		return addressRepository.save(address);
	}

	@Transactional
	public void deleteAddress(long id) {
		if (addressRepository.findById(id).isPresent())
			addressRepository.deleteById(id);
	}
	
	@Transactional
	public void deleteAll() {
		addressRepository.deleteAll();
	}

	@Transactional
	public Address ModifyAddress(Address address) {
		if (!addressRepository.existsById(address.getId()))
			throw new EntityNotFoundException();
		return addressRepository.save(address);
	}

	public Page<Address> findAddressesByExample(AddressExampleDto example, Pageable pageable) {
		String countryCode = example.getCountryCode();
		String city = example.getCity();
		String street = example.getStreet();
		String zipCode = example.getZipCode();
		
		Specification<Address> spec = Specification.where(null);
		if (StringUtils.hasText(countryCode))
			spec = spec.and(AddressSpecifications.hasCountryCode(countryCode));
		if (StringUtils.hasText(city))
			spec = spec.and(AddressSpecifications.hasCity(city));
		if (StringUtils.hasText(street))
			spec = spec.and(AddressSpecifications.hasStreet(street));
		if (StringUtils.hasText(zipCode))
			spec = spec.and(AddressSpecifications.hasZipCode(zipCode));
		
		
		return addressRepository.findAll(spec, pageable);
	}
}
