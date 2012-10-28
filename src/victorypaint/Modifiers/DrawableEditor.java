
package victorypaint.Modifiers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * A DrawableEditor allows the user to change a particular point on a Drawable.
 * Analogous to DrawableTranslator, the user does this by pressing the mouse 
 * down near the desired point, dragging it around, and releasing the mouse when finished.
 * @author Jesse Victors, A01485921
 */
public class DrawableEditor extends DrawableModifier
{
	/**
	 * The index of the selected Point on the closest Drawable
	 */
	private int index = -1;
	
	
	/**
	 * Constructs a DrawableEditor using the specified Component for user input.
	 * @param input the input Component, must be non-null.
	 */
	public DrawableEditor(Component input)
	{
		super(input);
	}
	
	
	
	/**
	 * Called when the user's mouse is pressed down whilst inside the input Component.
	 * This method helps reference the Point the user would like to change.
	 * @param e the MouseEvent corresponding to the mouse press, must be non-null
	*/
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		
		if (index == -1)
			for (int j = 0; j < getClosestDrawable().getPointCount(); j++)
				if (getClosestDrawable().getPoint(j) == getClosestPt())
					index = j;
	}
	
	
	
	/**
	 * Called when the user's mouse is dragged around in the input Component.
	 * Updates the position of the selected Point from a drawable based on mouse movement.
	 * @param e the MouseEvent corresponding to the mouse drag, must be non-null
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);

		if (getClosestDrawable() != null && captured)
		{
			Point diff = new Point(getMouseLoc().x - mouseLocAtCapture.x, getMouseLoc().y - mouseLocAtCapture.y);
			
			getClosestDrawable().setPoint(index, new Point(diff.x + getClosestPt().x, diff.y + getClosestPt().y));
			getClosestDrawable().setGhosting(true);
		}
	}
	
	
	
	/**
	 * Illustrates this DrawableEditor. If a point is not currently being modified,
	 * this simply draws a light gray line from the mouse location to the closest point.
	 * However, if a point has been selected to be modified, it draws a blue line
	 * from its original location to its current location.
	 * @param g 
	 */
	@Override
	public void draw(Graphics g)
	{
		if (acceptingUserInput() && !captured && closeEnough())
		{ //illustrate basics
			
			g.setColor(Color.LIGHT_GRAY);

			Point pt = getClosestPt();
			g.drawLine(getMouseLoc().x, getMouseLoc().y, pt.x, pt.y);
		}
		
		if (acceptingUserInput() && captured)
		{ //illustrate change
			
			g.setColor(Color.BLUE);

			Point pt = getClosestDrawable().getPoint(index);
			g.drawLine(pt.x, pt.y, getClosestPt().x, getClosestPt().y);
		}
	}
}
