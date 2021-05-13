package hu.webuni.hr.mzsombor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.webuni.hr.mzsombor.config.HrConfigProperties;
import hu.webuni.hr.mzsombor.repository.CompanyRepository;
import hu.webuni.hr.mzsombor.service.InitDbService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	InitDbService initDbService;

	@Autowired
	CompanyRepository companyRepo;

	@Autowired
	HrConfigProperties config;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (!config.isTest()) {
			initDbService.clearDB();
			initDbService.insertTestData();
		}
	}

}
