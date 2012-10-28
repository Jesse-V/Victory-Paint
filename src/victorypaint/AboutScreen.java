
package victorypaint;

import java.awt.*;
import javax.swing.SwingUtilities;

/**
 * An AboutScreen is a semitransparent undecorated window which hovers over the main
 * program's window and displays various information about it such as the author and release date.
 * @author Jesse Victors, A01485921
 */
public class AboutScreen extends PopupScreen
{
	/**
	 * Specifies the margin in pixels between this AboutScreen and the horizontal edges of the screen
	 */
	private static final int HEIGHT_MARGIN = 100;
	
	/**
	 * Specifies the margin in pixels between this AboutScreen and the vertical edges of the screen
	 */
	private static final int WIDTH_MARGIN = HEIGHT_MARGIN * 2;
	
	/**
	 * Default font to use for displaying all text on this AboutScreen
	 */
	private static final Font DEFAULT_FONT = new Font("Arial", Font.PLAIN, 24);
	
	
	/**
	 * Sets up this AboutScreen, and makes it semitransparent, undecorated,
	 * and gives it rounded corners
	 */
	public AboutScreen(Point location, Dimension size)
	{
		super("About");
		
		setSize(size.width - WIDTH_MARGIN * 2, size.height - HEIGHT_MARGIN * 2);
		setLocation(location.x + WIDTH_MARGIN, location.y + HEIGHT_MARGIN);
		
		makeWindowSpecial();
		
		setVisible(true);
	}
	
	
	
	/**
	 * Paints this AboutScreen, drawing all its text
	 * @param g the Graphics object to draw on, must be non-null
	 */
	@Override
	public void paint(Graphics g)
	{
		g.setColor(new Color(135, 206, 250)); //"Light Sky Blue" from http://en.wikipedia.org/wiki/Shades_of_blue
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.BLACK);
		g.setFont(DEFAULT_FONT);
		
		String[] text = {"\"Victory Paint\"",
						"by Jesse Victors",
						"",
						"April 2012",
						"", "", "", "", "", "", "", "",
						"Click to exit About screen"
		};
		
		FontMetrics fm = g.getFontMetrics();
		int dH = fm.getHeight();
		int y = (getHeight() - text.length*dH) / 2 + fm.getAscent(); //center vertically
		
		for (String line : text)
		{
			int strWidth = SwingUtilities.computeStringWidth(fm, line);
			int x = (getWidth() - strWidth) / 2; //center horizontally

			g.drawString(line, x, y);
			y += dH;
		}
	}
	
	
	
	/**
	 * Returns the desired transparency level for this AboutScreen
	 * @return 25% transparent
	 */
	@Override
	public float getDesiredOpacityLevel()
	{
		return 0.75f;
	}
}
