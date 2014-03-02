package userInterface;

import java.util.StringTokenizer;

public class StopOperation implements Operation {

	@Override
	public void callOp(StringTokenizer commandTokenizer, String line) {
		if (ConsoleUserInterface.breathMonitorThread != null) {
			ConsoleUserInterface.breathMonitorThread.interrupt();
		} else {
			System.out.println("nothing to stop");
		}
		ConsoleUserInterface.breathMonitorThread = null;
	}

}
