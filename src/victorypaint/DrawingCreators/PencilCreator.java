
package victorypaint.DrawingCreators;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import victorypaint.Drawables.Pencil;

/**
 * A PencilCreator constructs a Pencil based on user input from a specified Component.
 * A Pencil is first created when a user presses down on the mouse in the Component,
 * points are added to the Pencil when the mouse is dragged around on the Component,
 * and the Pencil is finalized when the user lets go of the mouse or
 * the mouse is moved outside of the input Component.
 * @author Jesse Victors, A01485921
 */
public class PencilCreator extends DrawableCreator implements MouseListener, MouseMotionListener
{
	/**
	 * The Pencil being created.
	 */
	private Pencil curve = new Pencil(new Point());


	/**
	 * Creates a PencilCreator using the specified Component for user input
	 * @param input the input Component, must be non-null.
	 */
	public PencilCreator(Component input)
	{
		super(input);
	}



	/**
	 * Returns the Pencil currently being constructed.
	 * @return the Pencil
	 */
	@Override
	public Pencil getDrawable()
	{
		return curve;
	}



	/**
	 * Called when the user presses down on the mouse whilst inside the input Component.
	 * This method sets up the Pencil
	 * and moves it to the mouse's location.
	 * @param e the MouseEvent corresponding to the mouse press, must be non-null
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		creating = true;
		curve.setLocation(e.getPoint());
		curve.setGhosting(true);
	}



	/**
	 * Called when the user lets go of the mouse button whilst inside the input Component.
	 * This method finalizes the Pencil.
	 * @param e the MouseEvent corresponding to the mouse release
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		finish();
		curve.setGhosting(false);
	}



	/**
	 * Called when the user's mouse exits the input Component.
	 * This method calls mouseReleased(e), which finalizes the Pencil.
	 * @param e the MouseEvent corresponding to the mouse exiting
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		mouseReleased(e);
	}



	/**
	 * Called when the user's mouse is dragged around in the input Component.
	 * This method adds the mouse's location to the Pencil.
	 * @param e the MouseEvent corresponding to the mouse drag, must be non-null
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		curve.addPoint(e.getPoint());
	}
}
