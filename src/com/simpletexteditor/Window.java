/**
 * 
 */
package com.simpletexteditor;

import java.awt.BorderLayout;
import java.awt.Font;
//import java.awt.Color;
//import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
//import java.awt.event.KeyEvent;
import java.io.File;
//import java.awt.image.*;
import java.io.IOException;
//import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.html.HTMLDocument;

import jsyntaxpane.DefaultSyntaxKit;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

/**
 * @author hun7er
 *
 */
public class Window implements ActionListener, ItemListener {
	private JFrame frame = null;
	private String curSkin = "Dust"/*"Graphite","Dust","Mariner","Gemini","Autumn"*/;
	private int width, height;
	private static final String version = "0.001 (alpha)";
	private JPanel panel;
	private JTabbedPane tabs;
	private Menu menu;
	// - kontener (Vector) na taby a tam JPanele a w nich JSyntaxPane
	// - powiązać Document z tabami (JPanelami)
	// - metoda do tworzenia menu (dość łatwego tworzenia)
	// - szukanie i zamiana:
	// - www.swingall.com/findreplace/index.html
	// - download: www.swingall.com/download/jfindreplace.zip
	// - przy zamykaniu pliku/zakładki dać jakieś okienko z zapytaniem czy zapisać plik
	// - focus na świeżo otwartym pliku
	// - wydzielić obsługę eventów do oddzielnej klasy EventHandler (wtedy implements wyleci do tej klasy wraz z metodą wyłapującą eventy)
	
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
		//frame.getContentPane().add(label);
		frame.setVisible(true);
//////////////////////////////////////////zmiana stylu///////////////////////////////////////////////
		//JFrame frame_style = new JFrame("Wybierz styl");
		//frame_style.setSize(200, 90);
		//frame_style.setVisible(true);
		//String styles[] = {"Dust", "Graphite", "Mariner", "BusinessBlackSteel", "Gemini", "Autumn"}; // "Dust"/*"Graphite","Dust","Mariner","Gemini","Autumn"
		//JComboBox box = new JComboBox(styles);
		//box.addActionListener(this);
		//frame_style.add(box);
		/*RSyntaxTextArea textArea = new RSyntaxTextArea();
		//textArea.setFont(new Font("Terminus", 12, 12));
	    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
	    textArea.setCodeFoldingEnabled(true);
	    textArea.setAntiAliasingEnabled(true);
	    textArea.setVisible(true);
	    textArea.setText("Test\nTest\nTest");
	    RTextScrollPane sp = new RTextScrollPane(textArea);
	    sp.setFoldIndicatorEnabled(true);
	    sp.setVisible(true);
	    JPanel tab = new JPanel(new BorderLayout());
	    tab.add(sp);
	    tabs.add("test", tab);*/
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
				JEditorPane edit = new JEditorPane();
				edit.setFont(new Font("Segoe Script", 0, 14));
				//String f = DefaultSyntaxKit.getProperty("font");
				//System.out.print(f);
				//Font font = new Font("Terminus", Font.PLAIN, 14);
			    //String bodyRule = "body { font-family: " + font.getFamily() + "; " + "font-size: " + font.getSize() + "pt; }";
			    //((HTMLDocument)edit.getDocument()).getStyleSheet().addRule(bodyRule);
				/*RSyntaxTextArea textArea = new RSyntaxTextArea();
				//textArea.setFont(new Font("Terminus", 12, 12));
			    textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
			    textArea.setCodeFoldingEnabled(true);
			    textArea.setAntiAliasingEnabled(true);
			    textArea.setVisible(true);
			    textArea.setText("Test\nTest\nTest");
			    RTextScrollPane sp = new RTextScrollPane(textArea);
			    sp.setFoldIndicatorEnabled(true);
			    sp.setVisible(true);
			    JPanel tab = new JPanel(new BorderLayout());
			    tab.add(sp);
			    tabs.add("test", tab);*/
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
				AppManager.addDocument(d);
				frame.repaint();
				//tabs.setSelectedIndex(AppManager.getDocuments().size()-1);
				/*
		//codeEditor = new JEditorPane();
		//JScrollPane scrPane = new JScrollPane(codeEditor);
		//tab.add(scrPane, BorderLayout.CENTER);
		//tab.doLayout();
		//codeEditor.setContentType("text/java");
		//codeEditor.setText("public static void main(String[] args) {\n}");
		//tabs.add("main.java", tab);*/
			}
		} else if (e.getSource().equals(menu.fileReloadItem)) {
			// przeładuj plik
		} else if (e.getSource().equals(menu.fileSaveItem)) {
			// zapisz
		} else if (e.getSource().equals(menu.fileSaveAsItem)) {
			//final JFileChooser fc = new JFileChooser();
			//int ret = fc.showSaveDialog(frame);
		} else if(e.getSource().getClass().equals(JComboBox.class)) {
			try {
				JComboBox temp = (JComboBox)e.getSource();
				setSkin((String)temp.getSelectedItem());
				frame.repaint();
			} catch (Exception exc) {
				exc.printStackTrace();
			}
		}
        //...Get information from the action event...
        //...Display it in the text area...
    }

    public void itemStateChanged(ItemEvent e) {
        //...Get information from the item event...
        //...Display it in the text area...
    }
}
