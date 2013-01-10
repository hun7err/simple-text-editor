/**
 * 
 */
package com.simpletexteditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.image.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * @author hun7er
 *
 */
public class Window {
	private JFrame frame = null;
	private String curSkin = "Graphite";
	private int width, height;
	private static final String version = "0.001 (alpha)";
	
	public void setSkin(String name) throws Exception {
		curSkin = name;
		try {
			UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.Substance"+curSkin+"LookAndFeel");
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public String getCurrentSkin() {
		return curSkin;
	}
	
	private void createControls() {
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		java.awt.Image icon = null;
		//System.out.println("working directory: "+GUI.class.getProtectionDomain().getCodeSource().getLocation().getFile());
		try {
			//System.out.println(System.getProperty("user.dir"));
			java.io.InputStream is = getClass().getResourceAsStream("res/icon.png");
			if(is == null) {
				System.out.println("Image reading failed");
			} else 
				icon = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//if (icon != null) frame.setIconImage(icon);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("res/icon.png"));
		//frame.setBackground(Color.GREEN);
		JPanel panel = new JPanel(new java.awt.BorderLayout()); // works, yay \o/
		//panel.setBackground(Color.RED);
		JTabbedPane tabs = new JTabbedPane();
		for(int i = 0; i < 10; i++) {
			tabs.addTab("Tab "+String.valueOf(i+1), null);
			//tabs.setMnemonicAt(i, mnemonic)
		}
		//JLabel label = new JLabel("Hello world!");
		//label.setBackground(Color.YELLOW);
		//panel.add(label);
		JMenuBar jbar = new JMenuBar();
		
		// to-do: tworzenie menu przez jakieś stosunkowo proste metody
		jbar.add(new JMenu("File"));
		jbar.add(new JMenu("Help"));
		
		panel.add(jbar, BorderLayout.PAGE_START);
		panel.add(tabs, BorderLayout.CENTER);
		frame.add(panel);
		//frame.getContentPane().add(label);
		frame.setVisible(true);
	}
	
	public Window(int w, int h) throws Exception {
		width = w;
		height = h;
		JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			setSkin(this.getCurrentSkin());
		} catch (Exception e) {
			throw new Exception(e);
		}
		frame = new JFrame("Simple Text Editor v"+version);
		createControls();
	}
}
