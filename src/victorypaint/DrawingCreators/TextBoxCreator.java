
package victorypaint.DrawingCreators;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import victorypaint.Drawables.TextBox;

/**
 * A TextBoxCreator constructs a TextBox based on user input from a specified Component.
 * A TextBox is first created when the user clicks inside the Component, and the
 * TextBox is then place at that location. As the user types, text is appended to the TextBox,
 * and the TextBox is finalized when the user clicks again in the input Component.
 * @author Jesse Victors, A01485921
 */
public class TextBoxCreator extends DrawableCreator implements KeyListener
{
	/**
	 * The TextBox being constructed
	 */
	private TextBox textBox = new TextBox(new Point());


	/**
	 * Creates a TextBoxCreator using the specified Component for user input
	 * @param input the input Component, must be non-null.
	 */
	public TextBoxCreator(Component input)
	{
		super(input);
	}



	/**
	 * Returns the TextBox currently being constructed.
	 * @return the TextBox
	 */
	@Override
	public TextBox getDrawable()
	{
		return textBox;
	}



	/**
	 * Called when the user clicks the mouse inside the input Component.
	 * If a TextBox has not yet been created, it will initialize one at the mouse's location,
	 * otherwise it finalizes the TextBox.
	 * @param e the MouseEvent corresponding to the mouse click,
	 * must be non-null only if its not in the process of being created
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (isCreating())
		{
			getInputComponent().removeKeyListener(this);
			finish();
			textBox.setGhosting(false);
		}
		else
		{
			creating = true;
			textBox.setGhosting(true);
			textBox.setLocation(e.getPoint());
			getInputComponent().addKeyListener(this);
			getInputComponent().requestFocusInWindow();
		}
	}



	/**
	 * Called when the user clicks a key while the input Component has focus.
	 * This method will append the typed character to the TextBox.
	 * @param e the MouseEvent corresponding to the key typed, must be non-null
	 */
	@Override
	public void keyTyped(KeyEvent e)
	{
		if (e.getKeyChar() == KeyEvent.VK_ENTER)
			mouseClicked(null); //seems to be safe to pass null...
		textBox.processKey(e.getKeyChar());
	}



	/**
	 * Called when the user presses a key while the input Component has focus.
	 * This method does nothing.
	 * @param e the MouseEvent corresponding to the key press
	 */
	@Override
	public void keyPressed(KeyEvent e)
	{ }



	/**
	 * Called when the user releases a key while the input Component has focus.
	 * This method does nothing.
	 * @param e the MouseEvent corresponding to the key release
	 */
	@Override
	public void keyReleased(KeyEvent e)
	{ }
}
