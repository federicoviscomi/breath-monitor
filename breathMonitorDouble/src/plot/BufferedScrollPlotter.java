package plot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import util.CircularBuffer;
import util.Util;


/**
 * 
 * e' solo una bozza!
 * non funziona!!!!
 * 
 * @author federico
 *
 */
public class BufferedScrollPlotter implements PlotterInterface {

	public static void main(final String[] args) {
		final BufferedScrollPlotter p = new BufferedScrollPlotter("sdf",
				new JFrame(), 2 << 5);
		final double[] y = { 0, 1, 1, 1.2, 4, 5, 6, 7, 8, 9, 10 };
		// p.plotLines(y, 0, y.length, Color.BLACK);
		p.plotPoints(y, 0, y.length, Color.BLACK);
		// p.plotBeatFound(Color.GREEN);
	}

	Plot2DPanel plot2DPanel;

	JFrame frame;

	private double time;

	private static double TIME_INCREMENT = 1;
	private double maxVal = Double.MIN_VALUE;
	private double minVal = Double.MAX_VALUE;

	private int timeBound = 2 << 11;
	private int scrollTime;
	private final int windowSize;
	private LinkedBlockingQueue<Pair> cb;

	private static class Pair {
		double[] x;
		double[] y;
	}

	public BufferedScrollPlotter(final String name, JFrame frame, int windowSize) {
		this.windowSize = windowSize;
		this.scrollTime = windowSize;
		this.plot2DPanel = new Plot2DPanel();
		// put the PlotPanel in a JFrame, as a JPanel
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setBounds(50, 50, 800, 600);
		frame.setContentPane(plot2DPanel);
		time = 0;
		if (time > timeBound) {
			System.err.println("DiscreteTimePlotter.timeBound");
		}
		cb = new LinkedBlockingQueue<BufferedScrollPlotter.Pair>(timeBound
				/ windowSize);
	}

	@Override
	public void plotBeatFound(final Color color) {
		// aggiungere un grafico diverso ma con la stessa scala delle ascisse!
		final double[] x = { time };
		final double[] y = { 0.3 };
		// System.err.println("time " + time + ", beat " + color);
		time = time + TIME_INCREMENT;
		if (time > timeBound) {
			System.err.println("DiscreteTimePlotter.timeBound");
		}
		plot2DPanel.addScatterPlot("my plot", color, x, y);
	}

	@Override
	public void plotLines(final double[] y, final int offset, final int length,
			final Color color) {
		plot2DPanel.addLinePlot("my plot", color,
				Util.linspace(length, time, TIME_INCREMENT),
				Util.trim(y, offset, length));
		// plot2DPanel.addLinePlot(color,
		// Util.linspace(length, time, TIME_INCREMENT), 0, y, offset,
		// length);
		// System.err.print("time: " + time);
		time = time + (TIME_INCREMENT * length);
		// System.err.println(" -> " + time);
		for (int i = offset; i < (offset + length); i++) {
			if (maxVal < y[i]) {
				maxVal = y[i];
			}
			if (minVal > y[i]) {
				minVal = y[i];
			}
		}
		if (time > timeBound) {
			System.err.println("DiscreteTimePlotter.timeBound");
		}
	}

	@Override
	public void plotPoints(final double[] y, final int offset, final int length) {
		plot2DPanel.addScatterPlot("my plot",
				Util.linspace(length, time, TIME_INCREMENT),
				Util.trim(y, offset, length));

		// plot2DPanel.addScatterPlot(color,
		// Util.linspace(length, time, TIME_INCREMENT), 0, y, offset,
		// length);
		// System.err.print("time: " + time);
		time = time + (TIME_INCREMENT * length);
		// System.err.println(" -> " + time);
		for (int i = offset; i < (offset + length); i++) {
			if (maxVal < y[i]) {
				maxVal = y[i];
			}
			if (minVal > y[i]) {
				minVal = y[i];
			}
		}
	}

	@Override
	public void plotPoints(final double[] y, final int offset,
			final int length, final Color color) {
		plot2DPanel.addScatterPlot("my plot", color,
				Util.linspace(length, time, TIME_INCREMENT),
				Util.trim(y, offset, length));

		// plot2DPanel.addScatterPlot(color,
		// Util.linspace(length, time, TIME_INCREMENT), 0, y, offset,
		// length);
		// System.err.print("time: " + time);
		time = time + (TIME_INCREMENT * length);
		// System.err.println(" -> " + time);
		for (int i = offset; i < (offset + length); i++) {
			if (maxVal < y[i]) {
				maxVal = y[i];
			}
			if (minVal > y[i]) {
				minVal = y[i];
			}
		}
		if (time > timeBound) {
			System.err.println("DiscreteTimePlotter.timeBound");
		}
	}

	@Override
	public void shutDown() {
		frame.removeAll();
		frame = null;
		plot2DPanel.removeAll();
		plot2DPanel = null;
	}

	@Override
	public void plotPoints(final double[] x, final double[] y,
			final Color colore) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void plotLines(final double[] x, final double[] y, final Color color) {
		throw new UnsupportedOperationException();
	}

}
