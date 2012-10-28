
package victorypaint.DrawingCreators;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.MouseEvent;
import victorypaint.Drawables.LineSegment;

/**
 * A LineSegmentCreator constructs a LineSegment based on user input from a specified Component.
 * A LineSegment is first created when a user presses down on the mouse in the Component,
 * the second point is changed as the user drags the mouse around inside the Component,
 * and the LineSegment is finalized when the user lets go of the mouse or
 * the mouse is moved outside of the input Component.
 * @author Jesse Victors, A01485921
 */
public class LineSegmentCreator extends DrawableCreator
{
	/**
	 * The LineSegment being created
	 */
	private LineSegment lineSegment = new LineSegment(new Point());


	/**
	 * Creates a LineSegmentCreator using the specified Component for user input
	 * @param input the input Component, must be non-null.
	 */
	public LineSegmentCreator(Component input)
	{
		super(input);
	}



	/**
	 * Returns the LineSegment currently being constructed.
	 * @return the LineSegment
	 */
	@Override
	public LineSegment getDrawable()
	{
		return lineSegment;
	}



	/**
	 * Called when the user presses down on the mouse whilst inside the input Component.
	 * This method sets up the LineSegment and makes both end points at the mouse's location.
	 * @param e the MouseEvent corresponding to the mouse press, must be non-null
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		creating = true;
		lineSegment.setPointA(e.getPoint());
		lineSegment.setPointB(e.getPoint());
		lineSegment.setGhosting(true);
	}



	/**
	 * Called when the user lets go of the mouse button whilst inside the input Component.
	 * This method finalizes the LineSegment.
	 * @param e the MouseEvent corresponding to the mouse release
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		finish();
		lineSegment.setGhosting(false);
	}



	/**
	 * Called when the user's mouse exits the input Component.
	 * This method calls mouseReleased(e), which finalizes the LineSegment.
	 * @param e the MouseEvent corresponding to the mouse exiting
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{
		mouseReleased(e);
	}



	/**
	 * Called when the user's mouse is dragged around in the input Component.
	 * This method changes Point B of the LineSegment to the mouse's location.
	 * @param e the MouseEvent corresponding to the mouse drag, must be non-null
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		lineSegment.setPointB(e.getPoint());
	}
}
