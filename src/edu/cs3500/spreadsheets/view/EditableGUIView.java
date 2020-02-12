package edu.cs3500.spreadsheets.view;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SpringLayout;

import edu.cs3500.spreadsheets.model.Coord;
import edu.cs3500.spreadsheets.model.WorksheetReadOnlyModel;

/**
 * A class that adds the functionality of editing to the existing {@link GUIView} class. All
 * functionality in a {@link GUIView} is supported here, along with added features. Users are able
 * to edit cells by entering data into the equation bar, and then clicking the accept button. Before
 * accepting, the equation bar can be reverted by using the reject button. Scrolling is done via the
 * arrow keys and with the navigation box, which accepts a {@link Coord} (ex. "B5"). Files can be
 * loaded and saved using the file menu, which also has an option to quit the program. Cells can
 * also be clicked to select them. However, all cell editing must be done in the equation bar.
 */
public class EditableGUIView extends JFrame implements GUIWorksheetView {
  private JButton acceptButton;
  private JButton rejectButton;
  private JMenuBar menuBar;
  private JMenuItem saveAs;
  private JMenuItem open;
  private JMenuItem exit;
  private ViewPanel view;


  /**
   * A public constructor that takes in the model and a title for the JFrame.
   */
  public EditableGUIView(String title, WorksheetReadOnlyModel m) {
    super(title);
    this.view = new ViewPanel(m, this);
    this.view.setVisible(true);
    this.add(view);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setSize(1000, 700);
    this.setLayout(new SpringLayout());

    menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");

    this.saveAs = new JMenuItem("Save As");
    this.saveAs.setActionCommand("Save As");
    fileMenu.add(this.saveAs);


    this.open = new JMenuItem("Open");
    fileMenu.add(this.open);

    this.exit = new JMenuItem("Exit");
    fileMenu.add(this.exit);

    menuBar.add(fileMenu);

    acceptButton = new JButton("accept");
    acceptButton.setVisible(true);
    acceptButton.setActionCommand("accept");

    rejectButton = new JButton("reject");
    rejectButton.setVisible(true);
    rejectButton.setActionCommand("reject");
    this.view.addEquationBarButtons(this.acceptButton, this.rejectButton);
    this.view.addMenuBar(menuBar);
    this.setIconImage(new ImageIcon("resources\\excelIcon.png").getImage());
  }


  @Override
  public void render() {
    this.setVisible(true);
    this.view.updateMenu();
  }

  @Override
  public void acceptKeyListener(KeyListener keyListener) {
    this.view.acceptKeyListener(keyListener);
  }

  @Override
  public void acceptActionListener(ActionListener actionListener) {
    this.acceptButton.addActionListener(actionListener);
    this.rejectButton.addActionListener(actionListener);
    this.open.addActionListener(actionListener);
    this.saveAs.addActionListener(actionListener);
    this.exit.addActionListener(actionListener);
  }

  @Override
  public Coord getSelectedCell() {
    return this.view.getSelectedCell();
  }

  @Override
  public String getEquationBarText() {
    return this.view.getEquationBarText();
  }

  @Override
  public void shiftFocus(int x, int y) {
    this.view.shiftFocus(x, y);
  }

  @Override
  public Coord getOffset() {
    return this.view.getOffset();
  }

  @Override
  public void savePopup() {
    this.view.savePopup();
  }

  @Override
  public void openPopup() {
    this.view.openPopup();
  }

  @Override
  public void acceptMouseListener(MouseListener mouseListener) {
    this.addMouseListener(mouseListener);
  }


  @Override
  public JMenuBar getJMenuBar() {
    return this.menuBar;
  }
}
