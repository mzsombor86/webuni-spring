package hu.webuni.hr.mzsombor.service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.webuni.hr.mzsombor.dto.LeaveExampleDto;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.model.Leave;
import hu.webuni.hr.mzsombor.repository.LeaveRepository;

@Service
public class LeaveService {
	
	@Autowired
	LeaveRepository leaveRepository;
	
	@Autowired
	EmployeeService employeeService;
	
	public List<Leave> findAll() {
		return leaveRepository.findAll();
	}
	
	public Optional<Leave> findById(long id) {
		return leaveRepository.findById(id);
	}

	public Page<Leave> findLeavesByExample(LeaveExampleDto example, Pageable pageable) {
		long id = example.getId();
		LocalDateTime createDateTimeStart = example.getCreateDateTimeStart();
		LocalDateTime createDateTimeEnd = example.getCreateDateTimeEnd();
		String employeeName = example.getEmployeeName();
		String approvalName = example.getApproverName();
		Boolean approved = example.getApproved();
		LocalDateTime startOfLeave = example.getStartOfLeave();
		LocalDateTime endOfLeave = example.getEndOfLeave();
	
		Specification<Leave> spec = Specification.where(null);
		
		if (approved != null)
			spec = spec.and(LeaveSpecifications.hasApproved(approved));
		if (id > 0)
			spec = spec.and(LeaveSpecifications.hasId(id));
		if (createDateTimeStart != null && createDateTimeEnd != null)
			spec = spec.and(LeaveSpecifications.createDateIsBetween(createDateTimeStart,createDateTimeEnd));
		if (StringUtils.hasText(employeeName))
			spec = spec.and(LeaveSpecifications.hasEmployeeName(employeeName));
		if (StringUtils.hasText(approvalName))
			spec = spec.and(LeaveSpecifications.hasApprovalName(approvalName));
		if (startOfLeave != null && endOfLeave != null) {
			Specification<Leave> spec2 = Specification.where(LeaveSpecifications.leaveStartIsBetween(startOfLeave, endOfLeave));			
			spec2 = spec2.or(LeaveSpecifications.leaveEndIsBetween(startOfLeave, endOfLeave));
			
			Specification<Leave> spec3 = Specification.where(LeaveSpecifications.leaveStartIsLessThan(startOfLeave));			
			spec3 = spec3.and(LeaveSpecifications.leaveEndIsGreaterThan(endOfLeave));
			
			Specification<Leave> spec4 = Specification.where(spec2);
			spec4 = spec4.or(spec3);
			
			spec = spec.and(spec4);
		}
		return leaveRepository.findAll(spec, pageable);
	}

	@Transactional
	public Leave addLeave(Leave leave, long employeeId) {
		Employee employee = employeeService.findById(employeeId).get();
		employee.addLeave(leave);
		leave.setCreateDateTime(LocalDateTime.now());
		return leaveRepository.save(leave);
	}

	@Transactional
	public Leave approveLeave(long id, long approvalId, boolean status) {
		Leave leave = leaveRepository.findById(id).get();
		leave.setApprover(employeeService.findById(approvalId).get());
		leave.setApproved(status);
		leave.setApproveDateTime(LocalDateTime.now());
		return leave;
	}

	@Transactional
	public Leave modifyLeave(long id, Leave newLeave) {
		Leave leave = leaveRepository.findById(id).get();
		if (leave.getApproved() != null)
			throw new InvalidParameterException();
		leave.setStartOfLeave(newLeave.getStartOfLeave());
		leave.setEndOfLeave(newLeave.getEndOfLeave());
		leave.setCreateDateTime(LocalDateTime.now());
		return leave;
	}
	
	@Transactional
	public void deleteLeave(long id) {
		Leave leave = leaveRepository.findById(id).get();
		if (leave.getApproved() != null)
			throw new InvalidParameterException();
		leave.getEmployee().getLeaves().remove(leave);
		leaveRepository.deleteById(id);
	}
	
	

}
