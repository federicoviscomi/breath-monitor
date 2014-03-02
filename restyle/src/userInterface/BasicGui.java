package userInterface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.plaf.FileChooserUI;

import filters.ByteToDoubleInputStream;

import breathMonitor.BreathMonitor;

import plot.DiscreteTimePlotter;
import wavFile.WavFile;
import wavFile.WavFileDoubleInputStream;
import wavFile.WavFileException;

/**
 * This class demonstrates the basics of setting up a Java Swing GUI uisng the
 * BorderLayout. You should be able to use this program to drop in other
 * components when building a GUI
 */
public class BasicGui {
	public class ListenCloseWdw extends WindowAdapter {
		@Override
		public void windowClosing(final WindowEvent e) {
			System.exit(0);
		}
	}

	public class ListenMenuLoadFile implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			try {
				JFileChooser fc = new JFileChooser();
				if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fc.getSelectedFile();
					boolean waveRader = true;
					String nextTestFileName = selectedFile.getCanonicalPath();
					ConsoleIO consoleIO = new ConsoleIO();
					if (waveRader) {
						final WavFileDoubleInputStream wavFileDoubleInputStream = new WavFileDoubleInputStream(
								WavFile.openWavFile(new File(nextTestFileName)));
						final BreathMonitor breathMonitor = new BreathMonitor(
								wavFileDoubleInputStream, AudioSystem
										.getAudioInputStream(
												new File(nextTestFileName))
										.getFormat(), nextTestFileName, true,
								plotter, consoleIO, consoleIO);
						breathMonitor.run();
						wavFileDoubleInputStream.close();
					} else {
						final AudioInputStream audioInputStream = AudioSystem
								.getAudioInputStream(new File(nextTestFileName));
						new BreathMonitor(new ByteToDoubleInputStream(
								audioInputStream),
								audioInputStream.getFormat(), nextTestFileName,
								true, plotter, consoleIO, consoleIO).run();
						audioInputStream.close();
					}
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				System.exit(-1);
			} catch (WavFileException e1) {
				e1.printStackTrace();
				System.exit(-1);
			} catch (UnsupportedAudioFileException e1) {
				e1.printStackTrace();
				System.exit(-1);
			} catch (Exception e1) {
				e1.printStackTrace();
				System.exit(-1);
			}
		}
	}

	public class ListenMenuQuit implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			System.exit(0);
		}
	}

	public static void main(final String args[]) {
		final BasicGui gui = new BasicGui();

	}

	private final JFrame f;
	private final JPanel pnlNorth;
	private final JPanel pnlSouth;
	private final JPanel pnlEast;
	private final JPanel pnlWest;
	private final JPanel pnlCenter;

	// Buttons some there is something to put in the panels
	// private final JButton btnNorth = new JButton("North");
	// private final JButton btnSouth = new JButton("South");
	// private final JButton btnEast = new JButton("East");
	// private final JButton btnWest = new JButton("West");
	// private final JButton btnCenter = new JButton("Center");

	// Menu
	private final JMenuBar mb;
	private final JMenu mnuFile;
	private final JMenuItem mnuItemQuit;
	private final JMenuItem mnuItemLoadFile;
	private final JMenuItem mnuItemSetSource;
	private final JMenu mnuHelp;
	private final JMenuItem mnuItemAbout;
	private DiscreteTimePlotter plotter;
	private JCheckBox chinButton;
	private JCheckBox glassesButton;
	private JCheckBox hairButton;
	private JCheckBox teethButton;
	private ItemListener chinButtonListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			System.err.println("chinButtonListener " + e);
		}
	};
	private ItemListener glassesButtonListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			System.err.println("glassesButtonListener " + e);
		}
	};
	private ItemListener hairButtonListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			System.err.println("hairButtonListener " + e);
		}
	};
	private ItemListener teethButtonListener = new ItemListener() {

		@Override
		public void itemStateChanged(ItemEvent e) {
			System.err.println("teethButtonListener " + e);
		}
	};

	/** Constructor for the GUI */
	public BasicGui() {
		// create graphics
		// Initialize all swing objects.
		f = new JFrame("Breath monitor"); // create Frame
		pnlNorth = new JPanel(); // North quadrant
		pnlSouth = new JPanel(); // South quadrant

		pnlEast = new JPanel(); // East quadrant
		pnlWest = new JPanel(); // West quadrant
		pnlCenter = new JPanel(); // Center quadrant

		// Menu
		mb = new JMenuBar();
		mnuFile = new JMenu("File");
		mnuItemQuit = new JMenuItem("Quit");
		mnuItemLoadFile = new JMenuItem("Load File");
		mnuItemSetSource = new JMenuItem("Set Source");
		mnuHelp = new JMenu("Help");
		mnuItemAbout = new JMenuItem("About");

		// Set menubar
		f.setJMenuBar(mb);

		// Set icon
		f.setIconImage(new ImageIcon("/home/federico/tesi/stethoscope.jpg")
				.getImage());

		// Build Menus
		mnuFile.add(mnuItemQuit); // Create Quit line
		mnuFile.add(mnuItemLoadFile); // Create Load file line
		mnuFile.add(mnuItemSetSource); // Create Set source line
		// mnuHelp.add(mnuItemAbout); // Create About line

		mnuHelp.add(new ShowAboutDialogAction());

		mb.add(mnuFile); // Add Menu items to form
		mb.add(mnuHelp);

		// Add Buttons
		// pnlNorth.add(btnNorth);
		// pnlSouth.add(btnSouth);
		// pnlEast.add(btnEast);
		// pnlWest.add(btnWest);
		// pnlCenter.add(btnCenter);

		// Setup Main Frame
		f.getContentPane().setLayout(new BorderLayout());
		f.getContentPane().add(pnlNorth, BorderLayout.NORTH);
		f.getContentPane().add(pnlSouth, BorderLayout.SOUTH);
		f.getContentPane().add(pnlEast, BorderLayout.EAST);
		f.getContentPane().add(pnlWest, BorderLayout.WEST);
		f.getContentPane().add(pnlCenter, BorderLayout.CENTER);

		// Allows the Swing App to be closed
		f.addWindowListener(new ListenCloseWdw());

		// Add Menu listener
		mnuItemQuit.addActionListener(new ListenMenuQuit());
		mnuItemLoadFile.addActionListener(new ListenMenuLoadFile());

		this.plotter = new DiscreteTimePlotter("", f);

		final Dimension screenSize = Toolkit.getDefaultToolkit()
				.getScreenSize();
		// final int width = (int) (screenSize.getWidth() * 0.8);
		// final int height = (int) (screenSize.getHeight() * 0.8);
		f.setBounds(100, 100, 800, 600);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// In initialization code:
		chinButton = new JCheckBox("Chin");
		chinButton.setMnemonic(KeyEvent.VK_C);
		chinButton.setSelected(true);

		glassesButton = new JCheckBox("Glasses");
		glassesButton.setMnemonic(KeyEvent.VK_G);
		glassesButton.setSelected(true);

		hairButton = new JCheckBox("Hair");
		hairButton.setMnemonic(KeyEvent.VK_H);
		hairButton.setSelected(true);

		teethButton = new JCheckBox("Teeth");
		teethButton.setMnemonic(KeyEvent.VK_T);
		teethButton.setSelected(true);

		// Register a listener for the check boxes.
		chinButton.addItemListener(chinButtonListener);
		glassesButton.addItemListener(glassesButtonListener);
		hairButton.addItemListener(hairButtonListener);
		teethButton.addItemListener(teethButtonListener);

	}

}