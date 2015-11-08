package com.theopsfloor.billboard;

import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class MainFrame extends JFrame {
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  public MainFrame() {
    JMenuBar bar = new JMenuBar();
    bar.add(createMenu("File",
        createMenuItem("New", null),
        createMenuItem("Open", null)));
    //    menu.add
    //    bar.add(createMenu("Load...", new ActionListener() {
    //      @Override
    //      public void actionPerformed(ActionEvent e) {
    //
    //      }
    //  }));

    this.setJMenuBar(bar);
  }

  private static JMenu createMenu(String text, JMenuItem...items) {
    JMenu menu =  new JMenu(text);

    for(JMenuItem it : items) {
      menu.add(it);
    }

    return menu;
  }

  private static JMenuItem createMenuItem(String text, ActionListener listen) {
    JMenuItem out = new JMenuItem(text);
    if (listen != null) {
      out.addActionListener(listen);
    };
    return out;
  }

}
