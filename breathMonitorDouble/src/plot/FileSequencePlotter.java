package plot;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class FileSequencePlotter implements PlotterInterface {

	private final PrintWriter out;

	public FileSequencePlotter(final String fileName)
			throws FileNotFoundException {
		out = new PrintWriter(new FileOutputStream(new File(fileName)));
	}

	@Override
	public void plotBeatFound(final Color color) {
		// TODO Auto-generated method stub
	}

	@Override
	public void plotLines(final double[] x, final double[] y, final Color color) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void plotLines(final double[] y, final int offset, final int length,
			final Color color) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void plotPoints(final double[] x, final double[] y, final Color color) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void plotPoints(final double[] y, final int offset, final int length) {
		for (int i = offset; i < (offset + length); i++) {
			for (int j = 0; j < y[i]; j++) {
				out.print("*");
			}
			out.print("*\n");
		}
	}

	@Override
	public void plotPoints(final double[] y, final int offset,
			final int length, final Color color) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void shutDown() {
		out.close();
	}

}
