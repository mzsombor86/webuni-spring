package hu.mzsombor.logistics.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public Address ModifyAddress(Address address) {
		if (!addressRepository.existsById(address.getId()))
			throw new EntityNotFoundException();
		return addressRepository.save(address);
	}
}
