package hu.webuni.hr.mzsombor.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.mzsombor.config.HrConfigProperties;
import hu.webuni.hr.mzsombor.model.Employee;

@Service
public class SmartEmployeeService implements EmployeeService {

	@Autowired
	HrConfigProperties config;

	@Override
	public int getPayRaisePercent(Employee employee) {
		double yearsFromEntry = Duration.between(employee.getEntryDate(), LocalDateTime.now()).toDays() / 365.0;

		String yearsString = config.getRaise().getSmart().getYears();
		Matcher m = Pattern.compile("\\d+\\.?\\d*").matcher(yearsString);
		List<Double> years = new ArrayList<Double>();
		while (m.find()) {
			years.add(Double.parseDouble(m.group()));
		}

		String percentageString = config.getRaise().getSmart().getPercents();
		m = Pattern.compile("\\d+").matcher(percentageString);
		List<Integer> percentages = new ArrayList<Integer>();
		while (m.find()) {
			percentages.add(Integer.parseInt(m.group()));
		}

		for (int i = 0; i < years.size(); i++) {
			if (yearsFromEntry > years.get(i))
				return percentages.get(i);
		}
		return 0;

	}

}
