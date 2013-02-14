/**
 * 
 */
package com.simpletexteditor;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
//import java.awt.Font;
//import java.awt.Color;
//import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
//import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
//import java.awt.image.*;
import java.io.IOException;
//import javax.imageio.ImageIO;
import javax.swing.*;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

/**
 * @author hun7er
 *
 */
public class Window implements ActionListener, ItemListener {
	private JFrame frame = null,
				   searchFrame = null,
				   searchAndReplaceFrame = null;
	private String curSkin = "Dust"/*"Graphite","Dust","Mariner","Gemini","Autumn"*/;
	private int width, height;
	private static final String version = "0.0999 (work in progress)";
	private JPanel panel;
	private JTabbedPane tabs;
	private Menu menu;
	private JTextField searchField;
	private JCheckBox regexCB;
	private JCheckBox matchCaseCB;
	
	public void setSkin(String name) throws Exception {
		curSkin = name;
		//JFrame.setDefaultLookAndFeelDecorated(true);
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
		//java.awt.Image icon = null;
		//System.out.println("working directory: "+GUI.class.getProtectionDomain().getCodeSource().getLocation().getFile());
		/*try {
			//System.out.println(System.getProperty("user.dir"));
			java.io.InputStream is = getClass().getResourceAsStream("res/icon.png");
			//if(is == null) {
				//System.out.println("Image reading failed");
			//} else 
			//	icon = ImageIO.read(is);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		//if (icon != null) frame.setIconImage(icon);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("res/icon.png"));
		//frame.setBackground(Color.GREEN);
		panel = new JPanel(new java.awt.BorderLayout()); // works, yay \o/
		//panel.setBackground(Color.RED);
		tabs = new JTabbedPane();
		
		menu = new Menu(this);
		
		panel.add(menu.getMenuBar(), BorderLayout.PAGE_START);
		panel.add(tabs, BorderLayout.CENTER);
		frame.add(panel);
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
	
	public void actionPerformed(ActionEvent e) {
		if(searchFrame != null) {
			String command = e.getActionCommand();
			boolean forward = "FindNext".equals(command);
			
			SearchContext context = new SearchContext();
			String text = searchField.getText();
			if(text.length() > 0) {
				context.setSearchFor(text);
				context.setMatchCase(matchCaseCB.isSelected());
				context.setRegularExpression(regexCB.isSelected());
				context.setSearchForward(forward);
				context.setWholeWord(false);
				
				boolean found = SearchEngine.find(AppManager.getDocuments().get(tabs.getSelectedIndex()).getEditor(), context);
				// do replace jest metoda replace() w SearchEngine
				if(!found) {
					JOptionPane.showMessageDialog(frame, "Text not found");
				}
			}
		}
		if(e.getSource().equals(menu.fileOpenItem)) {
			final JFileChooser fc = new JFileChooser();
			int ret = fc.showOpenDialog(frame);
			if(ret == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				String val = "";
				try {
					val = AppManager.readFile(file);
				} catch (IOException exc) {
					exc.printStackTrace();
				}
				Document d = new Document(file);
				d.setEditor(new RSyntaxTextArea());
			    d.getEditor().setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
			    d.getEditor().setCodeFoldingEnabled(true);
			    d.getEditor().setAntiAliasingEnabled(true);
				//RTextScrollPane sp = new RTextScrollPane(d.getEditor());
				d.setScroll(new RTextScrollPane(d.getEditor()));
				d.getScroll().setFoldIndicatorEnabled(true);
				JPanel t = d.getTab();
				t.add(d.getScroll(), BorderLayout.CENTER);
				t.doLayout();
				d.setType(SyntaxConstants.SYNTAX_STYLE_JAVA);
				d.setText(val);
				//AppManager.addDocument(d);
				tabs.add(d.getFilename(), t);
				try {
					AppManager.addDocument(d);
				} catch (Exception exc) {
					System.out.println("Nie spodziewałem się Hiszpańskiej Inkwizycji!");
				}
				frame.repaint();
			}
		} else if (e.getSource().equals(menu.fileNewItem)) {
			Document d = new Document("New");
			d.setEditor(new RSyntaxTextArea());
		    d.getEditor().setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		    d.getEditor().setCodeFoldingEnabled(true);
		    d.getEditor().setAntiAliasingEnabled(true);
			//RTextScrollPane sp = new RTextScrollPane(d.getEditor());
			d.setScroll(new RTextScrollPane(d.getEditor()));
			d.getScroll().setFoldIndicatorEnabled(true);
			JPanel t = d.getTab();
			t.add(d.getScroll(), BorderLayout.CENTER);
			t.doLayout();
			d.setType(SyntaxConstants.SYNTAX_STYLE_JAVA);
			AppManager.addDocument(d);
			tabs.add(d.getFilename(), t);
		} else if (e.getSource().equals(menu.fileReloadItem)) {
			String val = "";
			try {
				val = AppManager.readFile(AppManager.getDocuments().get(tabs.getSelectedIndex()).getFile());
			} catch (IOException exc) {
				exc.printStackTrace();
			}
			AppManager.getDocuments().get(tabs.getSelectedIndex()).setText(val);
			// przeładuj plik
		} else if (e.getSource().equals(menu.fileSaveItem)) {
			String content = AppManager.getDocuments().get(tabs.getSelectedIndex()).getEditor().getText();
			
			if(AppManager.getDocuments().get(tabs.getSelectedIndex()).getFile() == null) {
				final JFileChooser fc = new JFileChooser();
				int ret = fc.showSaveDialog(frame);
				if(ret == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					AppManager.getDocuments().get(tabs.getSelectedIndex()).setFile(file);
					tabs.setTitleAt(tabs.getSelectedIndex(), file.getName());
					
					try {
						FileWriter fw = new FileWriter(file);
						fw.write(content);
						fw.close();
					} catch (IOException io) {
						io.printStackTrace();
					}
				}
			} else {
			try {
				File f = AppManager.getDocuments().get(tabs.getSelectedIndex()).getFile();
				System.out.println(f.getAbsolutePath());
				
				FileWriter fw = new FileWriter(f);
				fw.write(content);
				fw.close();
			} catch (IOException io) {
				io.printStackTrace();
			}
			}
		} else if (e.getSource().equals(menu.fileSaveAsItem)) {
			String content = AppManager.getDocuments().get(tabs.getSelectedIndex()).getEditor().getText();
			
			final JFileChooser fc = new JFileChooser();
			int ret = fc.showSaveDialog(frame);
			if(ret == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				AppManager.getDocuments().get(tabs.getSelectedIndex()).setFile(file);
				tabs.setTitleAt(tabs.getSelectedIndex(), file.getName());
				
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(content);
					fw.close();
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
		} else if (e.getSource().equals(menu.fileCloseItem)) {
			AppManager.getDocuments().remove(tabs.getSelectedIndex());
			tabs.remove(tabs.getSelectedIndex());
		} else if(e.getSource().equals(menu.documentSearchItem)) {
			searchFrame = new JFrame("Search");
			JPanel content = new JPanel(new BorderLayout());
			JPanel inner1 = new JPanel(new FlowLayout()), inner2 = new JPanel(new FlowLayout());
			searchFrame.setSize(220, 140);
			searchFrame.setAlwaysOnTop(true);
			searchField = new JTextField(20);
			content.add(searchField, BorderLayout.PAGE_START);
			final JButton nextButton = new JButton("next >>");
			nextButton.setActionCommand("FindNext");
			nextButton.addActionListener(this);
			JButton prevButton = new JButton("<< prev");
			searchField.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					nextButton.doClick(0);
				}
			});
			prevButton.setActionCommand("FindPrev");
			prevButton.addActionListener(this);
			inner1.add(prevButton);
			inner1.add(nextButton);
			regexCB = new JCheckBox("Regex");
			inner2.add(regexCB);
			matchCaseCB = new JCheckBox("Match case");
			inner2.add(matchCaseCB);
			content.add(inner1, BorderLayout.CENTER);
			content.add(inner2, BorderLayout.PAGE_END);
			searchFrame.add(content);
			searchFrame.setVisible(true);
///////////////////////////////////////////////////////////////////////////////////
			// to-do: podpiąć zamianę + zrobić okno jak tu wyżej ^
		} else if(e.getSource().equals(menu.documentSearchAndReplaceItem)) {
			searchAndReplaceFrame = new JFrame("SearchAndReplace");
			searchAndReplaceFrame.setSize(320, 240);
			searchAndReplaceFrame.setVisible(true);
/////////////////////////////////////////////////////////////////////////////////////
		} else if(e.getSource().equals(menu.editUndoItem)) {
			//AppManager.getDocuments()[tabs.getSelectedIndex()-1]
			System.out.println(AppManager.getDocuments().get(tabs.getSelectedIndex()).getFilename());
			AppManager.getDocuments().get(tabs.getSelectedIndex()).getEditor().undoLastAction();
		} else if (e.getSource().equals(menu.editRedoItem)) {
			AppManager.getDocuments().get(tabs.getSelectedIndex()).getEditor().redoLastAction();
		}
        //...Get information from the action event...
        //...Display it in the text area...
    }

    public void itemStateChanged(ItemEvent e) {
        //...Get information from the item event...
        //...Display it in the text area...
    }
}
