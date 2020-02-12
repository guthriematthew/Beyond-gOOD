package edu.cs3500.spreadsheets.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.cs3500.spreadsheets.model.Coord;

/**
 * A popup menu that prompts users for information to make graphs out of.
 */
public class GraphPopup extends JDialog {
  private JTextField startingCoord1;
  private JTextField endingCoord1;
  private JTextField startingCoord2;
  private JTextField endingCoord2;
  private JTextField nameField;
  private JPanel popUpPanel;
  private GraphingEditableGUIView view;

  private final String NUMERICS = "0123456789";

  /**
   * A constructor for the graphing popup.
   */
  public GraphPopup(JFrame owner, GraphingEditableGUIView view) {
    super(owner, "Make Graph", true);
    this.view = view;
    this.setSize(200, 200);
    this.setResizable(false);
    popUpPanel = new JPanel();

    addComponentsPanel();
    JButton confirmButton = new JButton("Confirm");

    confirmButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
          try {
          Coord[] coords = validate(startingCoord1.getText(), endingCoord1.getText(),
                  startingCoord2.getText(), endingCoord2.getText());
          String s = nameField.getText();
          if (s.contains(" ")) {
            throw new IllegalArgumentException("Title cannot contain spaces");
          }
          GraphPopup.this.view.makeGraph(coords[0], coords[1], coords[2], s);
        } catch (IllegalArgumentException e1) {
          makeErrorBox();
        }
        dispose();
      }
    });

    popUpPanel.add(confirmButton);
    this.add(popUpPanel);
    this.setVisible(true);
  }

  // adds the label and text field to the JPanel in the correct place
  private void addComponentsPanel() {
    popUpPanel.add(new JLabel("Starting Coord 1"));
    startingCoord1 = new JTextField(5);
    popUpPanel.add(startingCoord1);
    popUpPanel.add(new JLabel("Ending Coord 1 "));
    endingCoord1 = new JTextField(5);
    popUpPanel.add(endingCoord1);
    popUpPanel.add(new JLabel("Starting Coord 2"));
    startingCoord2 = new JTextField(5);
    popUpPanel.add(startingCoord2);
    popUpPanel.add(new JLabel("Ending Coord 2 "));
    endingCoord2 = new JTextField(5);
    popUpPanel.add(endingCoord2);
    popUpPanel.add(new JLabel("      Title     "));
    nameField = new JTextField(10);
    popUpPanel.add(nameField);
  }

  // ensures that all text corresponds to valid coordinates
  private Coord[] validate(String s1, String e1, String s2, String e2) {
    Coord[] temp = new Coord[4];
    if (isSingleReference(s1) && isSingleReference(e1) && isSingleReference(s2)
            && isSingleReference(e2)) {
      temp[0] = coordFromString(s1);
      temp[1] = coordFromString(e1);
      temp[2] = coordFromString(s2);
      temp[3] = coordFromString(e2);

      int colLengthA = temp[1].row - temp[0].row;
      int colLengthB = temp[3].row - temp[2].row;

      if (temp[0].col == temp[1].col && colLengthA >= 0 && temp[2].col == temp[3].col
              && colLengthB >= 0 && colLengthA == colLengthB) {
        return temp;
      }
    }
    throw new IllegalArgumentException("Not all textboxes have single cell references");
  }

  // ensures that the given text is a coord reference
  private boolean isSingleReference(String s) {
    boolean b = s.length() > 1;
    boolean expectingNum = false;
    for (int i = 0; i < s.length(); i++) {
      boolean temp = this.NUMERICS.contains(s.substring(i, i + 1));

      if (temp && i > 0) {
        expectingNum = true;
      }

      String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
      b = b && ((!expectingNum && alphabet.contains(s.substring(i, i + 1)))
              || (expectingNum && temp));
    }
    return b;
  }

  // converts a coord to string
  private Coord coordFromString(String s) {
    String rowStr = "1";
    String colString = "";
    for (int i = 0; i < s.length(); i++) {
      if (NUMERICS.contains(s.substring(i, i + 1))) {
        rowStr = s.substring(i);
        colString = s.substring(0, i);
        break;
      }
    }
    int row = Integer.parseInt(rowStr);
    return new Coord(Coord.colNameToIndex(colString), row);
  }

  // Opens an error box if the graph cannot be made encounter an error.
  private void makeErrorBox() {
    JDialog errorPopup = new JDialog(this, true);
    errorPopup.setSize(250, 50);
    errorPopup.add(new JLabel("These are not valid coordinates/title"));
    errorPopup.setResizable(false);
    errorPopup.setVisible(true);
  }
}
