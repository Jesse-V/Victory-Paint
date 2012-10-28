
package victorypaint;

import java.awt.*;

/**
 * An HelpScreen is a semitransparent undecorated window which hovers over the main
 * program's window and helps identify and explain various control elements to the user.
 * @author Jesse Victors, A01485921
 */
public class HelpScreen extends PopupScreen
{
	/**
	 * Default font to use for displaying all text on this HelpScreen
	 */
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 20);
	
	
	/**
	 * Sets up this HelpScreen
	 */
	public HelpScreen(Point location, Dimension size)
	{
		super("Help");
		setSize(size);
		setLocation(location);
		makeWindowSpecial();
		setVisible(true);
	}
	
	
	
	/**
	 * Paints this HelpScreen, drawing all its text
	 * @param g the Graphics object to draw on, must be non-null
	 */
	@Override
	public void paint(Graphics g)
	{
		g.setColor(new Color(135, 206, 250)); //"Light Sky Blue" from http://en.wikipedia.org/wiki/Shades_of_blue
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.BLACK);
		g.setFont(DEFAULT_FONT);
		
		g.drawString("First select a drawing option", 15, 170);
		g.drawString("Finally, these menus can then modify what you've drawn", 230, 70);
		g.drawString("For additional information and tips on how to use them, hover your mouse over each item", 170, 100);
		g.drawString("Second, draw something in this canvas area", 418, 330);
		g.drawString("Click to exit this help screen", 100, 500);
	}
	
	
	
	/**
	 * Returns the desired transparency level for this AboutScreen
	 * @return 50% transparent
	 */
	@Override
	public float getDesiredOpacityLevel()
	{
		return 0.5f;
	}
}
