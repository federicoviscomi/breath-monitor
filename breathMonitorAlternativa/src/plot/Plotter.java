package plot;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class Plotter {

	private final Plot2DPanel plot2DPanel;
	private final JFrame frame;

	public Plotter() {
		// create your PlotPanel (you can use it as a JPanel)
		plot2DPanel = new Plot2DPanel();
		// put the PlotPanel in a JFrame, as a JPanel
		frame = new JFrame("a plot panel");
		frame.setContentPane(plot2DPanel);
		final Dimension screenSize = Toolkit.getDefaultToolkit()
				.getScreenSize();
		final int width = (int) (screenSize.getWidth() * 0.8);
		final int height = (int) (screenSize.getHeight() * 0.8);
		frame.setBounds(100, 100, width, height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void plotLine(final double[] x, final double[] y) {
		// add a line plot to the PlotPanel
		plot2DPanel.addLinePlot("my plot", x, y);
	}

	public void plotPoints(final double[] x, final double[] y) {
		// add a line plot to the PlotPanel
		plot2DPanel.addScatterPlot("my plot", x, y);
	}

}
