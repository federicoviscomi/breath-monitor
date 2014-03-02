package persistencyManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 * da finire e testare
 * 
 * @author federico
 * 
 */
public class DatabaseManager {
	private static String tableName = "breathingData";
	private Connection conn = null;

	public DatabaseManager(final String databaseURL) {
		try {
			Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
			// Get a connection
			conn = DriverManager.getConnection(databaseURL);
		} catch (final Exception except) {
			except.printStackTrace();
		}
	}

	public void addEntry(final String personName, final Date date)
			throws SQLException {
		insertIntoBreathingData(personName, date);
		addNewMonitoring(personName, date);
	}

	public boolean addFreq(final String personName, final Date date,
			final double freq) throws SQLException {
		final Statement stm = conn.createStatement();
		final boolean result = stm.execute("insert into " + personName + date
				+ " values ('" + freq + "')");
		stm.close();
		return result;
	}

	private boolean addNewMonitoring(final String personName, final Date date)
			throws SQLException {
		final Statement stm = conn.createStatement();
		final boolean result = stm.execute("add table " + personName + date
				+ " (DOUBLE breathFreq)");
		stm.close();
		return result;
	}

	private boolean insertIntoBreathingData(final String personName,
			final Date date) throws SQLException {
		final Statement stm = conn.createStatement();
		final boolean result = stm.execute("insert into " + tableName
				+ " values (" + personName + "','" + date.toString() + "')");
		stm.close();
		return result;
	}

	public ArrayList<Double> retreiveAll(final String personName,
			final Date date) throws SQLException {
		final ArrayList<Double> freqs = new ArrayList<Double>();
		final Statement stm = conn.createStatement();
		final ResultSet resultSet = stm.executeQuery("SELECT breathFreq FROM "
				+ personName + date);
		while (resultSet.next()) {
			freqs.add(resultSet.getDouble(1));
		}
		stm.close();
		return freqs;
	}

	public void shutdown() {
		try {
			if (conn != null) {
				conn.close();
			}
		} catch (final SQLException sqlExcept) {
			// do nothig
		}
	}
}
