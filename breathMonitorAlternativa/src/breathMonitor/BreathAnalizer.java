package breathMonitor;

import javax.sound.sampled.AudioFormat;

public class BreathAnalizer {

	private int trainingTime;

	private double averageMax;

	private boolean breathing;

	public static final int TRAINING_THRESHOLD = 10;

	private static final double BREATHING_THRESHOLD = 0.4;

	public static double average(final byte[] arr) {
		double avg = 0;
		for (int i = 0; i < arr.length; i++) {
			avg += arr[i];
		}
		avg /= arr.length;
		return avg;
	}

	public BreathAnalizer(final AudioFormat format) {
		trainingTime = 0;
		averageMax = -1;
	}

	/**
	 * 
	 * buffer[offset ... (offset+length)] must be not empty
	 * 
	 * @param buffer
	 * @param offset
	 * @param length
	 */
	public void detect(final byte[] buffer, final int offset, final int length) {
		int min = buffer[offset];
		int max = buffer[offset];
		for (int i = offset; i < (offset + length); i++) {
			if (buffer[i] < min) {
				min = buffer[i];
			} else if (buffer[i] > max) {
				max = buffer[i];
			}
		}
		//System.err.println(" " + java.util.Arrays.toString(buffer));
		// initialize
		if (averageMax == -1) {
			averageMax = max;
		}
		breathing = (max >= (BREATHING_THRESHOLD * averageMax));
		averageMax = (averageMax + max) / 2;
		System.out.println("avg " + averageMax + " max " + max);
	}

	public boolean isBreathing() {
		return breathing;
	}

}
