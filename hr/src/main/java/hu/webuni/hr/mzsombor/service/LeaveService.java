package hu.webuni.hr.mzsombor.service;

import java.security.InvalidParameterException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.webuni.hr.mzsombor.dto.LeaveExampleDto;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.model.Leave;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;
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
			spec = spec.and(LeaveSpecifications.createDateIsBetween(createDateTimeStart, createDateTimeEnd));
		if (StringUtils.hasText(employeeName))
			spec = spec.and(LeaveSpecifications.hasEmployeeName(employeeName));
		if (StringUtils.hasText(approvalName))
			spec = spec.and(LeaveSpecifications.hasApprovalName(approvalName));
		if (startOfLeave != null)
			spec = spec.and(LeaveSpecifications.leaveEndIsGreaterThan(startOfLeave));
		if (endOfLeave != null)
			spec = spec.and(LeaveSpecifications.leaveStartIsLessThan(endOfLeave));
		return leaveRepository.findAll(spec, pageable);
	}

	@Transactional
	@PreAuthorize("#username == authentication.name")
	public Leave addLeave(Leave leave, long employeeId, String username) {
		Employee employee = employeeService.findById(employeeId).get();
		employee.addLeave(leave);
		leave.setCreateDateTime(LocalDateTime.now());
		
		return leaveRepository.save(leave);
	}
	
	
//	public Leave addLeave(Leave leave, long employeeId) {
//		String username = employeeService.findById(employeeId).get().getUsername();
//		return addLeave(leave, employeeId, username);
//	}

	@Transactional
	@PreAuthorize("#approvalUsername == authentication.name")
	public Leave approveLeave(long id, long approvalId, boolean status, String approvalUsername) {
		Leave leave = leaveRepository.findById(id).get();
		if (leave.getEmployee().getSuperior().getId() != approvalId)
			throw new AccessDeniedException("");
		leave.setApprover(employeeService.findById(approvalId).get());
		leave.setApproved(status);
		leave.setApproveDateTime(LocalDateTime.now());
		return leave;
	}
	
//	public Leave approveLeave(long id, long approvalId, boolean status) {
//		String approvalUsername = employeeService.findById(approvalId).get().getUsername();
//		return approveLeave(id, approvalId, status, approvalUsername);
//	}
	
	

	@Transactional
	@PreAuthorize("#username == authentication.name")
	public Leave modifyLeave(long id, Leave newLeave, String username) {
		Leave leave = leaveRepository.findById(id).get();
		if (leave.getApproved() != null)
			throw new InvalidParameterException();
		leave.setStartOfLeave(newLeave.getStartOfLeave());
		leave.setEndOfLeave(newLeave.getEndOfLeave());
		leave.setCreateDateTime(LocalDateTime.now());
		return leave;
	}
	
//	public Leave modifyLeave(long id, Leave newLeave) {
//		String username = leaveRepository.findById(id).get().getEmployee().getUsername();
//		return modifyLeave(id,newLeave, username);
//	}

	@Transactional
	@PreAuthorize("#username == authentication.name")
	public void deleteLeave(long id, String username) {
		Leave leave = leaveRepository.findById(id).get();
		if (leave.getApproved() != null)
			throw new InvalidParameterException();
		long employeeId = leave.getEmployee().getId();
		employeeService.findById(employeeId).get().getLeaves().remove(leave);
		leaveRepository.deleteById(id);
	}
	
//	public void deleteLeave(long id) {
//		String username = leaveRepository.findById(id).get().getEmployee().getUsername();
//		deleteLeave(id, username);
//	}
	

}
