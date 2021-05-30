package hu.mzsombor.logistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.mzsombor.logistics.config.LogisticsConfigProperties;
import hu.mzsombor.logistics.service.InitDBService;

@SpringBootApplication
public class LogisticsApplication implements CommandLineRunner{

	@Autowired
	InitDBService initDBService;

	@Autowired
	LogisticsConfigProperties config;
	
	
	public static void main(String[] args) {
		SpringApplication.run(LogisticsApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		if (!config.isTest()) {
//			initDBService.init();
		}
	}

}
