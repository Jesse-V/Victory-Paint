
package victorypaint.Drawables;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A Drawable is any user-created element that can be drawn on the screen.
 * Drawables contain a Location, any additional Points, and a Color.
 * When they are being created but not yet fully formed, they are "ghosting",
 * which renders them in a more transparently than their usual customizable color.
 * @author Jesse Victors, A01485921
 */
public abstract class Drawable implements Serializable
{
	/**
	 * The Points that define the Drawable.
	 * It is guaranteed to contain at least one Point.
	 */
	protected ArrayList<Point> points = new ArrayList<>(4);
	
	/**
	 * Holds if this Drawable is ghosting or not. If it is,
	 * GHOSTING_COLOR will be used to color this Drawable,
	 * REGULAR_COLOR otherwise.
	 */
	protected boolean ghosting = false;

	/**
	 * Color to be used if this Drawable is ghosting.
	 * This is usually gray.
	 */
	protected static final Color GHOSTING_COLOR = Color.GRAY;

	/**
	 * Color to be used if this Drawable is not ghosting.
	 */
	protected Color color = Color.BLACK;


	/**
	 * Constructs a Drawable at the specified location.
	 * @param initLocation The initial location for the Drawable.
	 * This becomes the first Point that describes the Drawable.
	 * Must be non-null.
	 */
	public Drawable(Point initLocation)
	{
		points.add(initLocation);
	}



	/**
	 * Returns the location of this Drawable.
	 * @return the location, which is the first Point
	 * that describes this drawable
	 */
	public Point getLocation()
	{
		return getPoint(0);
	}



	/**
	 * Sets the location of this Drawable to the new location.
	 * This will translate the entire Drawable so that its first Point
	 * is at the given location.
	 * @param newLoc The new location for the Drawable. Must be non-null.
	 */
	public void setLocation(Point newLoc)
	{
		Point oldLoc = getLocation();
		Point delta = new Point(newLoc.x - oldLoc.x, newLoc.y - oldLoc.y);

		for (int j = 0; j < points.size(); j++)
			getPoint(j).translate(delta.x, delta.y);
	}

	

	/**
	 * Projects the given Point onto this Drawable.
	 * This should return the closest Point on this Drawable to loc,
	 * but the returned Point may not necessarily be a Point which defines this Drawable.
	 * In other words, while the returned Point is as close as possible to loc,
	 * there is no guarantee that there exists an n such that getPoint(n) == projectOnto(loc)
	 * @param loc the comparative location. Must be non-null.
	 * @return the Drawable's closest point to the given point
	 */
	public Point projectOnto(Point loc)
	{
		Point closestPt = getClosestPoint(loc);
		double closestDist = closestPt.distance(loc);
		int closestPtInd = points.indexOf(closestPt);
		
		if (closestPtInd - 1 >= 0)
		{
			Point pt = projectOntoLineSegment(getPoint(closestPtInd - 1), closestPt, loc);
			double dist = pt.distance(loc);
			if (dist < closestDist)
			{
				closestPt = pt;
				closestDist = dist;
			}
		}
		
		if (closestPtInd + 1 < points.size())
		{
			Point pt = projectOntoLineSegment(getPoint(closestPtInd + 1), closestPt, loc);
			double dist = pt.distance(loc);
			if (dist < closestDist)
			{
				closestPt = pt;
				closestDist = dist;
			}
		}
		
		return closestPt;
	}
	
	
	
	/**
	 * Projects Point c onto the line segment formed between the Points a and b.
	 * It returns a Point midway between a and b along this line segment which is
	 * as close as possible to c.
	 * @param a the first Point specifying the AB line segment
	 * @param b the second Point specifying the AB line segment
	 * @param c the Point which will be projected on to AB
	 * @return a Point along the AB line segment which is close as possible to c.
	 * As this must be on the line segment, this may return a copy of either a or b.
	 */
	protected static Point projectOntoLineSegment(Point a, Point b, Point c)
	{
		//modified from:
		//http://stackoverflow.com/questions/3120357/get-closest-point-to-a-line
		//http://math.stackexchange.com/questions/13176/how-to-find-a-point-on-a-line-closest-to-another-given-point
		
		Point AB = new Point(b.x - a.x, b.y - a.y); //Vector from A to B
		Point AC = new Point(c.x - a.x, c.y - a.y); //Vector from A to C

		double ABAP = AB.x * AC.x + AB.y * AC.y; //dot product of AB and AP
		double percentage = ABAP / (Math.pow(AB.x, 2) + Math.pow(AB.y, 2)); //percentage down the line segment the point is on
		percentage = Math.min(1, Math.max(0, percentage)); //ensures 0 <= percentage <= 1

		return new Point((int)Math.round(a.x + AB.x * percentage), (int)Math.round(a.y + AB.y * percentage));
	}
	
	
	
	/**
	 * Calculates and returns the closest point which defines this Drawable to the specified point.
	 * @param loc the comparative location. Must be non-null.
	 * @return the Drawable's closest point to the given point
	 */
	public Point getClosestPoint(Point loc)
	{
		double closestDist = Double.POSITIVE_INFINITY;
		Point closestPt = new Point();

		for (int j = 0; j < points.size(); j++)
		{
			double dist = getPoint(j).distance(loc);
			if (dist < closestDist)
			{
				closestDist = dist;
				closestPt = getPoint(j);
			}
		}

		return closestPt;
	}



	/**
	 * Returns the nth Point which describes this Drawable.
	 * @param n The index of the desired point.
	 * If n is less than zero or greater than the number of
	 * points in this Drawable, an IndexOutOfBoundsException may be thrown.
	 * @return the nth Point of this Drawable
	 */
	public Point getPoint(int n)
	{
		return points.get(n);
	}
	
	
	
	/**
	 * Sets the nth Point which describes this Drawable to the specified new Point.
	 * @param n The index of the point to override.
	 * If n is less than zero or greater than the number of
	 * points in this Drawable, an IndexOutOfBoundsException may be thrown.
	 * @return the nth Point of this Drawable
	 */
	public void setPoint(int n, Point newPoint)
	{
		points.set(n, newPoint);
	}
	
	
	/**
	 * @return the number of Points which describes this Drawable
	 */
	public int getPointCount()
	{
		return points.size();
	}



	/**
	 * Sets the ghosting property of this Drawable to the specified value.
	 * @param ghosting the new ghosting property value
	 */
	public void setGhosting(boolean ghosting)
	{
		this.ghosting = ghosting;
	}



	/**
	 * Returns whether this Drawable is ghosting or not.
	 * @return the value of the ghosting property
	 */
	public boolean isGhosting()
	{
		return ghosting;
	}



	/**
	 * Returns the current color of this Drawable.
	 * If this Drawable is ghosting, this method
	 * will return GHOSTING_COLOR, otherwise REGULAR_COLOR.
	 * @return the current color
	 */
	public Color getCurrentColor()
	{
		if (isGhosting())
			return GHOSTING_COLOR;
		else
			return color;
	}
	
	
	
	/**
	 * Sets the default (non-ghosting) color to the specified Color.
	 * @param newColor the new default Color
	 */
	public void setDefaultColor(Color newColor)
	{
		color = newColor;
	}



	/**
	 * Draws this Drawable on the given Graphics object.
	 * Although this function will draw using the current
	 * color, g's original color will be preserved.
	 * @param g the Graphics to draw on. Must be non-null.
	 */
	public void draw(Graphics g)
	{
		Color tempColor = g.getColor();
		g.setColor(getCurrentColor());

		paint(g);

		g.setColor(tempColor);
	}
	
	
	
	/**
	 * Simply paints this Drawable without performing any
	 * coloring operations.
	 * @param g the Graphics to draw on. Must be non-null.
	 */
	protected abstract void paint(Graphics g);
	
	
	
	/**
	 * Makes a deep copy clone of this Drawable and returns the result.
	 * @return a deep copy of this Drawable
	 */
	@Override
	public abstract Drawable clone();
	
	
	
	/**
	 * Makes a deep copy of the Points which define this Drawable and returns the result.
	 * @return a deep 
	 */
	protected ArrayList<Point> clonePointList()
	{
		ArrayList<Point> list = new ArrayList<>(points.size());
		for (int j = 0; j < points.size(); j++)
			list.add(new Point(points.get(j)));
		return list;
	}
}
