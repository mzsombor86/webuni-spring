package hu.webuni.hr.mzsombor;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.webuni.hr.mzsombor.repository.CompanyRepository;
import hu.webuni.hr.mzsombor.service.InitDbService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	InitDbService initDbService;

	@Autowired
	CompanyRepository companyRepo;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		initDbService.clearDB();
		initDbService.insertTestData();
	}

}
