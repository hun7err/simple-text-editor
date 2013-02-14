/**
 * 
 */
package com.simpletexteditor;

import java.awt.BorderLayout;
/*import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;*/
//import java.awt.Font;
//import java.awt.Color;
//import java.awt.Component;
import java.awt.Toolkit;
import java.io.IOException;
//import java.awt.event.KeyEvent;
//import java.awt.image.*;
//import javax.imageio.ImageIO;
import javax.swing.*;

import org.fife.ui.rsyntaxtextarea.Theme;

/**
 * @author hun7er
 *
 */

///////////////////////////
/*

to-do:
	- jak się uda to menu kontekstowe do zakładek
	- replace i replaceAll (jedno okno)
	- Makefile (z generowaniem .jar do bin/ włącznie), pamiętać o classpath
	- ew. zmiana stylu
	
	Jest godzina 2:00, muszę wstać na 6:00 i spakować walizkę (czyli pewnie koło 5:00).
	Damn you, Java...

*/
//////////////////////////

public class Window {
	private JFrame frame = null,
				   searchFrame = null,
				   searchAndReplaceFrame = null,
				   changeThemeFrame = null,
				   aboutFrame = null;
	private String curSkin = "Gemini"/*"Graphite","Dust","Mariner","Gemini","Autumn"*/,
				   curEditorSkin = "default";
	private int width, height;
	private JPanel panel;
	private JTabbedPane tabs;
	private Menu menu;
	private JTextField searchField, replacementField;
	private JCheckBox regexCB;
	private JCheckBox matchCaseCB;
	
	public JFrame getSearchFrame() {
		return searchFrame;
	}
	public JFrame getSearchAndReplaceFrame() {
		return searchAndReplaceFrame;
	}
	public JFrame getAboutFrame() {
		return aboutFrame;
	}
	public JFrame getChangeThemeFrame() {
		return changeThemeFrame;
	}
	public JPanel getPanel() {
		return panel;
	}
	public JTextField getReplacementField() {
		return replacementField;
	}
	public Menu getMenu() {
		return menu;
	}
	public JCheckBox getRegexCB() {
		return regexCB;
	}
	public JCheckBox getMatchCaseCB() {
		return matchCaseCB;
	}
	public JTabbedPane getTabs() {
		return tabs;
	}
	public JTextField getSearchField() {
		return searchField;
	}
	public JFrame getFrame() {
		return frame;
	}
	
	public void setAboutFrame(JFrame f) {
		aboutFrame = f;
	}
	public void setSearchField(JTextField f) {
		searchField = f;
	}
	public void setReplacementField(JTextField f) {
		replacementField = f;
	}
	public void setMatchCaseCB(JCheckBox c) {
		matchCaseCB = c;
	}
	public void setRegexCB(JCheckBox c) {
		regexCB = c;
	}
	public void setFrame(JFrame f) {
		frame = f;
	}
	public void setSearchFrame(JFrame f) {
		searchFrame = f;
	}
	public void setSearchAndReplaceFrame(JFrame f) {
		searchAndReplaceFrame = f;
	}
	public void setChangeThemeFrame(JFrame f) {
		changeThemeFrame = f;
	}
	
	public void setSkin(String name) throws Exception {
		curSkin = name;
		//JFrame.setDefaultLookAndFeelDecorated(true);
		try {
			UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.Substance"+curSkin+"LookAndFeel");
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public void setCurEditorSkin(String name) {
		curEditorSkin = name;
	}
	
	public String getCurrentSkin() {
		return curSkin;
	}
	public String getCurrentEditorSkin() {
		return curEditorSkin;
	}
	
	private void createControls() {
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//if (icon != null) frame.setIconImage(icon);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("res/icon.png"));
		//frame.setBackground(Color.GREEN);
		panel = new JPanel(new java.awt.BorderLayout()); // works, yay \o/
		//panel.setBackground(Color.RED);
		tabs = new JTabbedPane();
		
		menu = new Menu(new WindowActionHandler(this));
		
		panel.add(menu.getMenuBar(), BorderLayout.PAGE_START);
		panel.add(tabs, BorderLayout.CENTER);
		frame.add(panel);
		frame.setVisible(true);
	}
	
	public void changeEditorTheme(String name) {
		setCurEditorSkin(name);
		for(int i = 0; i < AppManager.getDocuments().size(); i++) {
			 try {
		         Theme theme = Theme.load(getClass().getResourceAsStream("res/" + name + ".xml"));
		         theme.apply(AppManager.getDocuments().get(i).getEditor());
		      } catch (IOException ioe) {
		         ioe.printStackTrace();
		      }
		}
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
		frame = new JFrame("Simple Text Editor");
		createControls();
	}
	
	
}
