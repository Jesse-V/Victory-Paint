
package victorypaint.Modifiers;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.List;
import victorypaint.Drawables.Drawable;

/**
 * A DrawableDeleter gives the user the ability to delete a Drawable simply by clicking on it.
 * @author Jesse Victors, A01485921
 */
public class DrawableDeleter extends DrawableModifier
{
	/**
	 * A reference to DrawingPanel's list of drawables.
	 * The selected Drawable will be removed from this list.
	 */
	private List<Drawable> drawableList;
	
	
	/**
	 * Constructs a DrawableDeleter using the specified Component for user input
	 * and the given list for deleting the selected Drawable.
	 * @param input the Component for user input, must be non-null
	 * @param list  the list of Drawables, must be non-null,
	 * assumed to be identical to the list passed to calculateSelection.
	 */
	public DrawableDeleter(Component input, List<Drawable> list)
	{
		super(input);
		drawableList = list;
	}
	
	
	
	/**
	 * Called when the user lets go of the mouse button whilst inside the input Component.
	 * This method removes the selected Drawable from the list and then calls mouseExited.
	 * @param e the MouseEvent corresponding to the mouse release
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{
		drawableList.remove(getClosestDrawable());
		mouseExited(e);
	}
}
