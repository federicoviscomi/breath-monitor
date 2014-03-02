package userInterface;

import java.io.IOException;

public class ConsoleIO implements InputUserInterface, OutUserInterface {

	@Override
	public void in_WaitForUserToStopAlarm() throws IOException {
		System.out.println("Press a key to stop alarm");
		System.in.read();
	}

	@Override
	public void out_showBreathingState(double elapsedTime,
			boolean breathingState) {
		if (breathingState) {
			System.out.println("time " + elapsedTime + "s: breathing");
		} else {
			System.out.println("time " + elapsedTime + "s: not breathing");
		}
	}

	@Override
	public void out_startAlarm() {
		System.err.println("alarm!");
	}

	@Override
	public void out_stopAlarm() {
		System.err.println("alarm stopped");
	}

}
