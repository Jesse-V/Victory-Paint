
package victorypaint;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import victorypaint.DrawingCreators.DrawableCreator;
import victorypaint.DrawingCreators.LineSegmentCreator;
import victorypaint.DrawingCreators.PencilCreator;
import victorypaint.DrawingCreators.TextBoxCreator;
import victorypaint.Modifiers.*;

/*Changelog
 * 2.1:  Correct sizing of AboutScreen/HelpScreen, used ArrayList instead of files for Undo memory,
 *		 OnscreenAssistant no longer appears, 
 * 1.72: Added everyone's favorite assistant, renamed "Curve" to "Pencil"
 * 1.68: Added startup sound
 * 1.63: Added some Keyboard accelerator shortcuts, completed Javadoc
 * 1.61: Added help menu with About and Help screens
 * 1.53: Added rotation function and background color controls, organized code that sets up each drop-down menu
 * 1.41: Added Undo button, (no practical limit on the number of undo actions)
 * 1.4:	 Improved code organization/documentation, seperation of projectOnto and getClosestPt functions,
 *		 added ability to copy, delete, and edit Drawables
 *		 DrawingPanel, Main, MouseInputSelector, Curve, Drawable, LineSegment, TextBox,
 *		 PencilCreator, DrawableCreator, LineSegmentCreator, TextBoxCreator, DrawableCopier,
 *		 DrawableDeleter, DrawableEditor, DrawableModifier, DrawableTranslator
 * 1.3:  Added File Open/Save/Close options
 * 1.2:	 Improved polymorphism of DrawableCreator, improved menu, selectable/translatable Drawables
 *		 DrawingPanel, Main, MouseInputSelector, Curve, Drawable, LineSegment, TextBox,
 *		 PencilCreator, DrawableCreator, LineSegmentCreator, TextBoxCreator, DrawableModifier, DrawableTranslator
 * 1.05: Clarified a variable in DrawableCreator, added Javadoc everywhere
 * 1.0:  First version. Top menu consists of disabled buttons
 *		 DrawingPanel, Main, Curve, Drawable, LineSegment, TextBox,
 *		 PencilCreator, DrawableCreator, LineSegmentCreator, TextBoxCreator
 */

/**
 * The Main class for handling the program.
 * Handles GUI setups, general organization, and top-level commands.
 * @author Jesse Victors, A01485921, April 2012
*/
public class Main extends JFrame implements Runnable, ActionListener, ChangeListener
{	
	/**
	 * A list of all drawing functions. These are buttons corresponding all things the user can draw.
	 */
	private ArrayList<JToggleButton> drawFunctions = new ArrayList<>(8);
	
	/**
	 * The panel the Drawables will be drawn on.
	 */
	private DrawingPanel drawPanel = new DrawingPanel();
	
	/**
	 * The font to use for the entire menu systems.
	 */
	private Font menuFont = new Font("Dialog", Font.BOLD, 14);
	
	/**
	 * Reference to the current DrawableCreator, if one has been selected from the drawFunctions
	 */
	private DrawableCreator creator;
	
	/**
	 * Button group for all the draw functions. This way only one can be selected at a time.
	 */
	private ButtonGroup bg = new ButtonGroup();
	
	/**
	 * Color of the side panel. Use to camouflage the other components with it for better appearance.
	 */
	private final Color SIDE_PANEL_COLOR = Color.LIGHT_GRAY;
	
	
	/**
	 * Keeps track of each drawing state for the Undo function
	 */
	private ArrayList<ArrayList> undoList = new ArrayList<>(16);


	/**
	 * Sets up GUI, initializes variables, and launches the repaint thread
	 * @param args
	 */
	public static void main(String[] args)
	{
		Main main = new Main();
		new Thread(main).start();
	}



	/**
	 * Constructors for Main. Creates GUI.
	 */
	public Main()
	{
		super("Victory Paint v2.1");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(Toolkit.getDefaultToolkit().getScreenSize());
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		//new OnscreenAssistant(); //has a timer
		playStartupSound();
		setupGUI(getContentPane());
		setVisible(true);
	}
	
	
	
	/**
	 * Plays the startup sound. It looks for "startupSound.wav" either in "resources" folder of
	 * the project directory or in the "resources" folder of the .jar.
	 */
	public final void playStartupSound()
	{	//http://stackoverflow.com/questions/26305/how-can-i-play-sound-in-java
		//http://stackoverflow.com/questions/5529754/java-io-ioexception-mark-reset-not-supported
		
		try
		{
			Clip clip = AudioSystem.getClip();
			
			AudioInputStream inputStream;
			InputStream is = getClass().getResourceAsStream("/resources/startupSound.wav");
			if (is == null) //test if found inside .jar
				inputStream = AudioSystem.getAudioInputStream(new File("resources/startupSound.wav"));
			else
				inputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
			
			clip.open(inputStream);
			clip.start(); 
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}



	/**
	 * Initializes the drawing options which appear on the left-hand side of the screen.
	 * Configures all the buttons and adds them to drawFunctions and puts them all in the button group.
	 */
	public final void initDrawingOptions()
	{
		JToggleButton b = new JToggleButton("Line");
		b.setName("Line Segment Button");
		b.setToolTipText("Draw a line segment. To create: press mouse, drag around, release");
		drawFunctions.add(b);
		bg.add(b);
		
		b = new JToggleButton("Pencil");
		b.setName("Pencil Button");
		b.setToolTipText("Freehand an open curve. Combine with Line for closed curve. To create: press mouse, drag around, release");
		drawFunctions.add(b);
		bg.add(b);
		
		b = new JToggleButton("Text");
		b.setName("Text Button");
		b.setToolTipText("Insert a text box. To create: click desired location, type, click to finish");
		drawFunctions.add(b);
		bg.add(b);

		for (JToggleButton button : drawFunctions)
		{
			button.setBackground(SIDE_PANEL_COLOR);
			button.setFocusable(false);
			button.setFont(menuFont);
			button.addActionListener(this);
		}
	}



	/**
	 * Sets up the menu options which appear on the top of the screen,
	 * and all the drop-down functions.
	 */
	private void setupMenuOptions(JPanel panel)
	{
		JMenuBar menuBar = new JMenuBar();
		panel.add(Box.createHorizontalStrut(77));
		panel.add(menuBar);

		JMenu fileMenu = new JMenu("File");
		fileMenu.setToolTipText("General file operations");
		fileMenu.setFont(menuFont);
		
		JMenu editMenu = new JMenu("Edit");
		editMenu.setToolTipText("Modify something you've already drawn");
		editMenu.setFont(menuFont);
		
		JMenu envMenu = new JMenu("Environment");
		envMenu.setToolTipText("Modify the general drawing environment");
		envMenu.setFont(menuFont);
		
		JMenu helpMenu = new JMenu("Help");
		helpMenu.setToolTipText("Confused? This might help!");
		helpMenu.setFont(menuFont);
		
		menuBar.add(fileMenu);
		menuBar.add(Box.createHorizontalStrut(10));
		menuBar.add(editMenu);
		menuBar.add(Box.createHorizontalStrut(10));
		menuBar.add(envMenu);
		menuBar.add(Box.createHorizontalStrut(10));
		menuBar.add(helpMenu);
		
		Component glue = Box.createHorizontalGlue();
		menuBar.setBackground(SIDE_PANEL_COLOR);
		glue.setBackground(null); //will inherit background from its menuBar parent
		menuBar.add(glue);

		for (int j = 0; j < menuBar.getComponentCount(); j++)
		{
			JComponent menu = (JComponent)menuBar.getComponent(j);
			menu.setOpaque(true);
			menu.setBackground(SIDE_PANEL_COLOR);
		}

		//set up file menu:
		String[][] fileMenuItemInfo = {
				{"New",		"File New",			"Blanks the canvas for a new drawing",	"N"},
				{"Open",	"File Open",		"Load the canvas from a single file", 	"O"},
				{"Save",	"File Save",		"Save the canvas to a single file", 	"S"},
				{"Close",	"File Close",		"Shuts down Victory Paint",				"Q"}};
		setupMenu(fileMenuItemInfo, fileMenu);

		//set up edit menu:
		String[][] editMenuItemInfo = {
				{"Undo",		"Edit Undo",		"Undo the last action",				"Z"},
				{"Color",		"Edit Color",		"Press mouse near a drawing to color it to random color."},
				{"Translate",	"Edit Translate",	"Moves an object. Press mouse near a drawing and drag it around"},
				{"Delete",		"Edit Delete",		"Delete something that you drew. Click near it to delete."},
				{"Clone",		"Edit Clone",		"Similar in functionality to Translate, but drags a copy into place."},
				{"Modify",		"Edit Modify",		"Similar in functionality to Translate, but translates a single point."},
				{"Rotate",		"Edit Rotate",		"Press mouse near a drawing and drag in a circular fashion to rotate."}};
		setupMenu(editMenuItemInfo, editMenu);
		
		//set up environment menu:
		String[][] sliderInfo = {
				{"Red Slider",		"Controls the red component of the color"},
				{"Green Slider",	"Controls the green component of the color"},
				{"Blue Slider",		"Controls the blue component of the color"}};
		
		JMenu backgroundColorMenu = new JMenu("Background color");
		backgroundColorMenu.setToolTipText("Adjust the canvas' background color using RGB sliders");
		backgroundColorMenu.setFont(menuFont);
		envMenu.add(backgroundColorMenu);
		setupSliders(sliderInfo, backgroundColorMenu);
		
		//set up help menu:
		String[][] helpMenuItemInfo = {
				{"I'm so confused",		"Help Confused",	"Lost? This shows how to operate this program", "H"},
				{"About "+getTitle(),	"Help About",		"Who made this program anyway?",				"A"}};
		setupMenu(helpMenuItemInfo, helpMenu);
	}
	
	
	
	/**
	 * Sets up a drop-down menu given a 2D array of each item's text, name, tool tip text,
	 * and possible accelerator, as well as the JMenu to add the item to.
	 */
	private void setupMenu(String[][] menuItemInfo, JMenu menu)
	{
		for (String[] itemInfo : menuItemInfo)
		{
			JMenuItem item = new JMenuItem(itemInfo[0]);
			item.setName(itemInfo[1]);
			item.setToolTipText(itemInfo[2]);
			item.setFont(menuFont);
			item.addActionListener(this);
			if (itemInfo.length == 4)
				item.setAccelerator(KeyStroke.getKeyStroke((int)itemInfo[3].charAt(0), ActionEvent.CTRL_MASK));
			
			menu.add(item);
		}
	}

	
	
	/**
	 * Sets up the JSliders which control the background colors and add them to the given menu
	 * @param sliderInfo contains the name and tooltip text for each JSlider
	 * @param menu the menu to add the JSliders to
	 */
	private void setupSliders(String[][] sliderInfo, JMenu menu)
	{
		for (String[] info : sliderInfo)
		{
			JSlider slider = new JSlider(0, 255, 255);
			slider.setName(info[0]);
			slider.setToolTipText(info[1]);
			slider.addChangeListener(this);
			
			slider.setMajorTickSpacing(255);
			slider.setMinorTickSpacing(16);
			slider.setPaintTicks(true);
			slider.setPaintLabels(true);
			
			menu.add(slider);
		}
	}


	
	/**
	 * Sets up the GUI and adds it to the specified Container.
	 * @param contentPane the Container to add the GUI to, must be non-null
	 */
	private void setupGUI(Container contentPane)
	{
		initDrawingOptions();
		
		JPanel drawOptionsPanel = new JPanel();
		drawOptionsPanel.addMouseListener(new MouseAdapter()
		{
			/*for some reason a DrawableCreator does not recognize that its exited the
			drawingPanel when the mouse moves into the drawOptionsPanel.
			This is a workaround*/
			@Override
			public void mouseEntered(MouseEvent e)
			{
				if (creator != null && creator.isCreating())
					creator.mouseExited(e);
			}
		});
		
		drawOptionsPanel.setLayout(new GridBagLayout());
		drawOptionsPanel.setBackground(SIDE_PANEL_COLOR);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;

		for (int j = 0; j < drawFunctions.size(); j++)
		{
			c.gridy = j;
			drawOptionsPanel.add(drawFunctions.get(j), c);
		}

		//fill the lower part of the drawOptionsPanel with a blank element to shove all options to the top
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 1;
		c.gridy = 50;
		drawOptionsPanel.add(new JLabel(), c);

		JPanel topMenu = new JPanel();
		topMenu.setLayout(new BoxLayout(topMenu, BoxLayout.X_AXIS));
		topMenu.setBackground(SIDE_PANEL_COLOR);
		setupMenuOptions(topMenu);

		drawPanel.setBackground(Color.WHITE);
		drawPanel.setPreferredSize(getSize());
		
		contentPane.add(topMenu, BorderLayout.NORTH);
		contentPane.add(drawOptionsPanel, BorderLayout.WEST);
		contentPane.add(drawPanel, BorderLayout.EAST);
	}



	/**
	 * This method runs in a separate thread. 50 times per second, it takes care of two things:
	 * 1) Repainting the GUI, which includes the drawing panel,
	 * 2) If a DrawableCreator is no longer accepting user input, un-select all drawing options
	 */
	@Override
	public void run()
	{
		while (true)
		{
			try
			{	Thread.sleep(20);	}
			catch (Exception e)
			{ }

			if (creator != null && !creator.acceptingUserInput())
			{
			//	bg.clearSelection();
				creator = null;
			}

			repaint();
		}
	}



	/**
	 * Called when a user clicks on one of the drawing options.
	 * It then sets up the appropriate DrawableCreator to make the desired object.
	 * @param e the ActionEvent corresponding to the button press
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		Component src = (Component)e.getSource();
		
		//handle undo
		if (src.getName().equals("Edit Undo"))
		{
			if (undoList.size() > 0)
			{
				drawPanel.setDrawableList(undoList.remove(undoList.size() - 1));
				return;
			}
		}
		else
			undoList.add(drawPanel.getDrawableList());
		
		
		if (src instanceof JToggleButton)
		{ 	//if a drawing function
			
			switch (src.getName())
			{
				case "Line Segment Button":
					creator = new LineSegmentCreator(drawPanel);
					break;
					
				case "Pencil Button":
					creator = new PencilCreator(drawPanel);
					break;
					
				case "Text Button":
					creator = new TextBoxCreator(drawPanel);
					break;
			}
			drawPanel.add(creator.getDrawable());
		}
		else
		{ //its a general menu function
			
			try
			{
				switch (src.getName())
				{
					case "File New":
						drawPanel.drawables = new ArrayList<>(16);
						break;
						
					case "File Open":
						ObjectInputStream ois = new ObjectInputStream(new FileInputStream("VPsave.dat"));
						drawPanel.drawables = (ArrayList)ois.readObject();
						ois.close();
						break;
						
					case "File Save":
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("VPsave.dat"));
						oos.writeObject(drawPanel.drawables);
						oos.close();
						break;
						
					case "File Close":
						System.exit(0);
						break;
					
					case "Edit Color":
						drawPanel.setDrawableModifier(new DrawableColorChanger(drawPanel, new Color(new Random().nextInt())));
						break;
							
					case "Edit Translate":
						drawPanel.setDrawableModifier(new DrawableTranslator(drawPanel));
						break;
						
					case "Edit Delete":
						drawPanel.setDrawableModifier(new DrawableDeleter(drawPanel, drawPanel.drawables));
						break;
						
					case "Edit Clone":
						drawPanel.setDrawableModifier(new DrawableCopier(drawPanel, drawPanel.drawables));
						break;
						
					case "Edit Modify":
						drawPanel.setDrawableModifier(new DrawableEditor(drawPanel));
						break;
						
					case "Edit Rotate":
						drawPanel.setDrawableModifier(new DrawableRotator(drawPanel));
						break;
						
					case "Help Confused":
						new HelpScreen(getLocation(), getSize());
						break;
					
					case "Help About":
						new AboutScreen(getLocation(), getSize());
						break;
				}
			}
			catch (IOException | ClassNotFoundException ex)
			{
				System.out.println(ex);
			}
		}
	}
	
	
	
	/**
	 * Called when the user changes one of the JSliders.
	 * This affects DrawingPanel's background color.
	 * @param e the ChangeEvent corresponding to the JSlider change
	 */
	@Override
	public void stateChanged(ChangeEvent e)
	{
		JSlider src = (JSlider)e.getSource();
		Color currColor = drawPanel.getBackground();
		
		Color newColor = currColor;
		switch (src.getName())
		{
			case "Red Slider":
				newColor = new Color(src.getValue(), currColor.getGreen(), currColor.getBlue());
				break;
			
			case "Green Slider":
				newColor = new Color(currColor.getRed(), src.getValue(), currColor.getBlue());
				break;
			
			case "Blue Slider":
				newColor = new Color(currColor.getRed(), currColor.getGreen(), src.getValue());
				break;
		}
		
		drawPanel.setBackground(newColor);
	}
}
