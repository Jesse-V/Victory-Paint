
package victorypaint.Modifiers;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import victorypaint.Drawables.Drawable;

/**
 * With a DrawableRotator, the user can rotate a selected Drawable around one of its points.
 * The user does this by pressing the mouse down near the desired center point,
 * dragging the mouse in a circular fashion to rotate in real-time,
 * and releases the mouse to complete the operation.
 * @author Jesse Victors, A01485921
 */
public class DrawableRotator extends DrawableModifier
{
	/**
	 * Maintains the original version of the selected Drawable
	 */
	private Drawable oldCopy;
	private double oldAngle;
	
	
	/**
	 * Constructs a DrawableEditor using the specified Component for user input.
	 * @param input the input Component, must be non-null.
	 */
	public DrawableRotator(Component input)
	{
		super(input);
	}
	
	
	
	/**
	 * Called when the user's mouse is pressed down whilst inside the input Component.
	 * Calls DrawableModifier's mousePressed method, and captures the closest Drawable
	 * for rotation no matter how far away the user's mouse is from it.
	 * @param e the MouseEvent corresponding to the mouse press, must be non-null
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		
		Drawable drawable = getClosestDrawable();
		if (drawable != null)
		{
			drawable.setGhosting(true);
			oldCopy = drawable.clone();
			oldAngle = getAngle();
		}
	}
	
	
	
	/**
	 * Called when the user's mouse is dragged around in the input Component.
	 * Updates the rotation of the selected Drawable based on the angle between
	 * the mouse location and the point the Drawable is rotating around.
	 * @param e the MouseEvent corresponding to the mouse drag, must be non-null
	 */
	@Override
	public void mouseDragged(MouseEvent e)
	{
		mouseMoved(e);

		if (getClosestDrawable() != null && captured)
		{
			double angle = getAngle() - oldAngle;
			double sinAngle = Math.sin(angle), cosAngle = Math.cos(angle); //bit of optimization: only computes this once
			
			for (int j = 0; j < oldCopy.getPointCount(); j++)
				getClosestDrawable().setPoint(j, 
						rotatePoint(oldCopy.getPoint(j), getClosestProjectionPt(), sinAngle, cosAngle));
		}
	}

	

	/**
	 * @return the radian angle between the mouse location and the point the Drawable is rotating around.
	 */
	private double getAngle()
	{
		return getAngle(getMouseLoc(), getClosestProjectionPt());
	}
	
	
	
	/**
	 * @param a first Point
	 * @param b second Point
	 * @return the radian angle between the two Points
	 */
	public static double getAngle(Point a, Point b)
	{
		return Math.atan2(b.y - a.y, b.x - a.x);
	}



	/**
	 * Illustrates this DrawableRotator. A gray line is drawn from the mouse location to the
	 * closest projection point from any Drawable. If the user has selected and is currently 
	 * rotating a Drawable, a blue arc is drawn which illustrates the rotation.
	 * @param g the Graphics object to draw on, must be non-null
	 */
	@Override
	public void draw(Graphics g)
	{
		if (acceptingUserInput() && getClosestPt() != null)
		{
			g.setColor(Color.LIGHT_GRAY);
			g.drawLine(getMouseLoc().x, getMouseLoc().y, getClosestProjectionPt().x, getClosestProjectionPt().y);
			
			if (captured)
			{
				g.setColor(Color.BLUE);
				
				int radius = (int)Math.round(getMouseLoc().distance(getClosestProjectionPt()));
				Point pt = new Point(getClosestProjectionPt().x - radius, getClosestProjectionPt().y - radius);

				int startAngle = 180 - (int)Math.toDegrees(oldAngle);
				if (startAngle < 0)
					startAngle += 360;
				
				int arcAngle = -(int)Math.toDegrees(getAngle() - oldAngle);
				if (arcAngle < 0)
					arcAngle += 360;
				
				g.drawArc(pt.x, pt.y, radius * 2, radius * 2, startAngle, arcAngle);
			}
		}
	}

	
	
	/**
	 * @return true, always.
	 */
	@Override
	public boolean closeEnough()
	{
		return true;
	}
	
	
	
	/**
	 * Rotates the given Point around the given pivot Point the given radian angle.
	 * @param p the Point to rotate
	 * @param pivot the Point to use as the rotation origin
	 * @param angle the angle of rotation
	 * @return a new Point representing "p" rotated around "pivot" by the given angle
	 */
	public static Point rotatePoint(Point p, Point pivot, double angle)
	{
		return rotatePoint(p, pivot, Math.sin(angle), Math.cos(angle));
	}
	
	
	
	/*
	 * Same as rotatePoint, except that the sin and cosine of angle are given
	 * instead of recomputed.
	 */
	private static Point rotatePoint(Point p, Point pivot, double sinAngle, double cosAngle)
	{ //http://stackoverflow.com/questions/2259476/rotating-a-point-about-another-point-2d
		
		Point newPt = new Point(p.x - pivot.x, p.y - pivot.y);
		
		double xnew = newPt.x * cosAngle - newPt.y * sinAngle;
		double ynew = newPt.x * sinAngle + newPt.y * cosAngle;

		return new Point((int)Math.round(xnew + pivot.x), (int)Math.round(ynew + pivot.y));
	}
}
