Changes Made to Model:
	Added method to get Coords of all non-empty cells for use in TextualView render(). This is needed to know
		which cells actually have data to avoid checking all possible cells.
	Overrode toString methods in Cells for use in views. Our original model required a call to a visitor to 
		get the String representation of a Cell or Value, but now the toString() method in all Cell classes
		calls this visitor automatically.
	Added a HashMap which collects errors, to allow the GUI to display errors.

Added a ReadOnlyWorksheet for use the the View. The view must have access to the model to get information about
	the worksheet, and this version prevents mutation of the Worksheet, since that functionality has not been
	added. Planning to remove this functionality after implementing a controller.

WorksheetView:
	A WorksheetView only requires classes that implement it to implement render(). This is because a view only needs
	to render itself, and anything else should be done in setting up the view in it's own constructor. Any other
	operations should be done to the Controller or Model.

TextualView Design:
	The design of the TextualView was entirely based on the assignment description. The View simply reads in all
	cells, evaluates them using a ReadOnlyModel, and write the result for each Cell in the format given in the 
	assignment. There were not many design choices to make, given the format of the result was given in the assignment.

Gui Design:
	For the main GUI we used a JFrame. This was essential to get set up the view for the use of other Swing components.
		Inside of this JFrame we have two Panels, a MenuPanel and a SpreadSheetPanel. These panels were created to 
		unify the similar parts of the assignment and allow for seperation of visually unrelated elements. While the
		two panel system made organizing the view easier, it did created complications when the SpreadSheetPanel and 
		MenuPanel needed to communicate, which they now do by going through the GUIView. We felt this design was logical
		and it allowed for us to keep our two panel system.
	SpreadSheetPanel Design:
		The SpreadSheetPanel is the view for all the contents of a Worksheet, and is the actual spreadsheet of the view.
		We needed to solve the following problems when implementing this view.
			1. How to represent cells
				Cells in our view are represented as Graphics2D rectangles. We wanted to avoid using JTable, as 
				Prof. Lerner said this design was difficult to figure out and he personally did not do it. We
				also avoided using a JComponent for each Cell, as a TA said this would cause our view to lag during
				each redraw. Therefore, using Graphics2D rectangles made the most sense.
			2. How to create row and column headers
				Creating row and column headers was difficult, because they need to dynamically changed with scrolling
				and physically fit into the view. We thought about using seperate panels for this, but we felt this
				was overly complicated and unnecessary, since the headers are essentially uneditable cells with preset text,
				and in every other way are identical to normal cells. Because of the similarity, we simply use the first row
				and column of cells as headers, and have special cases for them when drawing the sheet. This solution was
				very simple given our implementation.
			3. How to draw text and prevent text overflow
				Drawing text was done using the Graphics2D drawString method. This was an easy choice to make, because this decision
				was made with the same reasoning as used when determining how to draw cells. The difficulty came from preventing String
				overflow. We could not format the String, because there was no way to know how long the String would be or how big the
				cell would be. Clipping was the solution. We draw the String set the clip to exactly double the width of the cell, 
				and then clip to the size of the cell. This will always correctly clip the String without potentially clipping anything
				after the cell.
			4. How to select a cell
				Selecting a is done using a FocusPanel, which is a Panel that lays ontop of the SpreadSheetPanel, except for the first row
				and column. This FocusPanel has a single rectangle, which has thicker lines than a typical cell. Scrolling moves this box, 
				and does not alter the rest of the SpreadSheetPanel unless you are scrolling offscreen. This was done to avoid constant re-rendering
				and needing to have a way to draw only one cell as focused, and no other cells. This will also work when we need to select a cell
				for editing, as we have already thought about and planned for this case.
			5. How to scroll
				Scrolling is done exclusively with the arrow keys and the NavBox in the MenuPanel. This was done because the scrollbars in typical spreadsheet
				programs are useless and difficult to implement. Scrolling in either way changes the Focus, represented as the FocusX and FocusY components,
				and changes the relativePos if the screen needs to scroll. The Focus is whatever cell is selected. The relativePos is whatever cell
				is in the top-left corner of the SpreadSheet. Based on these two variables we know where we are in the sheet, and what cell is selected,
				and can adjust accordingly.
	MenuPanel Design:
		The MenuPanel is the view for the components in the menu. Currently, this is the NavBox and EquationBar. The NavBox allows for a Coord to be entered, and the
		SpreadSheetPanel will jump to that Coord in the top-left. This is one of the two methods of scrolling, with this method being designed as the way to jump 
		to cells far away. The EquationBar displays the contents of the Cell that is focused. The EquationBar will also allow for the focused cell to be 
		edited once we have a Controller. The SpringLayout was chosen to allow for specific formatting of these textboxes and their labels in relation to each other.