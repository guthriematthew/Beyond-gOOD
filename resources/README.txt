Changes:
	A new interface has been added: GUIWorksheetView. This interface extends the WorksheetView interface, and 
	the GUIView now implements the new interface. This was done because the TextualView and GUI Views are 
	completely different except for one method, so this separation felt logical.
	The GUIView no longer holds the SpreadSheetPanel and MenuPanel. Instead a ViewPanel holds these panels
	and has all the functionality the GUIView formerly had. This was to allow for more versitility and reuseability
	in the design of the EditableGUIView. These two views simply delegate to the ViewPanel for all of their 
	methods.
	Bug fixes in the model to catch more errors and display them to the user instead of throwing exceptions.
	GUIView has new methods that are required of it in it's interface, but that it does not use because it is 
	uneditable. These methods throw an IllegalStateException if called on a GUIView.

Controller:
	The WorksheetController interface extends the KeyListener, ActionListener, and MouseListener interfaces. It does this
	because every controller will need to use the methods in these interfaces to interact with the view, and 
	the view will accept each of these listeners. The Controller can also render the view, instead of the view
	rendering automatically.
	
	The GUIWorksheetController implements the WorkSheetController interface. It takes in a WorksheetModel, and 
	an EditableGUIView. It takes in only an EditableGUIView because a controller is not needed for any other 
	views, and the other views do not support adding the listeners the GUiWorksheetController would try to
	add to them. This controller implementation uses the listeners to add all the functionality in the assignment,
	and certain extra credit functionality. It does not handle moving with arrow keys, as that was already built
	into the SpreadSheetPanel in assignment 6. 

EditableGUIView:
	The EditableGUIView holds a ViewPanel and the additional Swing elements it adds to the ViewPanel, such
	as the accept and reject JButtons. The ViewPanel still holds all other view elements, so the EditableGUIView 
	does nothing except delegate to the ViewPanel, except for the fact the MouseListener is added to the 
	EditableGUIView, and not the ViewPanel.