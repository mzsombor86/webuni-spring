package hu.webuni.hr.mzsombor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.mzsombor.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {

}
