package hu.mzsombor.logistics.web;

import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.mzsombor.logistics.dto.AddressDto;
import hu.mzsombor.logistics.dto.AddressExampleDto;
import hu.mzsombor.logistics.mapper.AddressMapper;
import hu.mzsombor.logistics.model.Address;
import hu.mzsombor.logistics.service.AddressService;
import javassist.tools.web.BadHttpRequest;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {
	
	@Autowired
	AddressService addressService;
	
	@Autowired
	AddressMapper addressMapper;
	
	@GetMapping
	public List<AddressDto> getAllAddresses() {
		return addressMapper.addressesToDtos(addressService.getAllAddresses());
	}
	
	@GetMapping("/{id}")
	public AddressDto getAddressById(@PathVariable long id) {
		return addressMapper.addressToDto(addressService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
	}
	
	@PostMapping(value = "/search")
	public ResponseEntity<List<AddressDto>> findByExample(
			@RequestBody AddressExampleDto example, 
			@PageableDefault(direction = Sort.Direction.ASC, page = 0, size = Integer.MAX_VALUE, sort = "id") Pageable pageable
			){
		
		Page<Address> result = addressService.findAddressesByExample(example, pageable);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("X-Total-Count", 
			      Long.toString(result.getTotalElements()));
		
		return ResponseEntity.ok()
				.headers(responseHeaders)
				.body(addressMapper.addressesToDtos(result.getContent()));
	}
	
	@PostMapping
	@PreAuthorize("hasAuthority('AddressManager')")
	public AddressDto addNewAddress(@RequestBody @Valid AddressDto addressDto) {
		if (addressDto.getId() != null && addressDto.getId() != 0L)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		
		return addressMapper.addressToDto(addressService.addNewAddress(addressMapper.dtoToAddress(addressDto)));
	}
	
	@DeleteMapping("/{id}")
	@PreAuthorize("hasAuthority('AddressManager')")
	public void deleteAddress(@PathVariable long id) {
		try {
			addressService.deleteAddress(id);
		} catch (BadHttpRequest e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/{id}")
	@PreAuthorize("hasAuthority('AddressManager')")
	public AddressDto modifyAddress(@RequestBody @Valid AddressDto addressDto, @PathVariable long id) {
		if (addressDto.getId() != null && addressDto.getId() != 0 && addressDto.getId() != id)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		addressDto.setId(id);
		AddressDto modifiedAddress;
		try {
			modifiedAddress = addressMapper.addressToDto(addressService.ModifyAddress(addressMapper.dtoToAddress(addressDto)));
		} catch (EntityNotFoundException e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return modifiedAddress;
	}
	

}
