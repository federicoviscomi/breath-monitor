package persistencyManager;

import java.util.Date;

public interface PersistencyInterface {
	public void addData(BreathingData data);

	public void close();

	public void init();

	public void newRecording(String name, Date date);
}
