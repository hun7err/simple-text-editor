/**
 * 
 */
package com.simpletexteditor;

import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * @author hun7er
 *
 */
public class AppManager {
	private static Window win = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					win = new Window(800, 600);
				} catch(Exception e) {
					e.printStackTrace();
					System.out.println("Failed to create the main window; quitting...");
					System.exit(1);
				}
				//initUI();
			}
		});
	}

}
