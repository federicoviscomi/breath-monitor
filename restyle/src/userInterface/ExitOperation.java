package userInterface;

import java.util.StringTokenizer;

public class ExitOperation implements Operation {

	@Override
	public void callOp(final StringTokenizer commandTokenizer, final String line) {
		System.out.println("\nbye\n");
		System.exit(0);
	}

}
