package plot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

import util.Util;

public class DiscreteTimePlotter implements PlotterInterface {

	public static void main(final String[] args) {
		final DiscreteTimePlotter p = new DiscreteTimePlotter("sdf");
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

	/**
	 * Valore massimo consigliato. In una versione successiva del software
	 * bisogna migliorare questa interfaccia grafica. Dopo questo valore si
	 * cancella il grafico e si riparte da zero oppure si usa una soluzione
	 * migliore, ad esempio un grafico che scorre o due pannelli diversi, uno
	 * per il vecchio e uno per il nuovo.
	 */
	private int timeBound = 2 << 17;

	public DiscreteTimePlotter(final String name) {
		// create your PlotPanel (you can use it as a JPanel)
		plot2DPanel = new Plot2DPanel();
		final Dimension screenSize = Toolkit.getDefaultToolkit()
				.getScreenSize();
		final int width = (int) (screenSize.getWidth() * 0.7);
		final int height = (int) (screenSize.getHeight() * 0.7);

		// put the PlotPanel in a JFrame, as a JPanel
		frame = new JFrame(name);
		frame.setContentPane(plot2DPanel);
		frame.setBounds(100, 100, width, height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		time = 0;
	}

	public DiscreteTimePlotter(final String name, JFrame frame) {
		this.plot2DPanel = new Plot2DPanel();
		// put the PlotPanel in a JFrame, as a JPanel
		frame.setContentPane(plot2DPanel);
		time = 0;
		if (time > timeBound) {
			System.err.println("DiscreteTimePlotter.timeBound");
		}
	}

	@Override
	public void plotBeatFound(final Color color) {
		// aggiungere un grafico diverso ma con la stessa scala delle ascisse!
		final double[] x = { time };
		final double[] y = { 0.3 };
		// System.err.println("time " + time + ", beat " + color);
		plot2DPanel.addScatterPlot("my plot", color, x, y);
		time = time + TIME_INCREMENT;
		if (time > timeBound) {
			System.err.println("DiscreteTimePlotter.timeBound");
		}
	}

	@Override
	public void plotLines(final double[] x, final double[] y, final Color color) {
		throw new UnsupportedOperationException();
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
	public void plotPoints(final double[] x, final double[] y,
			final Color colore) {
		throw new UnsupportedOperationException();
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
}
