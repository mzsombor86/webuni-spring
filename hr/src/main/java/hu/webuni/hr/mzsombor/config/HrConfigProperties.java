package hu.webuni.hr.mzsombor.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigProperties {

	
	
	private Raise raise = new Raise();

	private boolean test;
	
	public static class Raise {

		private Default def = new Default();
		private Smart smart = new Smart();

		public Default getDef() {
			return def;
		}

		public void setDef(Default def) {
			this.def = def;
		}

		public Smart getSmart() {
			return smart;
		}

		public void setSmart(Smart smart) {
			this.smart = smart;
		}

	}

	public static class Default {
		private int percent;

		public int getPercent() {
			return percent;
		}

		public void setPercent(int percent) {
			this.percent = percent;
		}

	}

	public static class Smart {
		private String years;
		private String percents;

		public String getYears() {
			return years;
		}

		public void setYears(String years) {
			this.years = years;
		}

		public String getPercents() {
			return percents;
		}

		public void setPercents(String percents) {
			this.percents = percents;
		}

	}

	public Raise getRaise() {
		return raise;
	}

	public void setRaise(Raise raise) {
		this.raise = raise;
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}
	
	

}
