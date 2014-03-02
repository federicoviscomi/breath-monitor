package userInterface;

import java.util.StringTokenizer;

public class StopOperation implements Operation {

	@Override
	public void callOp(StringTokenizer commandTokenizer, String line) {
		ConsoleUserInterface.breathMonitorThread.interrupt();
	}

}
