
package victorypaint;

import java.awt.Component;
import java.awt.event.MouseAdapter;

/**
 * Main superclass for adding user input listeners to a Component.
 * It is up to subclasses to override appropriate mouse functions
 * and perform specific action.
 * @author Jesse Victors, A01485921
 */
public class MouseInputAcceptor extends MouseAdapter
{
	/**
	 * The Component used for user input.
	 */
	protected Component inputComponent;

	
	/**
	 * Holds whether this MouseInputAcceptor is accepting user action or not.
	 */
	protected boolean acceptingUserInput = true;


	/**
	 * Constructs a MouseInputAcceptor using the specified Component for user input.
	 * This adds a MouseListener and a MouseMotionListener to the Component,
	 * so that user input from a mouse can be detected.
	 * @param input the input Component, must be non-null.
	 */
	public MouseInputAcceptor(Component input)
	{
		inputComponent = input;
		inputComponent.addMouseListener(this);
		inputComponent.addMouseMotionListener(this);
	}



	/**
	 * Returns whether user input is being accepted or not.
	 * This occurs from a moment this class is first created
	 * until finish() is called, which removes the mouse and mouse motion listeners.
	 * @return whether or not this MouseInputAcceptor will accept user input
	 */
	public final boolean acceptingUserInput()
	{
		return acceptingUserInput;
	}



	/**
	 * Returns the Component which user input is accepted from
	 * @return the input Component
	 */
	public final Component getInputComponent()
	{
		return inputComponent;
	}



	/**
	 * Removes the user input listeners,
	 * namely the MouseListeners and MouseMotionListeners,
	 * and sets the "accepting user input" property to false.
	 */
	protected void finish()
	{
		inputComponent.removeMouseListener(this);
		inputComponent.removeMouseMotionListener(this);
		acceptingUserInput = false;
	}
}
