package plot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SimpleDiscreteTimePlotter implements PlotterInterface {

	private static class Points extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -4519726532571339307L;

		public Points() {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void paintComponent(final Graphics g) {
			super.paintComponent(g);

			final Graphics2D g2d = (Graphics2D) g;

			g2d.setColor(Color.red);

			for (int i = 0; i <= 100000; i++) {
				final Dimension size = getSize();
				final int w = size.width;
				final int h = size.height;

				final Random r = new Random();
				final int x = Math.abs(r.nextInt()) % w;
				final int y = Math.abs(r.nextInt()) % h;
				g2d.drawLine(x, y, x, y);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		final Points points = new Points();
		final JFrame frame = new JFrame("Points");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(points);
		frame.setSize(250, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private final JPanel jPanel;

	private final JFrame frame;

	public SimpleDiscreteTimePlotter() {
		jPanel = new JPanel();
		final Dimension screenSize = Toolkit.getDefaultToolkit()
				.getScreenSize();
		final int width = (int) (screenSize.getWidth() * 0.7);
		final int height = (int) (screenSize.getHeight() * 0.7);

		// put the PlotPanel in a JFrame, as a JPanel
		frame = new JFrame("");
		frame.setContentPane(jPanel);

		frame.setBounds(100, 100, width, height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void plotBeatFound(final Color red) {
		// TODO Auto-generated method stub

	}

	@Override
	public void plotLines(final double[] x, final double[] y, final Color color) {
		// TODO Auto-generated method stub

	}

	@Override
	public void plotLines(final double[] buffer, final int i, final int read,
			final Color color) {
		// TODO Auto-generated method stub

	}

	@Override
	public void plotPoints(final double[] x, final double[] fs,
			final Color color) {
		// TODO Auto-generated method stub

	}

	@Override
	public void plotPoints(final double[] buffer, final int startIndex,
			final int size) {
		// TODO Auto-generated method stub

	}

	@Override
	public void plotPoints(final double[] ds, final int i, final int j,
			final Color color) {
		new Point2D.Double(3, 5);

	}

	@Override
	public void shutDown() {
		// TODO Auto-generated method stub

	}

}
