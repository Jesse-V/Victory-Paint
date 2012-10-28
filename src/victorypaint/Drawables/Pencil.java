
package victorypaint.Drawables;

import java.awt.Graphics;
import java.awt.Point;

/**
 * A Pencil is open curve specified by a flexible set of Points.
 * @author Jesse Victors, A01485921
 */
public class Pencil extends Drawable
{
	/**
	 * Constructs a Pencil at the specific Location,
	 * which is the Pencil's first point.
	 * @param firstPoint the initial location for the Pencil.
	 * Must be non-null.
	 */
	public Pencil(Point firstPoint)
	{
		super(firstPoint);

	}
	
	

	/**
	 * Adds the given Point to the curve.
	 * @param pt the new Point to append to the current set of Points.
	 * Must be non-null.
	 */
	public void addPoint(Point pt)
	{
		points.add(pt);
	}



	/**
	 * Draws the open Pencil by drawing line segments between consecutive pairs of points,
	 * but does not draw a line from the Pencil's last point to the Pencil's first point.
	 * @param g the Graphics to draw on. Must be non-null.
	 */
	@Override
	protected void paint(Graphics g)
	{
		for (int j = 0; j < points.size() - 1; j++)
			g.drawLine(points.get(j).x, points.get(j).y, points.get(j + 1).x, points.get(j + 1).y);
	}
	
	
	
	/**
	 * Makes a deep copy clone of this Pencil and returns the result.
	 * @return a deep copy of this Pencil
	 */
	@Override
	public Pencil clone()
	{
		Pencil c = new Pencil(getLocation());
		c.points = clonePointList();
		return c;
	}
}
