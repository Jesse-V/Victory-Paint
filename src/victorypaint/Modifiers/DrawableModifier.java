
package victorypaint.Modifiers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.List;
import victorypaint.Drawables.Drawable;
import victorypaint.MouseInputAcceptor;

/**
 * The superclass for all classes which are designed to modify a drawable.
 * This class holds references to a mouse location the closest Drawable to it,
 * the closest point on a Drawable to the mouse location, and closest projection
 * point of the mouse location onto a Drawable. This class also holds other location
 * variables and has functions defined which should take care of tasks common to
 * all DrawableModifiers, such as reducing chances of accidental selection if the
 * mouse is far away from a Drawable.
 * Subclasses can override functionality to modify a Drawable in their own way.
 * Subclasses define the exact modification how the user will modify the Drawable.
 * @author Jesse Victors, A01485921
 */
public abstract class DrawableModifier extends MouseInputAcceptor
{
	/**
	 * Holds the last known mouse location,
	 * since that's otherwise only accessible from MouseEvent
	 */
	private Point mouseLoc = new Point();
	
	/**
	 * Holds the Point returned when mouseLoc is projected onto a Drawable
	 * using the Drawable's projectOnto function.
	 */
	private Point closestProjectionPt;
	
	/**
	 * Holds the closest Point on a Drawable to mouseLoc
	 */
	private Point closestPt;
	
	/**
	 * Holds a reference to the Drawable which is closest to mouseLoc
	 */
	private Drawable closestDrawable;
	
	/**
	 * Holds whether a Drawable has been captured or not.
	 * If a Drawable has been captured, then the user can modify it
	 */
	protected boolean captured;
	
	/**
	 * Holds the location of the mouse when the Drawable was captured.
	 */
	protected Point mouseLocAtCapture;
	
	/**
	 * Holds the original location of the Drawable when it was captured.
	 */
	protected Point locAtCapture;
	
	
	/**
	 * Constructs a DrawableModifier using the specified Component for user input.
	 * @param input the input Component, must be non-null.
	 */
	public DrawableModifier(Component input)
	{
		super(input);
	}



	/**
	 * Calculates and finds the closest Drawable and the closest point on that Drawable
	 * to the last know mouse location, which was determined from setMouseLoc(Point).
	 * @param drawables the list of Drawables to analyze
	 */
	public void calculateSelection(List<Drawable> drawables)
	{
		if (!acceptingUserInput() || captured)
			return;

		double ptClosestDist = Double.POSITIVE_INFINITY;
		double projectionPtClosestDist = Double.POSITIVE_INFINITY;
		
		for (Drawable drawable : drawables)
		{
			Point pt = drawable.getClosestPoint(mouseLoc);
			Point projPt = drawable.projectOnto(mouseLoc);
			
			double ptDist = pt.distance(mouseLoc);
			double projDist = projPt.distance(mouseLoc);
			
			if (ptDist < ptClosestDist)
			{
				closestPt = pt;
				ptClosestDist = ptDist;
			}
			
			if (projDist < projectionPtClosestDist)
			{
				projectionPtClosestDist = projDist;
				closestProjectionPt = projPt;
				closestDrawable = drawable;
			}
		}
	}



	/**
	 * @return the closest Drawable to last known mouse location.
	 * This value may change with subsequent calls to calculateSelection(List<Drawables>).
	 */
	public Drawable getClosestDrawable()
	{
		return closestDrawable;
	}



	/**
	 * @return the closest point on a Drawable to last known mouse location.
	 * This value may change with subsequent calls to calculateSelection(List<Drawables>).
	 */
	public Point getClosestPt()
	{
		return closestPt;
	}


	/**
	 * @return the closest point which is a projection of the last known mouse location onto a Drawable.
	 * This value may change with subsequent calls to calculateSelection(List<Drawables>).
	 */
	public Point getClosestProjectionPt()
	{
		return closestProjectionPt;
	}
	
	

	/**
	 * @return the value of the mouse location variable.
	 * This value may change with subsequent calls to calculateSelection(List<Drawables>).
	 */
	public Point getMouseLoc()
	{
		return mouseLoc;
	}



	/**
	 * Updates the mouse location variable to the given point.
	 * @param newMouseLoc the new value for the mouse location variable.
	 */
	public void setMouseLoc(Point newMouseLoc)
	{
		mouseLoc = newMouseLoc;
	}

	
	
	/**
	 * Illustrates basics of this DrawableModifier by drawing a line from the
	 * last known mouse location to the closest projection point only if this DrawableModifieer
	 * is accepting user input, hasn't captured a Drawable, and if the mouse location is close enough.
	 * @param g the Graphics to draw on, must be non-null
	 */
	public void draw(Graphics g)
	{
		if (acceptingUserInput() && !captured && closeEnough())
		{
			g.setColor(Color.LIGHT_GRAY);

			Point pt = getClosestProjectionPt();
			g.drawLine(getMouseLoc().x, getMouseLoc().y, pt.x, pt.y);
		}
	}
	
	
	
	/**
	 * Called when the user's mouse is enters the input Component.
	 * Updates mouseLoc based on the current position of the mouse.
	 * @param e the MouseEvent corresponding to the mouse enter, must be non-null
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{
		setMouseLoc(e.getPoint());
	}
	
	
	
	/**
	 * Called when the user's mouse exits the input Component.
	 * Removes the ability to receive any further user input for this DrawableTranslator.
	 * If it was currently translating a Drawable, it unghosts that Drawable.
	 * @param e the MouseEvent corresponding to the mouse exiting
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		captured = false;
		finish();
		if (getClosestDrawable() != null)
			getClosestDrawable().setGhosting(false);
	}
	
	
	
	/**
	 * Called when the user's mouse is pressed down whilst inside the input Component.
	 * If the mouse's location is close enough to closest point from a Drawable,
	 * captures that Drawable so that the user can then modify it.
	 * @param e the MouseEvent corresponding to the mouse press, must be non-null
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		setMouseLoc(e.getPoint());
		
		if (closeEnough())
		{
			captured = true;
			mouseLocAtCapture = e.getPoint();

			if (getClosestDrawable() != null)
				locAtCapture = new Point(getClosestDrawable().getLocation());
		}
	}
	
	
	
	/**
	 * Called when the user lets go of the mouse button whilst inside the input Component.
	 * This method simply calls mouseExited(s)
	 * @param e the MouseEvent corresponding to the mouse release
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		mouseExited(e);
	}


	
	/**
	 * Called when the user's mouse is moved around in the input Component.
	 * Updates mouseLoc based on the current position of the mouse.
	 * @param e the MouseEvent corresponding to the mouse move, must be non-null
	 */
	@Override
	public void mouseMoved(MouseEvent e)
	{
		setMouseLoc(e.getPoint());
	}
	
	
	
	/**
	 * Determines whether the closest point of a Drawable is close enough to the mouse point
	 * that its reasonable that the user wants to modify that particular Drawable.
	 * @return true if its close enough, false otherwise
	 */
	public boolean closeEnough()
	{
		Point pt = getClosestProjectionPt();
		if (pt == null)
			return false;
		return pt.distance(getMouseLoc()) <= 100;
	}
}
