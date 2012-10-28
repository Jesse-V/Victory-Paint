README for Victory Paint v2.1 by Jesse Victors, A01485921

This is a drawing program. The user has the ability to draw line segments, open curves, and enter text. The user can color, translate, clone, modify, rotate, or delete nearly any drawn object. If a mistake is made, there is an Undo feature. I've included on-screen help, and every control has informative tool tip text, but here's an overview as you wanted:

===IN SHORT===
Select a drawing function from among the options on the left hand side of the screen. Each behaves similar to MS-Paint, and each has tool tip text to explain how to use it.
	Line segment: Press mouse on first location, drag second location around, release mouse
	Curve: Press mouse on starting location, drag mouse around, release to finish.
	Text: Click on desired location, type, and then click again to finish

If you make a mistake, click the Undo function under the "Edit" menu.


===KEYBOARD SHORTCUTS===
	Control-N: Makes a new clear canvas. Deletes current canvas.
	Control-O: Opens a saved canvas. Only one file is available. (filename hardcoded in)
	Control-S: Saves the current canvas to a single file, overriding old save.
	Control-Q: Quits/Exits Victory Paint
	Control-Z: Undo last action
	Control-H: Launches on-screen help
	Control-A: Displays the About screen


===MODIFICATION FUNCTIONS===
All of these functions show mouse over text, and they strive to be intuitive.
	Color: Press mouse near a drawing to color it to a random color.
	Translate: Press mouse near a drawing and drag it around.
	Clone: Press mouse near a drawing and drag its copy around.
	Modify: Press mouse near a point of a drawing and drag it around.
	Rotate: Press mouse near a point of a drawing and move mouse in a circular fashion around that point to rotate the entire drawing around that point.
	Delete: Click mouse near an object to delete it.

As always, the Undo button is available if you make a mistake!


===SOME MAIN GUI COMPONENTS===
JMenuBar, JMenu, JMenuItem, JToggleButton, JSlider, HorizontalStrut


===NOTES===
	This program contains some advanced features. It plays a startup sound, and the sound file itself is actually inside the .jar file. The About and Help screens are semitransparent and have rounded window corners. Note that it no longer displays as of v2.1, but there was an image of Clippit that popped up on the screen.

I make great use of polymorphism in my code, so it should be very organized, and is also heavily commented. Code style (including spacing) strives to follow the C++ official industry guidelines because they are very handy and make the code clean.
	