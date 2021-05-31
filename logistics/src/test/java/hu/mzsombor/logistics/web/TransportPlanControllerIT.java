package hu.mzsombor.logistics.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.mzsombor.logistics.config.LogisticsConfigProperties;
import hu.mzsombor.logistics.dto.DelayDto;
import hu.mzsombor.logistics.dto.LoginDto;
import hu.mzsombor.logistics.model.Milestone;
import hu.mzsombor.logistics.service.AddressService;
import hu.mzsombor.logistics.service.InitDBService;
import hu.mzsombor.logistics.service.MilestoneService;
import hu.mzsombor.logistics.service.SectionService;
import hu.mzsombor.logistics.service.TransportPlanService;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransportPlanControllerIT {

	private static final String TRANSPORTPLANS_BASE_URI = "/api/transportPlans";
	private static final String LOGIN_URI = "/api/login";

	private long transportPlanId;

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	AddressService addressService;

	@Autowired
	MilestoneService milestoneService;

	@Autowired
	SectionService sectionService;

	@Autowired
	TransportPlanService transportPlanService;

	@Autowired
	InitDBService initDBService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	LogisticsConfigProperties config;

	@BeforeEach
	public void prepareDB() {
		transportPlanId = initDBService.init().getId();
	}

	@Test
	void testThatWeCanLogin() throws Exception {
		loginAdminUser();
	}

	@Test
	void testThatWeCannotLoginWithBadCredentials() throws Exception {
		loginWithJwtNotOk("baduser", "badpassword");
		loginWithJwtNotOk("admin", "badpassword");
		loginWithJwtNotOk("baduser", "passAdmin");
	}

	@Test
	void testThatWeCannotAddDelayWithInsufficientRights() throws Exception {
		String jwtToken = loginAddressUser();
		addDelayToATransportPlan403(1, 1, 1, jwtToken);
	}

	@Test
	void testThatWeCannotAddDelayToANonExistingTransportPlanOrMilestone() throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		addDelayToATransportPlan404(transportPlanId, 0, 1, jwtToken);
		addDelayToATransportPlan404(0, milestoneId, 1, jwtToken);
		addDelayToATransportPlan404(0, 0, 1, jwtToken);
	}

	@Test
	void testThatWeCannotAddDelayToAMilestoneThatIsNotBoundedToTheCorrespondingTransportPlan() throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(milestones.size() - 1).getId();
		addDelayToATransportPlan400(transportPlanId, milestoneId, 1, jwtToken);
	}

	@Test
	void testThatDecreasedExpectedRevenueIsOkBelow30minutes() throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		long originalRevenue = transportPlanService.findById(transportPlanId).get().getExpectedRevenue();
		int delayInMinutes = 20;
		
		addDelayToATransportPlanOk(transportPlanId, milestoneId, delayInMinutes, jwtToken);
		long modifiedRevenue = transportPlanService.findById(transportPlanId).get().getExpectedRevenue();
		assertThat(modifiedRevenue).isEqualTo(
				(long) (originalRevenue * (100 - config.getRevenueDropPercentage().getBelow30minutes()) * 0.01));
	}

	@Test
	void testThatDecreasedExpectedRevenueIsOkBelow60minutes() throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		long originalRevenue = transportPlanService.findById(transportPlanId).get().getExpectedRevenue();
		int delayInMinutes = 50;
		
		addDelayToATransportPlanOk(transportPlanId, milestoneId, delayInMinutes, jwtToken);
		long modifiedRevenue = transportPlanService.findById(transportPlanId).get().getExpectedRevenue();
		assertThat(modifiedRevenue).isEqualTo(
				(long) (originalRevenue * (100 - config.getRevenueDropPercentage().getBelow60minutes()) * 0.01));
	}

	@Test
	void testThatDecreasedExpectedRevenueIsOkBelow120minutes() throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		long originalRevenue = transportPlanService.findById(transportPlanId).get().getExpectedRevenue();
		int delayInMinutes = 110;
		
		addDelayToATransportPlanOk(transportPlanId, milestoneId, delayInMinutes, jwtToken);
		long modifiedRevenue = transportPlanService.findById(transportPlanId).get().getExpectedRevenue();
		assertThat(modifiedRevenue).isEqualTo(
				(long) (originalRevenue * (100 - config.getRevenueDropPercentage().getBelow120minutes()) * 0.01));
	}

	@Test
	void testThatDecreasedExpectedRevenueIsOkAbove120minutes() throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> milestones = milestoneService.getAllMilestones();
		long milestoneId = milestones.get(0).getId();
		long originalRevenue = transportPlanService.findById(transportPlanId).get().getExpectedRevenue();
		int delayInMinutes = 150;
		
		addDelayToATransportPlanOk(transportPlanId, milestoneId, delayInMinutes, jwtToken);
		long modifiedRevenue = transportPlanService.findById(transportPlanId).get().getExpectedRevenue();
		assertThat(modifiedRevenue).isEqualTo(
				(long) (originalRevenue * (100 - config.getRevenueDropPercentage().getAbove120minutes()) * 0.01));
	}

	@Test
	void testThatIfWeModifyAMilestoneAtTheBeginningOfASectionThanTheMilestoneAtTheEndOfTheSectionWillBeAffectedAlso()
			throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> originalMilestones = milestoneService.getAllMilestones();
		long milestoneId = originalMilestones.get(0).getId();
		int delayInMinutes = 20;
		addDelayToATransportPlanOk(transportPlanId, milestoneId, delayInMinutes, jwtToken);
		List<Milestone> modifiedMilestones = milestoneService.getAllMilestones();
		assertThat(originalMilestones.get(0).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(0).getPlannedTime());
		assertThat(originalMilestones.get(1).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(1).getPlannedTime());
		assertThat(originalMilestones.get(2).getPlannedTime()).isEqualTo(modifiedMilestones.get(2).getPlannedTime());
	}

	@Test
	void testThatIfWeModifyAMilestoneAtTheEndOfASectionThanTheMilestoneAtTheBeginningOfTheNextSectionWillBeAffectedAlso()
			throws Exception {
		String jwtToken = loginTransportUser();
		List<Milestone> originalMilestones = milestoneService.getAllMilestones();
		long milestoneId = originalMilestones.get(1).getId();
		int delayInMinutes = 20;
		addDelayToATransportPlanOk(transportPlanId, milestoneId, delayInMinutes, jwtToken);
		List<Milestone> modifiedMilestones = milestoneService.getAllMilestones();
		assertThat(originalMilestones.get(1).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(1).getPlannedTime());
		assertThat(originalMilestones.get(2).getPlannedTime().plusMinutes(delayInMinutes))
				.isEqualTo(modifiedMilestones.get(2).getPlannedTime());
		assertThat(originalMilestones.get(3).getPlannedTime()).isEqualTo(modifiedMilestones.get(3).getPlannedTime());
	}

	private String loginAddressUser() {
		return loginWithJwtOk("addressUser", "passAddress");
	}

	private String loginTransportUser() {
		return loginWithJwtOk("transportUser", "passTransport");
	}

	private String loginAdminUser() {
		return loginWithJwtOk("admin", "passAdmin");
	}

	private String loginWithJwtOk(String username, String password) {
		LoginDto loginDto = new LoginDto(username, password);
		return webTestClient.post().uri(LOGIN_URI).bodyValue(loginDto).exchange().expectStatus().isOk()
				.expectBody(String.class).returnResult().getResponseBody();
	}

	private void loginWithJwtNotOk(String username, String password) {
		LoginDto loginDto = new LoginDto(username, password);
		webTestClient.post().uri(LOGIN_URI).bodyValue(loginDto).exchange().expectStatus().isForbidden();
	}

	private void addDelayToATransportPlanOk(long transportPlanId, long milestoneId, int delayInMinutes,
			String jwtToken) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient.post().uri(TRANSPORTPLANS_BASE_URI + "/" + transportPlanId + "/delay")
				.headers(headers -> headers.setBearerAuth(jwtToken)).bodyValue(delayDto).exchange().expectStatus()
				.isOk();
	}

	private void addDelayToATransportPlan400(long transportPlanId, long milestoneId, int delayInMinutes,
			String jwtToken) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient.post().uri(TRANSPORTPLANS_BASE_URI + "/" + transportPlanId + "/delay")
				.headers(headers -> headers.setBearerAuth(jwtToken)).bodyValue(delayDto).exchange().expectStatus()
				.isEqualTo(HttpStatus.BAD_REQUEST);
	}

	private void addDelayToATransportPlan403(long transportPlanId, long milestoneId, int delayInMinutes,
			String jwtToken) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient.post().uri(TRANSPORTPLANS_BASE_URI + "/" + transportPlanId + "/delay")
				.headers(headers -> headers.setBearerAuth(jwtToken)).bodyValue(delayDto).exchange().expectStatus()
				.isEqualTo(HttpStatus.FORBIDDEN);
	}

	private void addDelayToATransportPlan404(long transportPlanId, long milestoneId, int delayInMinutes,
			String jwtToken) {
		DelayDto delayDto = new DelayDto(milestoneId, delayInMinutes);
		webTestClient.post().uri(TRANSPORTPLANS_BASE_URI + "/" + transportPlanId + "/delay")
				.headers(headers -> headers.setBearerAuth(jwtToken)).bodyValue(delayDto).exchange().expectStatus()
				.isNotFound();
	}

}
