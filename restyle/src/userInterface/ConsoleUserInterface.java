package userInterface;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import plot.DevNullPlotter;

public class ConsoleUserInterface {

	private static final class OperationRecord {
		public final String first;

		public final Operation second;

		private final String commandUsage;

		private final String commandString;

		public OperationRecord(final String first, final Operation second,
				String commandUsage, String commandString) {
			this.first = first;
			this.second = second;
			this.commandUsage = commandUsage;
			this.commandString = commandString;
		}

		public String commandString() {
			return commandString;
		}

		public String commandUsage() {
			return commandUsage;
		}

	}

	static Thread breathMonitorThread;

	private static final OperationRecord[] ops = {
			new OperationRecord("monitor", new OperationMonitor(
					new DevNullPlotter()), "monitor -f filename",
					"start breath recognition on given file name"),
			new OperationRecord("exit", new ExitOperation(), "exit",
					"exit application"),
			new OperationRecord("list", new ListDirectoryOperation(), "list",
					"list working directory content"),
			new OperationRecord("help", new HelpOperation(), "help",
					"print this help"),
			new OperationRecord("stop", new StopOperation(), "stop",
					"stop current monitoring if any") };

	public static void main(final String[] args) {
		try {
			System.out
					.println("Breath Monitor Beta version\nby Federico Viscomi\ntype help for a command list\n");
			final BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String read = null;
			while (true) {

				/* display $ and read a command line */
				StringTokenizer commandTokenizer;
				do {
					System.out.print(System.getProperty("user.dir") + "$ ");
					read = in.readLine();
					read = read.trim();
					commandTokenizer = new StringTokenizer(read);
				} while ((read == null) || read.trim().equals("")
						|| !commandTokenizer.hasMoreTokens());

				/* parse command and call corresponding operation */
				boolean recognized = false;
				final String command = commandTokenizer.nextToken();
				for (final OperationRecord p : ops) {
					if (command.startsWith(p.first)) {
						p.second.callOp(commandTokenizer, read);
						recognized = true;
						break;
					}
				}

				if (!recognized) {
					System.out.println("unrecognized command: " + read);
				}

			}
		} catch (final Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public static void printUsage() {
		StringBuilder format = new StringBuilder();
		ArrayList<String> args = new ArrayList<String>();
		for (int i = 0; i < ops.length; i++) {
			format.append("\n%-20s\t  %-30s");
			args.add(ops[i].commandString());
			args.add(ops[i].commandUsage());
		}
		System.out.format(format.toString() + "\n", args.toArray());
	}
}
