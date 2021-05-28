package hu.mzsombor.logistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.mzsombor.logistics.model.Section;

public interface SectionRepository extends JpaRepository<Section, Long> {

}
