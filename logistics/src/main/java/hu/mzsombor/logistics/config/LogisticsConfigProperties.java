package hu.mzsombor.logistics.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "logistics")
@Component
public class LogisticsConfigProperties {

	private RevenueDropPercentage revenueDropPercentage = new RevenueDropPercentage();

	private boolean test;

	public static class RevenueDropPercentage {

		private int below30minutes;
		private int below60minutes;
		private int below120minutes;
		private int above120minutes;

		public int getBelow30minutes() {
			return below30minutes;
		}

		public void setBelow30minutes(int below30minutes) {
			this.below30minutes = below30minutes;
		}

		public int getBelow60minutes() {
			return below60minutes;
		}

		public void setBelow60minutes(int below60minutes) {
			this.below60minutes = below60minutes;
		}

		public int getBelow120minutes() {
			return below120minutes;
		}

		public void setBelow120minutes(int below120minutes) {
			this.below120minutes = below120minutes;
		}

		public int getAbove120minutes() {
			return above120minutes;
		}

		public void setAbove120minutes(int above120minutes) {
			this.above120minutes = above120minutes;
		}

	}

	public RevenueDropPercentage getRevenueDropPercentage() {
		return revenueDropPercentage;
	}

	public void setRevenueDropPercentage(RevenueDropPercentage revenueDropPercentage) {
		this.revenueDropPercentage = revenueDropPercentage;
	}

	
	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}

}
