package com.simpletexteditor;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
//import javax.swing.JEditorPane;
import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import jsyntaxpane.DefaultSyntaxKit;

//import jsyntaxpane.DefaultSyntaxKit;
//import jsyntaxpane.util.Configuration;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

public class Document {
	private String	before, // after undo
					after, // after redo
					filename; //
	private File file;
	private JPanel tab;
	private RSyntaxTextArea editor;
	//private JEditorPane editor;
	//private JScrollPane linenumbers;
	private RTextScrollPane linenumbers;
	
	public void setEditor(RSyntaxTextArea e) {
		/*Configuration config = DefaultSyntaxKit.getConfig();
		config.put("PopupMenu", "cut-to-clipboard,copy-to-clipboard,paste-from-clipboard,-,select-all,-,undo,redo,-,find,find-next,goto-line,jump-to-pair,-,complete-word,-,toggle-comments");
		DefaultSyntaxKit.setConfig(config); */
		// to-do: customowe popup-menu + popup-menu dla tabów
		editor = e;
		editor.setFont(new Font("Monospace", 12, 12));
	}
	public void setScroll(RTextScrollPane s) {
		linenumbers = s;
	}
	public void setTab(JPanel t) {
		tab = t;
	}
	public void setText(String s) {
		editor.setText(s);
	}
	public void setType(String s) {
		//editor.setContentType(s);
		editor.setSyntaxEditingStyle(s);
	}
	public void setBefore(String s) {
		before = s;
	}
	public void setAfter(String s) {
		after = s;
	}
	
	public String getBefore() {
		return before;
	}
	public String getAfter() {
		return after;
	}

	public RSyntaxTextArea getEditor() {
		return editor;
	}
	public RTextScrollPane getScroll() {
		return linenumbers;
	}
	public JPanel getTab() {
		return tab;
	}
	public String getFilename() {
		return filename;
	}
	
	public Document(String name) {
		tab = new JPanel();
		tab.setLayout(new BorderLayout());
		filename = name;
		file = new File(filename);
		try {
			before = AppManager.readFile(file);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	public Document(File f) {
		tab = new JPanel();
		tab.setLayout(new BorderLayout());
		file = f;
		filename = f.getName();
		try {
			before = AppManager.readFile(f);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
