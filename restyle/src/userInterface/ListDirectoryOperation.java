package userInterface;

import java.io.File;
import java.util.StringTokenizer;

public class ListDirectoryOperation implements Operation {

	@Override
	public void callOp(final StringTokenizer commandTokenizer, final String line) {

		String currentWorkingDirectory = System.getProperty("user.dir");
		if (currentWorkingDirectory != null) {
			System.out.println(currentWorkingDirectory);
		} else {
			final File file = new File("");
			currentWorkingDirectory = file.getAbsolutePath();
			System.out.println(file.getAbsolutePath());
			file.delete();
		}

		final File folder = new File(currentWorkingDirectory);
		for (final String file : folder.list()) {
			System.out.println(file);
		}
	}

}
