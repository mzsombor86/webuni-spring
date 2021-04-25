package hu.webuni.hr.mzsombor.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.mzsombor.model.LegalForm;

public interface LegalFormRepository extends JpaRepository<LegalForm, Long> {

	Optional<LegalForm> findByForm(String form);
	
}
