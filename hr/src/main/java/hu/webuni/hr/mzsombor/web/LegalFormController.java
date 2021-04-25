package hu.webuni.hr.mzsombor.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.webuni.hr.mzsombor.dto.LegalFormDto;
import hu.webuni.hr.mzsombor.mapper.LegalFormMapper;
import hu.webuni.hr.mzsombor.service.LegalFormService;

@RestController
@RequestMapping("/api/legalforms")
public class LegalFormController {
	
	@Autowired
	LegalFormService legalFormService;
	
	@Autowired
	LegalFormMapper legalFormMapper;
	
	@GetMapping
	List<LegalFormDto> getLegalForms() {
		return legalFormMapper.legalFormsToLegalFormDtos(legalFormService.findAll());
	}
	
	@PostMapping
	LegalFormDto addLegalForm(@RequestBody LegalFormDto legalFormDto) {
		return legalFormMapper.legalFormToLegalFormDto(legalFormService.save(legalFormMapper.legalFormDTOToLegalForm(legalFormDto)));
	}
	
	@PutMapping("/{id}")
	LegalFormDto changeLegalForm(@RequestBody LegalFormDto legalFormDto, @PathVariable long id ) {
		legalFormService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		legalFormDto.setId(id);
		return legalFormMapper.legalFormToLegalFormDto(legalFormService.save(legalFormMapper.legalFormDTOToLegalForm(legalFormDto)));
	}
	
	@DeleteMapping("{/id}")
	void deleteLegalForm(@PathVariable long id) {
		legalFormService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		legalFormService.delete(id);
	}

}
