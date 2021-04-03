package hu.webuni.hr.mzsombor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.mzsombor.config.HrConfigProperties;
import hu.webuni.hr.mzsombor.dto.EmployeeDto;

@Service
public class DefaultEmployeeService implements EmployeeService {

	@Autowired
	HrConfigProperties config;

	@Override
	public int getPayRaisePercent(EmployeeDto employee) {
		return config.getRaise().getDef().getPercent();
	}

}
