package edu.cs3500.spreadsheets.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A class that represents the menu panel and its properties.
 */
public class MenuPanel extends JPanel {
  private final ViewPanel parent;
  private final JTextField equationBar;
  private final JTextField navBox;

  /**
   * A constructor that makes a MenuPanel with the correct properties.
   */
  MenuPanel(SpringLayout layout, ViewPanel parent) {
    super(layout);
    this.setPreferredSize(new Dimension(1000, 200));
    this.setBackground(Color.gray);
    this.parent = parent;
    JLabel navBoxLabel = new JLabel("Navagation Box");
    this.add(navBoxLabel);
    this.navBox = new JTextField(10);
    this.navBox.addKeyListener(new MenuPanelKeyListener());
    this.navBox.setFocusable(true);
    this.add(navBox);
    JLabel equationBarLabel = new JLabel("Equation Bar");
    this.add(equationBarLabel);
    this.equationBar = new JTextField(100);
    this.equationBar.setFocusable(true);
    this.add(equationBar);
    this.layoutComponents(layout, navBoxLabel, equationBarLabel);
  }

  /**
   * Structures the JTextBoxes and JLabels visually. Abstracted out of constructor for simplicity
   * and readability - not to be called outside of constructor.
   * @param layout The SpringLayout used in the constructor.
   * @param navBoxLabel The label for the navBox.
   * @param equationBarLabel The label for the equationBar.
   */
  private void layoutComponents(SpringLayout layout, JLabel navBoxLabel, JLabel equationBarLabel) {
    layout.putConstraint(SpringLayout.WEST, navBox, 100, SpringLayout.WEST, navBoxLabel);
    layout.putConstraint(SpringLayout.NORTH, navBox, 0, SpringLayout.NORTH, navBoxLabel);
    layout.putConstraint(SpringLayout.NORTH, navBoxLabel, 80, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, navBoxLabel, 10, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.NORTH, equationBarLabel, 110, SpringLayout.NORTH,
            this);
    layout.putConstraint(SpringLayout.WEST, equationBarLabel, 10, SpringLayout.WEST,
            this);
    layout.putConstraint(SpringLayout.WEST, equationBar, 100, SpringLayout.WEST,
            equationBarLabel);
    layout.putConstraint(SpringLayout.NORTH, equationBar, 0, SpringLayout.NORTH,
            equationBarLabel);
  }


  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Dimension bounds = parent.getSize();
    this.setBounds(0, 0, bounds.width, Math.round(bounds.height * 0.2f));
    this.setBackground(Color.gray);
  }


  /**
   * Changes the text of the navBox to be the given String. Used by the SpreadSheetPanel to update
   * the navBar when the focus is changed with the arrow keys.
   * @param s The text for the navBox.
   */
  void updateNavBox(String s) {
    this.navBox.setText(s);
  }

  /**
   * Changes the text of the equationBar to be the given String. Used by the SpreadSheetPanel to
   * update the equationBar when the focus is changed with the arrow keys.
   * @param s The text for the equationBar.
   */
  void updateEquationBar(String s) {
    this.equationBar.setText(s);
  }

  String equationBarText() {
    return this.equationBar.getText();
  }

  /**
   * Internal key handler for a MenuPanel. Accepts the text given to a text box.
   */
  class MenuPanelKeyListener implements KeyListener {
    @Override
    public void keyTyped(KeyEvent e) {
      // A KeyListener must implement all of these methods to be created.
      // We don't want there to be any response to this type of keyEvent.
    }

    @Override
    public void keyPressed(KeyEvent e) {
      // A KeyListener must implement all of these methods to be created.
      // We don't want there to be any response to this type of keyEvent.
    }

    @Override
    public void keyReleased(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        String s = navBox.getText();
        String numeric = "0123456789";
        String rowStr = "1";
        String colString = "";
        for (int i = 0; i < s.length(); i++) {
          if (numeric.contains(s.substring(i, i + 1))) {
            rowStr = s.substring(i);
            colString = s.substring(0, i);
            break;
          }
        }
        int row = Integer.parseInt(rowStr);

        MenuPanel.this.parent.updateSpreadsheetScroll(new
                Coord(Coord.colNameToIndex(colString), row));
        MenuPanel.this.parent.focusX = 1;
        MenuPanel.this.parent.focusY = 1;
        MenuPanel.this.parent.updateMenu();
      }
    }
  }
}
