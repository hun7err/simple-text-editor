package com.simpletexteditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;

public class WindowActionHandler implements ActionListener {
	private Window win;
	public WindowActionHandler(Window w) {
		win = w;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if("FindNext".equals(command) || "FindPrev".equals(command)) {
			boolean forward = "FindNext".equals(command);
			
			SearchContext context = new SearchContext();
			String text = win.getSearchField().getText();
			if(text.length() > 0) {
				context.setSearchFor(text);
				context.setMatchCase(win.getMatchCaseCB().isSelected());
				context.setRegularExpression(win.getRegexCB().isSelected());
				context.setSearchForward(forward);
				context.setWholeWord(false);
				
				boolean found = SearchEngine.find(AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getEditor(), context);
				// do replace jest metoda replace() w SearchEngine
				if(!found) {
					JOptionPane.showMessageDialog(win.getFrame(), "Text not found");
				}
			}
		}
		
		if("Replace".equals(command) || "ReplaceAll".equals(command)) {
			boolean replaceAll = "ReplaceAll".equals(command);
			
			SearchContext context = new SearchContext();
			String 	text = win.getSearchField().getText(),
					replacement = win.getReplacementField().getText();
			if(text.length() > 0 && replacement.length() > 0) {
				context.setSearchFor(text);
				context.setReplaceWith(replacement);
				context.setRegularExpression(win.getRegexCB().isSelected());
				context.setWholeWord(false);
				
				if(replaceAll) {
					int replaced = SearchEngine.replaceAll(AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getEditor(), context);
					if(replaced <= 0) {
						JOptionPane.showMessageDialog(win.getFrame(), "Nothing to replace");
					}
				} else {
					boolean replaced = SearchEngine.replace(AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getEditor(), context);
					if(!replaced)
						JOptionPane.showMessageDialog(win.getFrame(), "Nothing to replace");
				} 
			}
		}
		
		if("TabClose".equals(command)) {
			// usuń tab
			int pos = win.getTabs().getSelectedIndex();
			AppManager.getDocuments().remove(pos);
			win.getTabs().remove(pos);
		}
		
		if(e.getSource().equals(win.getMenu().fileOpenItem)) {
			final JFileChooser fc = new JFileChooser();
			int ret = fc.showOpenDialog(win.getFrame());
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
				//d.getEditor().setLocation(0, 0);
				
				//d.getEditor().
				//AppManager.addDocument(d);
				win.getTabs().addTab(d.getFilename(), t);
				int index = win.getTabs().getTabCount()-1;
				JPanel pnlTab = new JPanel(new GridBagLayout()), iconPanel = new JPanel(new GridLayout(1, 1, 4, 4));
				//iconPanel.setPreferredSize(new Dimension(12, 12));
				pnlTab.setOpaque(false);
				JLabel lblTitle = new JLabel(d.getFilename());
				ImageIcon icon = new ImageIcon("res/close.png");
				JButton btnClose = new JButton(new ImageIcon(icon.getImage().getScaledInstance(12, 12, 4)));
				btnClose.setActionCommand("TabClose");
				btnClose.setOpaque(false);
				btnClose.setBorderPainted(false);
				btnClose.setContentAreaFilled(false);
				btnClose.setFocusPainted(false);
				btnClose.setPreferredSize(new Dimension(11, 15));

				GridBagConstraints gbc = new GridBagConstraints();
			    win.changeEditorTheme(win.getCurrentEditorSkin());
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.weightx = 1;
				gbc.ipadx = 5;

				pnlTab.add(lblTitle, gbc);

				gbc.gridx++;
				gbc.weightx = 0;
				gbc.ipadx = 5;
				iconPanel.add(btnClose);
				pnlTab.add(btnClose, gbc);

				win.getTabs().setTabComponentAt(index, pnlTab);

				btnClose.addActionListener(this);
				try {
					AppManager.addDocument(d);
				} catch (Exception exc) {
					System.out.println("Nie spodziewałem się Hiszpańskiej Inkwizycji!");
				}
				win.getFrame().repaint();
			}
		} else if (e.getSource().equals(win.getMenu().fileNewItem)) {
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
			ImageIcon icon = new ImageIcon("res/close.png");
			win.getTabs().addTab(d.getFilename(), t);
			int index = win.getTabs().getTabCount()-1;
			JPanel pnlTab = new JPanel(new GridBagLayout()), iconPanel = new JPanel(new GridLayout(1, 1, 4, 4));
			//iconPanel.setPreferredSize(new Dimension(12, 12));
		    win.changeEditorTheme(win.getCurrentEditorSkin());
			pnlTab.setOpaque(false);
			JLabel lblTitle = new JLabel(d.getFilename());
			JButton btnClose = new JButton(new ImageIcon(icon.getImage().getScaledInstance(12, 12, 4)));
			btnClose.setActionCommand("TabClose");
			btnClose.setOpaque(false);
			btnClose.setBorderPainted(false);
			btnClose.setContentAreaFilled(false);
			btnClose.setFocusPainted(false);
			//btnClose.setSize(10, 10);
			btnClose.setPreferredSize(new Dimension(11, 15));

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;
			gbc.ipadx = 5;

			pnlTab.add(lblTitle, gbc);

			gbc.gridx++;
			gbc.weightx = 0;
			gbc.ipadx = 5;
			iconPanel.add(btnClose);
			pnlTab.add(btnClose, gbc);

			win.getTabs().setTabComponentAt(index, pnlTab);

			btnClose.addActionListener(this);
		} else if (e.getSource().equals(win.getMenu().fileReloadItem)) {
			String val = "";
			try {
				val = AppManager.readFile(AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getFile());
			} catch (IOException exc) {
				exc.printStackTrace();
			}
			AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).setText(val);
			// przeładuj plik
		} else if (e.getSource().equals(win.getMenu().fileSaveItem)) {
			String content = AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getEditor().getText();
			
			if(AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getFile() == null) {
				final JFileChooser fc = new JFileChooser();
				int ret = fc.showSaveDialog(win.getFrame());
				if(ret == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).setFile(file);
					win.getTabs().setTitleAt(win.getTabs().getSelectedIndex(), file.getName());
					
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
				File f = AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getFile();
				System.out.println(f.getAbsolutePath());
				
				FileWriter fw = new FileWriter(f);
				fw.write(content);
				fw.close();
			} catch (IOException io) {
				io.printStackTrace();
			}
			}
		} else if (e.getSource().equals(win.getMenu().fileSaveAsItem)) {
			String content = AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getEditor().getText();
			
			final JFileChooser fc = new JFileChooser();
			int ret = fc.showSaveDialog(win.getFrame());
			if(ret == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).setFile(file);
				win.getTabs().setTitleAt(win.getTabs().getSelectedIndex(), file.getName());
				
				try {
					FileWriter fw = new FileWriter(file);
					fw.write(content);
					fw.close();
				} catch (IOException io) {
					io.printStackTrace();
				}
			}
		} else if (e.getSource().equals(win.getMenu().fileCloseItem)) {
			AppManager.getDocuments().remove(win.getTabs().getSelectedIndex());
			win.getTabs().remove(win.getTabs().getSelectedIndex());
		} else if(e.getSource().equals(win.getMenu().documentSearchItem)) {
			win.setSearchFrame(new JFrame("Search"));
			JPanel content = new JPanel(new BorderLayout());
			JPanel inner1 = new JPanel(new FlowLayout()), inner2 = new JPanel(new FlowLayout());
			win.getSearchFrame().setSize(220, 140);
			win.getSearchFrame().setAlwaysOnTop(true);
			win.setSearchField(new JTextField(20));
			content.add(win.getSearchField(), BorderLayout.PAGE_START);
			final JButton nextButton = new JButton("next >>");
			nextButton.setActionCommand("FindNext");
			nextButton.addActionListener(this);
			JButton prevButton = new JButton("<< prev");
			win.getSearchField().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					nextButton.doClick(0);
				}
			});
			prevButton.setActionCommand("FindPrev");
			prevButton.addActionListener(this);
			inner1.add(prevButton);
			inner1.add(nextButton);
			win.setRegexCB(new JCheckBox("Regex"));
			inner2.add(win.getRegexCB());
			win.setMatchCaseCB(new JCheckBox("Match case"));
			inner2.add(win.getMatchCaseCB());
			content.add(inner1, BorderLayout.CENTER);
			content.add(inner2, BorderLayout.PAGE_END);
			win.getSearchFrame().add(content);
			win.getSearchFrame().setVisible(true);
///////////////////////////////////////////////////////////////////////////////////
			// to-do: podpiąć zamianę + zrobić okno jak tu wyżej ^
		} else if(e.getSource().equals(win.getMenu().documentSearchAndReplaceItem)) {
			win.setSearchAndReplaceFrame(new JFrame("Search"));
			JPanel content = new JPanel(new BorderLayout());
			JPanel inner1 = new JPanel(new FlowLayout()), inner2 = new JPanel(new FlowLayout());
			win.getSearchAndReplaceFrame().setSize(220, 170);
			win.getSearchAndReplaceFrame().setAlwaysOnTop(true);
			win.setSearchField(new JTextField(20));
			win.setReplacementField(new JTextField(20));
			//content.add(win.getSearchField(), BorderLayout.PAGE_START);
			JPanel top = new JPanel(new BorderLayout());
			top.add(win.getSearchField(), BorderLayout.PAGE_START);
			top.add(win.getReplacementField(), BorderLayout.PAGE_END);
			content.add(top, BorderLayout.PAGE_START);
			final JButton replaceButton = new JButton("Replace");
			replaceButton.setActionCommand("Replace");
			replaceButton.addActionListener(this);
			JButton replaceAllButton = new JButton("Replace All");
			win.getSearchField().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					replaceButton.doClick(0);
				}
			});
			replaceAllButton.setActionCommand("ReplaceAll");
			replaceAllButton.addActionListener(this);
			inner1.add(replaceButton);
			inner1.add(replaceAllButton);
			win.setRegexCB(new JCheckBox("Regex"));
			inner2.add(win.getRegexCB());
			win.setMatchCaseCB(new JCheckBox("Match case"));
			inner2.add(win.getMatchCaseCB());
			content.add(inner1, BorderLayout.CENTER);
			content.add(inner2, BorderLayout.PAGE_END);
			win.getSearchAndReplaceFrame().add(content);
			win.getSearchAndReplaceFrame().setVisible(true);
/////////////////////////////////////////////////////////////////////////////////////
		} else if(e.getSource().equals(win.getMenu().editUndoItem)) {
			//AppManager.getDocuments()[win.getTabs().getSelectedIndex()-1]
			System.out.println(AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getFilename());
			AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getEditor().undoLastAction();
		} else if (e.getSource().equals(win.getMenu().editRedoItem)) {
			AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getEditor().redoLastAction();
		} else if (e.getSource().equals(win.getMenu().editChangeThemeItem)) {
			win.setChangeThemeFrame(new JFrame("Choose a theme"));
			win.getChangeThemeFrame().setSize(180, 300);
			String themes[] = {"Autumn", "BusinessBlackSteel", "Dust", "Gemini", "Graphite", "Mariner"};
			JList<String> themeList = new JList<String>(themes);
			themeList.addListSelectionListener(new ListActionHandler(win));
			themeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			themeList.setSelectedIndex(3);
			JPanel p = new JPanel(new BorderLayout());
			p.add(themeList);
			win.getChangeThemeFrame().add(p);
			win.getChangeThemeFrame().pack();
			win.getChangeThemeFrame().setVisible(true);
			/*"Graphite","Dust","Mariner","Gemini","Autumn",BusinessBlackSteel*/
		} else if(e.getSource().equals(win.getMenu().helpAboutItem)) {			/*win.setAboutFrame(new JFrame("About"));
			win.getAboutFrame().setSize(500, 500);
			win.getAboutFrame().setVisible(true);
			JPanel panel = new JPanel(new BorderLayout());
			JLabel top = new JLabel("Simple Text Editor version " + AppManager.getVersion());
			String text = "Simple Text Editor (copyright &copy; Michał Bartecki, Krzysztof 'hun7er' Marciniak)\n" +
						  ",abbreviated STE, is a text edittor written ina Java for OOP classes at\n" +
						  "Poznań University of Technology.";
			JLabel center = new JLabel(text);
			panel.add(top, BorderLayout.PAGE_START);
			panel.add(center, BorderLayout.CENTER);
			win.getAboutFrame().add(panel);*/
		}
		//System.out.println("wat");
        //...Get information from the action event...
        //...Display it in the text area...
    }

}
