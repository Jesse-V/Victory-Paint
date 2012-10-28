
package victorypaint;

import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.JPanel;
import victorypaint.Drawables.Drawable;
import victorypaint.Modifiers.DrawableModifier;

/**
 * A DrawingPanel is exactly identical to a JPanel,
 * except that it also draws all the elements from a list of Drawables, which it maintains.
 * It also holds a DrawableModifier intended for modifying a given Drawable.
 * @author Jesse Victors, A01485921
 */
public class DrawingPanel extends JPanel
{
	/**
	 * The list of Drawables
	 */
	protected ArrayList<Drawable> drawables = new ArrayList<>(16);
	
	/**
	 * Reference to any DrawableModifier. May be null.
	 */
	private DrawableModifier modifier;

	
	/**
	 * Updates the current DrawableModifier to the given DrawableModifier
	 * @param dm the new DrawableModifier
	 */
	public void setDrawableModifier(DrawableModifier dm)
	{
		modifier = dm;
	}

	

	/**
	 * Draws the default JPanel, including any backgrounds and components,
	 * and then draws all of the Drawables this this DrawingPanel has.
	 * @param g the Graphics to draw to, must be non-null
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		for (Drawable drawable : drawables)
			drawable.draw(g);

		if (modifier != null)
		{
			modifier.calculateSelection(drawables);
			modifier.draw(g);
		}
	}



	/**
	 * Adds the given Drawable to the list.
	 * @param drawable the Drawable to add to the list.
	 * If this is null, this method does nothing.
	 */
	public void add(Drawable drawable)
	{
		if (drawable != null)
			drawables.add(drawable);
	}
	
	
	
	/**
	 * @return a deep copy of the list of Drawables. All Drawables will be cloned
	 */
	public ArrayList getDrawableList()
	{
		ArrayList temp = new ArrayList(drawables.size());
		for (int j = 0; j < drawables.size(); j++)
			temp.add(drawables.get(j).clone());
		return temp;
	}
	
	
	
	/**
	 * Sets the list of Drawables to the given list.
	 * @param list the new list of Drawables
	 */
	public void setDrawableList(ArrayList list)
	{
		drawables = list;
	}
}
