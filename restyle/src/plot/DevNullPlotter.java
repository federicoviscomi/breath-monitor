package plot;

import java.awt.Color;

/**
 * A dummy plotter that does not plot anything.
 * 
 * @author federico
 * 
 */
public class DevNullPlotter implements PlotterInterface {

	@Override
	public void plotBeatFound(final Color color) {
		// do nothing
	}

	@Override
	public void plotLines(final double[] x, final double[] y, final Color color) {
		// do nothing
	}

	@Override
	public void plotLines(final double[] y, final int offset, final int read,
			final Color color) {
		// do nothing
	}

	@Override
	public void plotPoints(final double[] x, final double[] y, final Color color) {
		// do nothing
	}

	@Override
	public void plotPoints(final double[] buffer, final int offset,
			final int length) {
		// do nothing
	}

	@Override
	public void plotPoints(final double[] ds, final int offset,
			final int length, final Color color) {
		// do nothing
	}

	@Override
	public void shutDown() {
		// do nothing
	}

}
