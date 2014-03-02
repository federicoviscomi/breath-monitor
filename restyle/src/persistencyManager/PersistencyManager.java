package persistencyManager;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PersistencyManager implements PersistencyInterface {

	private enum Mode {
		WRITE_OUTPUT_TO_LOCAL_FILE, WRITE_OUTPUT_TO_FTP_FILE
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		// TODO Auto-generated method stub

	}

	public static BreathingData read(final InetAddress inputFileAddress)
			throws FileNotFoundException, IOException {
		final BreathingData bd = new BreathingData(null, null);
		// TODO
		return bd;
	}

	public static BreathingData read(final String inputFileName)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		final BreathingData bd = new BreathingData(null, null);
		final ObjectInputStream ois = new ObjectInputStream(
				new FileInputStream(new File(inputFileName)));
		bd.personName = (String) ois.readObject();
		bd.date = (Date) ois.readObject();
		bd.breathFrequencyList = new ArrayList<Double>();
		boolean endOfFileReached = false;
		do {
			try {
				bd.breathFrequencyList.add(ois.readDouble());
			} catch (final EOFException e) {
				endOfFileReached = true;
			}
		} while (!endOfFileReached);

		return bd;
	}

	private final BreathingData breathingData;
	private final Mode mode;

	private ObjectOutputStream oos;

	private long time;

	private final Calendar calendar;

	public PersistencyManager(final String personName,
			final InetAddress ftpOutputAddress) {
		calendar = new GregorianCalendar();
		breathingData = new BreathingData(personName, calendar.getTime());
		mode = Mode.WRITE_OUTPUT_TO_FTP_FILE;
		// TODO
	}

	public PersistencyManager(final String personName,
			final String outputFileName) throws FileNotFoundException,
			IOException {
		calendar = new GregorianCalendar();
		breathingData = new BreathingData(personName, calendar.getTime());
		mode = Mode.WRITE_OUTPUT_TO_LOCAL_FILE;
		oos = new ObjectOutputStream(new FileOutputStream(new File(
				outputFileName)));
		oos.writeObject(personName);
		oos.writeObject(calendar.getTime());
	}

	@Override
	public void addData(final BreathingData data) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	private boolean aMinuteElapsed() {
		boolean aMinuteElapsed = false;
		if (((System.currentTimeMillis() - time) / 1000) >= 60) {
			aMinuteElapsed = true;
			time = System.currentTimeMillis();
		}
		return aMinuteElapsed;
	}

	@Override
	public void close() {
		if (mode.equals(Mode.WRITE_OUTPUT_TO_FTP_FILE)) {
			// TODO
		} else if (mode.equals(Mode.WRITE_OUTPUT_TO_LOCAL_FILE)) {
			try {
				oos.close();
			} catch (final IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// 0throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void init() {
		throw new UnsupportedOperationException("not implemented yet");
	}

	@Override
	public void newRecording(final String name, final Date date) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void write(final double breathFrequency) throws IOException {
		// TODO
		// System.err.println(" breath frequency: " + breathFrequency);
		breathingData.write(breathFrequency);
		if (aMinuteElapsed()) {
			for (final double bf : breathingData.breathFrequencyList) {
				oos.writeDouble(bf);
			}
			breathingData.breathFrequencyList.clear();
		}
	}
}
