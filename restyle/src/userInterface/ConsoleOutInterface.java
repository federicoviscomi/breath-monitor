package userInterface;

public class ConsoleOutInterface implements OutUserInterface {

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
		// do nothing
	}

}
