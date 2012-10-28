
package victorypaint.Modifiers;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import victorypaint.Drawables.Drawable;

/**
 * A DrawableColorChanger allows the user to change the color of a drawn Drawable
 * simply by clicking near it. The Drawable's default (non-ghosting) color will then change
 * to the Color given in the constructor.
 * @author Jesse Victors, A01485921
 */
public class DrawableColorChanger extends DrawableModifier
{
	/**
	 * Holds the user's desired color to apply to the Drawable's default color
	 */
	private Color desiredColor;
	
	
	/**
	 * Constructs a DrawableColorChanger using the specified Component for user input,
	 * and the specified Color for application to a selected Drawable.
	 * @param input the input Component, must be non-null
	 * @param desiredColor the Color to change the selected Drawable to, must be non-null
	 */
	public DrawableColorChanger(Component input, Color desiredColor)
	{
		super(input);
		this.desiredColor = desiredColor;
	}
	
	
	/**
	 * Called when the user's mouse is pressed down whilst inside the input Component.
	 * If the mouse's location is close enough to capture a Drawable, this method
	 * changes the default (non-ghosting) color of that Drawable to the desired color.
	 * @param e the MouseEvent corresponding to the mouse press, must be non-null
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{
		super.mousePressed(e);
		Drawable drawable = getClosestDrawable();
		if (drawable != null && captured)
			drawable.setDefaultColor(desiredColor);
	}
}
