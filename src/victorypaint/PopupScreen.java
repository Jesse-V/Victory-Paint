
package victorypaint;

import com.sun.awt.AWTUtilities;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JFrame;

/**
 * Superclass for all classes which display a semitransparent undecorated window
 * with rounded edges. Since it lacks an "X" button, users close a PopupScreen by
 * clicking on it.
 * @author Jesse Victors, A01485921
 */
public abstract class PopupScreen extends JFrame implements MouseListener
{
	/**
	 * Creates and displays a PopupScreen using the given title
	 * @param title The title for the PopupScreen. Will only show in the Taskbar
	 */
	public PopupScreen(String title)
	{
		super(title);
		
		setUndecorated(true);
		addMouseListener(this);
	}
	
	
	
	/**
	 * Attempts to make the window semitransparent and round its edges.
	 * Will do nothing if the OS does not support these operations.
	 */
	public final void makeWindowSpecial()
	{
		shapeWindow();
		setOpacityLevel(getDesiredOpacityLevel());
	}
	
	
	
	/**
	 * Attempts to round the edges of the window if supported.
	 */
	public void shapeWindow()
	{
		Shape shape = new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 120, 120);
		if (AWTUtilities.isWindowShapingSupported())
			setShape(shape);
	}
	
	
	
	/**
	 * Sets the opacity level of the window to the given value.
	 * @param level specifies the opacity level, between 0 (transparent) to 1 (completely opaque)
	 */
	public void setOpacityLevel(float level)
	{
		if (AWTUtilities.isTranslucencySupported(AWTUtilities.Translucency.TRANSLUCENT))
			setOpacity(level);
	}
	
	
	
	/**
	 * Called when the user clicks the mouse while over this component.
	 * This method hides the window.
	 * @param e the MouseEvent corresponding to the click
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		setVisible(false);
	}

	
	
	/**
	 * @return the subclass's desired opacity level
	 */
	public abstract float getDesiredOpacityLevel();
	
	
	
	/**
	 * Called when the user presses the mouse while over this component.
	 * This method does nothing.
	 * @param e the MouseEvent corresponding to the mouse press.
	 */
	@Override
	public void mousePressed(MouseEvent e)
	{ }

	
	
	/**
	 * Called when the user releases the mouse while over this component.
	 * This method does nothing.
	 * @param e the MouseEvent corresponding to the mouse release.
	 */
	@Override
	public void mouseReleased(MouseEvent e)
	{ }

	
	
	/**
	 * Called when the user's mouse enters the component.
	 * This method does nothing.
	 * @param e the MouseEvent corresponding to the mouse enter.
	 */
	@Override
	public void mouseEntered(MouseEvent e)
	{ }

	
	
	/**
	 * Called when the user's mouse exits the component.
	 * This method does nothing.
	 * @param e the MouseEvent corresponding to the mouse exit.
	 */
	@Override
	public void mouseExited(MouseEvent e)
	{ }
}
