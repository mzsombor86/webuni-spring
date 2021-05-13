package hu.webuni.hr.mzsombor.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.webuni.hr.mzsombor.dto.CompanyDto;
import hu.webuni.hr.mzsombor.dto.EmployeeDto;
import hu.webuni.hr.mzsombor.dto.LeaveDto;
import hu.webuni.hr.mzsombor.dto.LeaveExampleDto;
import hu.webuni.hr.mzsombor.dto.LegalFormDto;
import hu.webuni.hr.mzsombor.dto.PositionDto;
import hu.webuni.hr.mzsombor.model.Employee;
import hu.webuni.hr.mzsombor.repository.CompanyRepository;
import hu.webuni.hr.mzsombor.repository.EmployeeRepository;
import hu.webuni.hr.mzsombor.repository.LeaveRepository;
import hu.webuni.hr.mzsombor.repository.LegalFormRepository;
import hu.webuni.hr.mzsombor.repository.PositionRepository;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class LeaveControllerIT {

	//Before running integration tests, you have to switch off the two initDB calling in HrApplication.java run method!
	
	
	private static final String BASE_URI = "/api";
	private static final String LEAVES_BASE_URI = "/api/leaves";

	@Autowired
	WebTestClient webTestClient;
	
	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	LegalFormRepository legalFormRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	LeaveRepository leaveRepository;
	
	@BeforeEach
	public void prepareDB() {
		clearDB();
		initDB();
	}
	
	private void initDB() {
		createLegalForm(new LegalFormDto(1, "zrt"));
		createCompany(new CompanyDto(1, "JavaWorks", "1111 Budapest, Java street 1.", "zrt", null));
		createPosition(new PositionDto(1, "CEO", "MSc", 1_000_000, "JavaWorks", null));
		createPosition(new PositionDto(2, "assistant", "graduate", 100_000, "JavaWorks", null));
		EmployeeDto newEmployee1 = new EmployeeDto(1, "Vezér Igazgató", "CEO", 1_000_000, LocalDateTime.now(), "JavaWorks");
		EmployeeDto newEmployee2 = new EmployeeDto(2, "Asziszens Andi", "assistant", 200_000, LocalDateTime.now(), "JavaWorks");		
		long registrationNumber = companyRepository.findByName("JavaWorks").get().getRegistrationNumber();
		addEmployee(newEmployee1, registrationNumber);
		addEmployee(newEmployee2, registrationNumber);
		
	}

	private void clearDB() {
		leaveRepository.deleteAll();
		employeeRepository.deleteAll();
		positionRepository.deleteAll();
		companyRepository.deleteAll();
		legalFormRepository.deleteAll();
	}
	
	private void createLegalForm(LegalFormDto newLegalForm) {
		webTestClient
			.post()
			.uri(BASE_URI + "/legalforms")
			.bodyValue(newLegalForm)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	private void createCompany(CompanyDto newCompany) {
		webTestClient
			.post()
			.uri(BASE_URI + "/companies")
			.bodyValue(newCompany)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	private void createPosition(PositionDto newPosition) {
		webTestClient
			.post()
			.uri(BASE_URI + "/positions")
			.bodyValue(newPosition)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	private void addEmployee(EmployeeDto newEmployee, long registrationNumber) {
		webTestClient
			.post()
			.uri(BASE_URI + "/companies/" + registrationNumber + "/employee")
			.bodyValue(newEmployee)
			.exchange()
			.expectStatus()
			.isOk();
	}
	
	
	@Test
	void testThatANewLeaveCanBeAdded() throws Exception {
		List<Employee> employees = employeeRepository.findByNameStartingWithIgnoreCase("Vezér Igazgató");
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		addLeaveOk(newLeave);
		List<LeaveDto> leaves = getAllLeaves();
		assertThat(leaves.size()).isEqualTo(1);
	}
	
	@Test
	void testThatANewLeaveCannotBeAddedToANonExistingEmployee() throws Exception {
		LeaveDto newLeave = new LeaveDto(1L, null, 10_000L, null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		addLeave404(newLeave);
		List<LeaveDto> leaves = getAllLeaves();
		assertThat(leaves.size()).isEqualTo(0);
	}
	
	@Test
	void testThatALeaveCanBeApproved() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long leaveId = addLeaveOk(newLeave).getId();
		long approverId = employees.get(1).getId();
		
		approveLeaveOk(leaveId, approverId, true);
		
		LeaveDto leave = getALeaveByIdOk(leaveId);
		assertThat(leave.getApproved()).isEqualTo(true);
		assertThat(leave.getApproverId()).isEqualTo(approverId);
		assertThat(leave.getApproveDateTime()).isNotNull();
	}
	
	@Test
	void testThatAnInvalidLeaveCannotBeApproved() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long leaveId = addLeaveOk(newLeave).getId();
		long approverId = employees.get(1).getId();
		
		approveLeave404(10_000L, approverId, true);
		
		LeaveDto leave = getALeaveByIdOk(leaveId);
		assertThat(leave.getApproved()).isNull();
		assertThat(leave.getApproverId()).isNull();
		assertThat(leave.getApproveDateTime()).isNull();
	}
	
	@Test
	void testThatALeaveCannotBeApprovedByAnInvalidEmployee() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long leaveId = addLeaveOk(newLeave).getId();
		
		approveLeave404(leaveId, 10_000L, true);
		
		LeaveDto leave = getALeaveByIdOk(leaveId);
		assertThat(leave.getApproved()).isNull();
		assertThat(leave.getApproverId()).isNull();
		assertThat(leave.getApproveDateTime()).isNull();
	}
	
	@Test
	void testThatANotApprovedLeaveCanBeModified() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long leaveId = addLeaveOk(newLeave).getId();
		
		LeaveDto modifiedLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2031, 5, 9, 10, 0), LocalDateTime.of(2031, 5, 10, 10, 0));
		modifyLeaveOk(leaveId, modifiedLeave);
		
		LeaveDto leaveAfterModification = getALeaveByIdOk(leaveId);
		assertThat(leaveAfterModification.getStartOfLeave()).isEqualTo(modifiedLeave.getStartOfLeave());
		assertThat(leaveAfterModification.getEndOfLeave()).isEqualTo(modifiedLeave.getEndOfLeave());
		assertThat(leaveAfterModification.getEmployeeId()).isEqualTo(modifiedLeave.getEmployeeId());
	}
	
	@Test
	void testThatAnApprovedLeaveCannotBeModified() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long leaveId = addLeaveOk(newLeave).getId();
		long approverId = employees.get(1).getId();
		approveLeaveOk(leaveId, approverId, true);
		
		LeaveDto modifiedLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2031, 5, 9, 10, 0), LocalDateTime.of(2031, 5, 10, 10, 0));
		
		modifyLeave405(leaveId, modifiedLeave);
	}
	
	@Test
	void testThatAnInvalidLeaveCannotBeModified() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long leaveId = addLeaveOk(newLeave).getId();
		
		LeaveDto modifiedLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2031, 5, 9, 10, 0), LocalDateTime.of(2031, 5, 10, 10, 0));
		
		modifyLeave404(10_000L, modifiedLeave);
	}
	
	@Test
	void testThatANotApprovedLeaveCanBeDeleted() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long leaveId = addLeaveOk(newLeave).getId();
		
		deleteLeaveOk(leaveId);
		
		List<LeaveDto> leavesAfterModification = getAllLeaves();
		assertThat(leavesAfterModification.isEmpty()).isEqualTo(true);
	}
	
	@Test
	void testThatAnApprovedLeaveCannotBeDeleted() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long leaveId = addLeaveOk(newLeave).getId();
		long approverId = employees.get(1).getId();
		approveLeaveOk(leaveId, approverId, true);
				
		deleteLeave405(leaveId);
		
		List<LeaveDto> leavesAfterModification = getAllLeaves();
		assertThat(leavesAfterModification.size()).isEqualTo(1);
	}
	
	@Test
	void testThatAnInvalidLeaveCannotBeDeleted() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		addLeaveOk(newLeave);
		
		deleteLeave404(10_000L);
		
		List<LeaveDto> leavesAfterModification = getAllLeaves();
		assertThat(leavesAfterModification.size()).isEqualTo(1);
	}
	
	@Test
	void testThatAnInvalidLeaveCannotBeFound() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		addLeaveOk(newLeave);
		
		getALeaveById404(10_000L);
	}
	
	@Test
	void testThatAnEmptyExapmleGivesAllLeaves() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave1 = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave1Id = addLeaveOk(newLeave1).getId();
		LeaveDto newLeave2 = new LeaveDto(2L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave2Id = addLeaveOk(newLeave2).getId();
		LeaveDto newLeave3 = new LeaveDto(3L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave3Id = addLeaveOk(newLeave3).getId();
		LeaveDto newLeave4 = new LeaveDto(4L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave4Id = addLeaveOk(newLeave4).getId();
	
		LeaveExampleDto example = new LeaveExampleDto(0, null, null, null, null, null, null, null);
	
		List<LeaveDto> result = getLeavesByExampleOk(example, 0, 100, "");
		
		assertThat(result.size()).isEqualTo(4);
	}
	
	@Test
	void testThatAnEmptyExapmleGivesAllLeavesWithPaging() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave1 = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave1Id = addLeaveOk(newLeave1).getId();
		LeaveDto newLeave2 = new LeaveDto(2L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave2Id = addLeaveOk(newLeave2).getId();
		LeaveDto newLeave3 = new LeaveDto(3L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave3Id = addLeaveOk(newLeave3).getId();
		LeaveDto newLeave4 = new LeaveDto(4L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave4Id = addLeaveOk(newLeave4).getId();
	
		LeaveExampleDto example = new LeaveExampleDto(0, null, null, null, null, null, null, null);
	
		List<LeaveDto> result = getLeavesByExampleOk(example, 1, 3, "");
		
		assertThat(result.size()).isEqualTo(1);
	}
	
	@Test
	void testThatWeCanSearchByOverlappingTime() throws Exception {
		List<Employee> employees = employeeRepository.findAll();
		LeaveDto newLeave1 = new LeaveDto(1L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave1Id = addLeaveOk(newLeave1).getId();
		LeaveDto newLeave2 = new LeaveDto(2L, null, employees.get(0).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave2Id = addLeaveOk(newLeave2).getId();
		LeaveDto newLeave3 = new LeaveDto(3L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2021, 5, 9, 10, 0), LocalDateTime.of(2021, 5, 10, 10, 0));
		long newLeave3Id = addLeaveOk(newLeave3).getId();
		LeaveDto newLeave4 = new LeaveDto(4L, null, employees.get(1).getId(), null, null, null, LocalDateTime.of(2023, 5, 9, 10, 0), LocalDateTime.of(2023, 5, 10, 10, 0));
		long newLeave4Id = addLeaveOk(newLeave4).getId();
	
		LeaveExampleDto example = new LeaveExampleDto(0, null, null, null, null, null, LocalDateTime.of(2020,1,1,10,0,0), LocalDateTime.of(2025,1,1,10,0,0));
		List<LeaveDto> result = getLeavesByExampleOk(example, 0, 100, "");
		assertThat(result.size()).isEqualTo(4);
		
		example = new LeaveExampleDto(0, null, null, null, null, null, LocalDateTime.of(2023,1,1,10,0,0), LocalDateTime.of(2025,1,1,10,0,0));
		result = getLeavesByExampleOk(example, 0, 100, "");
		assertThat(result.size()).isEqualTo(2);
		
		example = new LeaveExampleDto(0, null, null, null, null, null, LocalDateTime.of(2024,1,1,10,0,0), LocalDateTime.of(2025,1,1,10,0,0));
		result = getLeavesByExampleOk(example, 0, 100, "");
		assertThat(result.size()).isEqualTo(0);
		
		example = new LeaveExampleDto(0, null, null, null, null, null, LocalDateTime.of(2010,1,1,10,0,0), LocalDateTime.of(2012,1,1,10,0,0));
		result = getLeavesByExampleOk(example, 0, 100, "");
		assertThat(result.size()).isEqualTo(0);	
	}
	
	
	
	
	
	private List<LeaveDto> getAllLeaves() {
		return webTestClient
				.get()
				.uri(LEAVES_BASE_URI)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private LeaveDto getALeaveByIdOk(long id) {
		return webTestClient
				.get()
				.uri(LEAVES_BASE_URI + "/" + id)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private void getALeaveById404(long id) {
		webTestClient
				.get()
				.uri(LEAVES_BASE_URI + "/" + id)
				.exchange()
				.expectStatus()
				.isNotFound();	
	}
	
	private List<LeaveDto> getLeavesByExampleOk(LeaveExampleDto example, Integer pageNo, Integer pageSize, String sortBy) {
		return webTestClient
				.post()
				.uri(LEAVES_BASE_URI + "/search/?pageNo=" + pageNo + "&pageSize=" + pageSize + "&sortBy=" + sortBy)
				.bodyValue(example)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBodyList(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private LeaveDto addLeaveOk(LeaveDto leave) {
		return webTestClient
				.post()
				.uri(LEAVES_BASE_URI)
				.bodyValue(leave)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private void addLeave404(LeaveDto leave) {
		webTestClient
				.post()
				.uri(LEAVES_BASE_URI)
				.bodyValue(leave)
				.exchange()
				.expectStatus()
				.isNotFound();
	}
	
	private LeaveDto modifyLeaveOk(long id, LeaveDto leave) {
		return webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" +id)
				.bodyValue(leave)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private void modifyLeave404(long id, LeaveDto leave) {
		webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" +id)
				.bodyValue(leave)
				.exchange()
				.expectStatus()
				.isNotFound();
	}
	
	private void modifyLeave405(long id, LeaveDto leave) {
		webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" +id)
				.bodyValue(leave)
				.exchange()
				.expectStatus()
				.isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	private void deleteLeaveOk(long id) {
		webTestClient
				.delete()
				.uri(LEAVES_BASE_URI + "/" +id)
				.exchange()
				.expectStatus()
				.isOk();
	}
	
	private void deleteLeave404(long id) {
		webTestClient
				.delete()
				.uri(LEAVES_BASE_URI + "/" +id)
				.exchange()
				.expectStatus()
				.isNotFound();
	}
	
	private void deleteLeave405(long id) {
		webTestClient
				.delete()
				.uri(LEAVES_BASE_URI + "/" +id)
				.exchange()
				.expectStatus()
				.isEqualTo(HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	private LeaveDto approveLeaveOk(long id, long approvalId, boolean status) {
		return webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" + id + "/approval?status=" + status + "&approvalId=" + approvalId)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(LeaveDto.class)
				.returnResult()
				.getResponseBody();
	}
	
	private void approveLeave404(long id, long approvalId, boolean status) {
		webTestClient
				.put()
				.uri(LEAVES_BASE_URI + "/" + id + "/approval?status=" + status + "&approvalId=" + approvalId)
				.exchange()
				.expectStatus()
				.isNotFound();
	}
	
	
}
