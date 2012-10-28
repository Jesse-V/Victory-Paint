
package victorypaint.Modifiers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;
import victorypaint.Drawables.Drawable;

/**
 * A DrawableCopier is very similar to a DrawableTranslator, except that it creates
 * a clone of the selected Drawable and the user moves that one around instead of the original.
 * @author Jesse Victors, A01485921
 */
public class DrawableCopier extends DrawableModifier
{
	/**
	 * A reference to DrawingPanel's list of drawables.
	 * The clone of the selected Drawable will be added to this list.
	 */
	private List<Drawable> drawableList;
	
	/**
	 * Holds a copy of the newly created clone of the selected Drawable.
	 */
	private Drawable newCopy;
	
	
	/**
	 * Creates a DrawableCopier using the specified Component for user input
	 * and the specified list of Drawables to add the newly created Drawable to.
	 * @param input the Component used for user input, must be non-null
	 * @param list the list of Drawables, assumed to be identical to the list
	 * passed to calculateSelection.
	 */
	public DrawableCopier(Component input, List<Drawable> list)
	{
		super(input);
		drawableList = list;
	}
	
	
	
	/**
	 * Called when the user's mouse is pressed down whilst inside the input Component.
	 * If the mouse's location is close enough to closest point from a Drawable,
	 * clones that Drawable and adds it to the list of Drawables.
	 * It also gives the user the ability to move it around.
	 * @param e the MouseEvent corresponding to the mouse press, must be non-null
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		
		Drawable drawable = getClosestDrawable();
		if (drawable != null)
		{
			newCopy = drawable.clone();
			newCopy.setGhosting(true);
			drawableList.add(newCopy);
		}
	}
	
	
	
	/**
	 * Called when the user's mouse is dragged around in the input Component.
	 * Updates the position of the newly created clone of the selected Drawable.
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
		}
	}
	
	
	
	/**
	 * Calls DrawableModifier's draw function,
	 * and further illustrates this DrawableCopier by drawing a blue line from the location
	 * of the original Drawable to the location of its new clone. This helps the user see its
	 * change in position.
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
	
	
	
	@Override
	public Drawable getClosestDrawable()
	{
		if (newCopy == null)
			return super.getClosestDrawable();
		return newCopy;
	}
}
