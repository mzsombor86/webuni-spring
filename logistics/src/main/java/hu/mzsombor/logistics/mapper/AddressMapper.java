package hu.mzsombor.logistics.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.mzsombor.logistics.dto.AddressDto;
import hu.mzsombor.logistics.model.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
	
	List<AddressDto> addressesToDtos(List<Address> addresses);
	
	AddressDto addressToDto(Address address);
	
	List<Address> dtosToAddresses(List<AddressDto> addressDtos);
	
	Address dtoToAddress (AddressDto addressDto);

}
