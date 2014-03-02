package persistencyManager;

import java.util.ArrayList;
import java.util.Date;

public class BreathingData {
	public String personName;

	public Date date;

	public ArrayList<Double> breathFrequencyList;

	public BreathingData(final String personName, final Date date) {
		this.personName = personName;
		this.date = date;
		breathFrequencyList = new ArrayList<Double>();
	}

	public void write(final double breathFrequency) {
		breathFrequencyList.add(breathFrequency);
	}
}