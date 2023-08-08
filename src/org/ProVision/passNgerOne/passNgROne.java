package org.ProVision.passNgerOne;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class passNgROne {
	/**
	 * Create the GUI and show it. For thread safety,
	 * this method should be invoked from the
	 * event-dispatching thread. 
	 */
	
	private static void createAndShowGUI(){
		//Create and set up the window.
		JFrame frame = new JFrame("PassNgR 1");		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		//Add Application icon
		URL url = passNgROne.class.getResource("/org/ProVision/passNgerOne/image/icon.png");
		if (url != null){
			Toolkit kit = Toolkit.getDefaultToolkit();
			Image img = kit.createImage(url);
			frame.setIconImage(img);
		}
		//Create and set up the content pane
		WindowComponent mainWin = new WindowComponent();
		frame.setJMenuBar(mainWin.CreatePassBar());		
		frame.setContentPane(mainWin.createInfoPane());
		frame.pack();
		
		//Display the window
		//frame.setMinimumSize(new Dimension(1000,700));
		frame.setPreferredSize(new  Dimension(1000,700));
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static void main(String[] args){
		//Schedule a job for the event-dispatching thread:
		//creating and showing this application's GUI
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				try {
					UIManager.put("swing.boldMetal", Boolean.FALSE);
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (ClassNotFoundException | InstantiationException
						| IllegalAccessException
						| UnsupportedLookAndFeelException e) {
					// TODO Auto-generated `catch block
					e.printStackTrace();
				}
				createAndShowGUI();
			}
		});
	}

}
