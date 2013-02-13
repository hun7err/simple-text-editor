package com.simpletexteditor;

import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu {
	public JMenuItem fileNewItem,
					 fileOpenItem,
					 fileReloadItem,
					 fileCloseItem,
					 fileSaveItem = null,
					 fileSaveAsItem = null,
					 editUndoItem = null,
					 editRedoItem = null,
					 documentSearchItem = null,
					 documentSearchAndReplaceItem = null,
					 helpAboutItem = null;
	private JMenu fileMenu, editMenu, documentMenu, helpMenu;
	private JMenuBar jbar;
	
	public JMenuBar getMenuBar() {
		return jbar;
	}
	
	private void createMenu(ActionListener l) {
		jbar = new JMenuBar();
		//menuItem = new JMenuItem("Open", KeyEvent.VK_O);
		fileMenu = new JMenu("File");
		helpMenu = new JMenu("Help");
		editMenu = new JMenu("Edit");
		documentMenu = new JMenu("Document");
		helpMenu = new JMenu("Help");
		
		fileNewItem = new JMenuItem("New");
		fileNewItem.addActionListener(l);
		fileOpenItem = new JMenuItem("Open");
		fileOpenItem.addActionListener(l);
		fileReloadItem = new JMenuItem("Reload");
		fileReloadItem.addActionListener(l);
		fileCloseItem = new JMenuItem("Close");
		fileCloseItem.addActionListener(l);
		fileSaveItem = new JMenuItem("Save");
		fileSaveItem.addActionListener(l);
		fileSaveAsItem = new JMenuItem("Save as...");
		fileSaveAsItem.addActionListener(l);
		editUndoItem = new JMenuItem("Undo");
		editUndoItem.addActionListener(l);
		editRedoItem = new JMenuItem("Redo");
		editRedoItem.addActionListener(l);
		helpAboutItem = new JMenuItem("About");
		helpAboutItem.addActionListener(l);
		documentSearchItem = new JMenuItem("Search");
		documentSearchItem.addActionListener(l);
		documentSearchAndReplaceItem = new JMenuItem("Search and replace");
		documentSearchAndReplaceItem.addActionListener(l);
		
		fileMenu.add(fileNewItem);
		fileMenu.add(fileOpenItem);
		fileMenu.add(fileReloadItem);
		fileMenu.add(fileCloseItem);
		fileMenu.add(fileSaveItem);
		fileMenu.add(fileSaveAsItem);
		
		editMenu.add(editUndoItem);
		editMenu.add(editRedoItem);
		
		documentMenu.add(documentSearchItem);
		documentMenu.add(documentSearchAndReplaceItem);
		
		helpMenu.add(helpAboutItem);
		
		jbar.add(fileMenu);
		jbar.add(editMenu);
		jbar.add(documentMenu);
		jbar.add(helpMenu);
		//jbar.add(fileMenu);
		//jbar.add(helpMenu);
	}
	
	public Menu(ActionListener l) {
		createMenu(l);
	}
}
