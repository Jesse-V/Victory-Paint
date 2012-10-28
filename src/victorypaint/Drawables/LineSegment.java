
package victorypaint.Drawables;

import java.awt.Graphics;
import java.awt.Point;

/**
 * A LineSegment is a Drawable specified by two Points, referred to as A and B.
 * @author Jesse Victors, A01485921
 */
public class LineSegment extends Drawable
{
	/**
	 * Constructs a LineSegment with zero length,
	 * as both A and B are set to the given position.
	 * @param initLocation the initial location for the LineSegment
	 */
	public LineSegment(Point initLocation)
	{
		super(initLocation);
		points.add(initLocation);
	}



	/**
	 * Returns Point A, the first point of this LineSegment.
	 * @return Point A, which is always the same as getLocation()
	 */
	public Point getPointA()
	{
		return getLocation();
	}



	/**
	 * Sets Point A to the specified new point.
	 * Unlike setLocation(Point), this does not translate the LineSegment.
	 * @param newPoint the new value for Point A
	 */
	public void setPointA(Point newPoint)
	{
		points.set(0, newPoint);
	}



	/**
	 * Returns Point B, the second point of this LineSegment.
	 * @return Point B
	 */
	public Point getPointB()
	{
		return getPoint(1);
	}



	/**
	 * Sets Point B to the specified new point.
	 * @param newPoint the new value for Point B
	 */
	public void setPointB(Point newPoint)
	{
		points.set(1, newPoint);
	}

	

	/**
	 * Draws a line segment between Point A and Point B
	 * @param g the Graphics to draw on
	 */
	@Override
	protected void paint(Graphics g)
	{
		Point a = getPointA(), b = getPointB();
		g.drawLine(a.x, a.y, b.x, b.y);
	}
	
	
	
	/**
	 * Makes a deep copy clone of this LineSegment and returns the result.
	 * @return a deep copy of this LineSegment
	 */
	@Override
	public LineSegment clone()
	{
		LineSegment ls = new LineSegment(getLocation());
		ls.points = clonePointList();
		return ls;
	}
}
