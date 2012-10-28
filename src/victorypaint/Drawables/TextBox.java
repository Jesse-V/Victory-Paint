
package victorypaint.Drawables;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;

/**
 * A TextBox displays a single line of modifiable text at a given location.
 * @author Jesse Victors, A01485921
 */
public class TextBox extends Drawable
{
	/**
	 * Font to use when displaying the text
	 */
	private static Font defaultFont = new Font("Times New Roman", Font.PLAIN, 24);
	
	/**
	 * Holds the text
	 */
	private StringBuffer text = new StringBuffer(16);
	
	/**
	 * A vector representing the difference between getLocation() and where the text should be drawn.
	 * This is needed because graphics.drawString(str, x, y) takes coordinates
	 * which are neither the top-right nor the bottom-right corner of the bounds of the text.
	 * Updated by calling updateTextBounds(graphics)
	 */
	private Point offset = new Point();
	
	/**
	 * The width and height of the current text. Updated by calling updateTextBounds(graphics)
	 */
	private Dimension textBounds = new Dimension();


	/**
	 * Constructs a TextBox at the specified location
	 * @param location the initial location for the TextBox
	 */
	public TextBox(Point location)
	{
		super(location);
		points.add(new Point(location));
		points.add(new Point(location));
		points.add(new Point(location));
	}
	
	

	/**
	 * Returns the text currently in this TextBox.
	 * @return the current text
	 */
	public String getText()
	{
		return text.toString();
	}

	
	
	/**
	 * Uses the current text to update the offset and textBounds as rendered on the given Graphics object.
	 * @param graphics used for rendering the text. Must be non-null.
	 */
	public void updateTextBounds(Graphics graphics)
	{
		//modified from:
		//http://docs.oracle.com/javase/tutorial/2d/text/measuringtext.html
		//http://docs.oracle.com/javase/tutorial/2d/text/drawmulstring.html
		
		Rectangle2D rect = graphics.getFontMetrics().getStringBounds(getText(), graphics);
		offset = new Point((int)Math.round(rect.getMinX()), (int)Math.round(rect.getMinY()));
		textBounds = new Dimension((int)Math.round(rect.getWidth()), (int)Math.round(rect.getHeight()));
		
		Point loc = getLocation();
		points.set(1, new Point(loc.x + textBounds.width, loc.y));
		points.set(2, new Point(loc.x + textBounds.width, loc.y + textBounds.height));
		points.set(3, new Point(loc.x, loc.y + textBounds.height));
	}



	/**
	 * Appends the given text to the current text.
	 * @param str the text to append. Must be non-null.
	 */
	public void processKey(char c)
	{
		if (c == KeyEvent.VK_BACK_SPACE)
			text.deleteCharAt(text.length()-1);
		else
			text.append(c);
	}

	

	/**
	 * Draws this TextBox at its location.
	 * @param g the Graphics to draw on, must be non-null
	 */
	@Override
	protected void paint(Graphics g)
	{
		Font oldFont = g.getFont();
		g.setFont(defaultFont);

		updateTextBounds(g);
		Point loc = getLocation();

		if (isGhosting())
			g.drawRect(loc.x, loc.y, textBounds.width, textBounds.height);
		g.drawString(getText(), loc.x - offset.x, loc.y - offset.y);

		g.setFont(oldFont);
	}

	
	
	/**
	 * Overrides normal functionality and instead does nothing. Such modification
	 * would be inappropriate unless the text's font could change as well.
	 * @param n
	 * @param newPoint 
	 */
	@Override
	public void setPoint(int n, Point newPoint)
	{ }
	
	
	
	/**
	 * Makes a deep copy clone of this TextBox and returns the result.
	 * @return a deep copy of this TextBox
	 */
	@Override
	public TextBox clone()
	{
		TextBox tb = new TextBox(getLocation());
		tb.points = clonePointList();
		tb.text = new StringBuffer(text);
		tb.offset = new Point(offset);
		tb.textBounds = new Dimension(textBounds);
		return tb;
	}
}
