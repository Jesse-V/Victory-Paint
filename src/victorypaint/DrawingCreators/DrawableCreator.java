
package victorypaint.DrawingCreators;

import java.awt.Component;
import victorypaint.Drawables.Drawable;
import victorypaint.MouseInputAcceptor;

/**
 * Abstract class for constructing a Drawable based on user input from a specified Component.
 * It is up to the subclasses to decide the specifics of the user's input and which Drawable will be created.
 * @author Jesse Victors, A01485921
 */
public abstract class DrawableCreator extends MouseInputAcceptor
{
	/**
	 * Holds whether this DrawableCreator is currently constructing a Drawable or not.
	 */
	protected boolean creating = false;


	/**
	 * Constructs a DrawableCreator using the specified Component for user input.
	 * This adds a MouseListener and a MouseMotionListener to the Component,
	 * so that user input from a mouse can be detected.
	 * @param input the input Component, must be non-null.
	 */
	public DrawableCreator(Component input)
	{
		super(input);
	}



	/**
	 * Returns whether a Drawable is currently being constructed or not.
	 * Each subclasses of DrawableCreator defines exactly when a Drawable is being created,
	 * but in general it occurs from the moment that user input is recognized
	 * and the Drawable is changed from a non-default state,
	 * until the moment when the user decides the Drawable is
	 * sufficient and no more user input is being recognized.
	 * @return whether or not a Drawable is currently being constructed
	 */
	public final boolean isCreating()
	{
		return creating;
	}




	/**
	 * Returns the Drawable currently being constructed.
	 * If isCreating() is false and no new user input has been accepted,
	 * this Drawable could contain default values.
	 * @return the Drawable currently being constructed
	 */
	public abstract Drawable getDrawable();



	/**
	 * Finalizes this DrawableCreator and removes user input listeners,
	 * sets the "creating" and "accepting user input" properties to false.
	 */
	@Override
	protected final void finish()
	{
		super.finish();
		creating = false;
	}
}
