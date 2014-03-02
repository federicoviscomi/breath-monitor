package userInterface;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class ShowAboutDialogAction extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4973722906722338603L;

	public ShowAboutDialogAction() {
		super("About");
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		// System.err.println("Action for first button/menu item" + e);
		/*
		 * JFrame about = new JFrame(); about.add(new TextArea("ABOUT..."));
		 * about.setBounds(300, 300, 300, 200); about.setVisible(true);
		 */
		final JFrame aboutFrame = new JFrame();
		final JLabel jlabel = new JLabel(new ImageIcon(
				"/home/federico/tesi/stethoscope.jpg"));
		jlabel.setBounds(100, 100, 100, 100);
		aboutFrame.setBounds(300, 300, 300, 200);
		aboutFrame.add(jlabel);

		aboutFrame.setVisible(true);
		// JOptionPane.showMessageDialog(aboutFrame,"Eggs are not supposed to be green.");
	}
}