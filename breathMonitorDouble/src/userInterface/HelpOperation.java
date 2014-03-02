package userInterface;

import java.util.StringTokenizer;

public class HelpOperation implements Operation {

	@Override
	public void callOp(final StringTokenizer commandTokenizer, final String line) {
		ConsoleUserInterface.printUsage();
	}

}
