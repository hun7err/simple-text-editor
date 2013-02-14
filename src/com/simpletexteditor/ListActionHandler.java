package com.simpletexteditor;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JFrame;
import javax.swing.JList;

public class ListActionHandler implements ListSelectionListener {
	private Window win;
	
	public ListActionHandler(Window w) {
		win = w;
	}
	
	public void valueChanged(ListSelectionEvent e) {
	    if (e.getValueIsAdjusting() == false) {
	    	@SuppressWarnings("rawtypes")
			String theme = (String)((JList)e.getSource()).getSelectedValue();
			System.out.println(theme);
			JFrame.setDefaultLookAndFeelDecorated(true);
			try {
				win.setSkin(theme);
				if(theme == "Graphite") {
					win.changeEditorTheme("dark");
					win.setCurEditorSkin("dark");
					//AppManager.getDocuments().get(win.getTabs().getSelectedIndex()).getEditor();
				} else {
					win.changeEditorTheme("default");
					win.setCurEditorSkin("default");
				}
				win.getFrame().repaint();
				win.getChangeThemeFrame().repaint();
			} catch (Exception ex) {
			}
	    }
	}
}
