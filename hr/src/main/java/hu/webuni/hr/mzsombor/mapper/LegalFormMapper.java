package hu.webuni.hr.mzsombor.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.mzsombor.dto.LegalFormDto;
import hu.webuni.hr.mzsombor.model.LegalForm;

@Mapper(componentModel = "spring")
public interface LegalFormMapper {
	
	LegalFormDto legalFormToLegalFormDto(LegalForm legalForm);
	
	List<LegalFormDto> legalFormsToLegalFormDtos(List<LegalForm> legalForms);
	
	LegalForm legalFormDTOToLegalForm(LegalFormDto legalFormDto);

	
}
