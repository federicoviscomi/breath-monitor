package plot;

import java.awt.Color;

// TODO: every method that has an array as a parameter must also include an offset and a length specification
public interface PlotterInterface {
	public void plotBeatFound(Color color);

	public void plotLines(final double[] x, final double[] y, Color color);

	// public void plotLines(final double[] x, int offsetX, final double[] y,
	// inst offsetY, Color color);

	public void plotLines(double[] y, int offset, int length, Color color);

	public void plotPoints(final double[] x, final double[] y, Color color);

	// public void plotPoints(final double[] x, int offsetX, final double[] y,
	// int offsetY, int length, Color color);

	public void plotPoints(double[] y, int offset, int length);

	public void plotPoints(double[] y, int offset, int length, Color color);

	public void shutDown();
}
