package victorypaint.Modifiers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * A DrawableTranslator gives the user the ability to select and translate a Drawable
 * based on mouse input. If the mouse is close enough, the user can drag the Drawable around.
 * @author Jesse Victors, A01485921
 */
public class DrawableTranslator extends DrawableModifier
{
	/**
	 * Constructs a DrawableTranslator using the specified Component for user input.
	 * @param input the input Component, must be non-null.
	 */
	public DrawableTranslator(Component input)
	{
		super(input);
	}

	
	
	/**
	 * Called when the user's mouse is dragged around in the input Component.
	 * Translates any selected Drawable by updating its location relative to where
	 * the user started dragging their mouse. Also calls mouseMoved(e)
	 * @param e the MouseEvent corresponding to the mouse drag, must be non-null
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);

		if (getClosestDrawable() != null && captured)
		{
			Point diff = new Point(getMouseLoc().x - mouseLocAtCapture.x, getMouseLoc().y - mouseLocAtCapture.y);
			getClosestDrawable().setLocation(new Point(diff.x + locAtCapture.x, diff.y + locAtCapture.y));
			getClosestDrawable().setGhosting(true);
		}
	}

	
	
	/**
	 * Calls DrawableModifier's draw function, and if the user has selected and is translating
	 * a Drawable, a blue line is drawn from its original location point to its current location.
	 * @param g the Graphics to draw on, must be non-null
	 */
	@Override
	public void draw(Graphics g)
	{
		super.draw(g);
		
		if (acceptingUserInput() && captured)
		{
			g.setColor(Color.BLUE);
			
			Point pt = getClosestDrawable().getLocation();
			g.drawLine(pt.x, pt.y, locAtCapture.x, locAtCapture.y);
		}
	}
}
