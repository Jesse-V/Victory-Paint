
package victorypaint;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.*;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.Timer;

/**
 * This class will make everyone's favorite assistant appear on the screen!
 * @author Jesse Victors, A01485921
 */
public class OnscreenAssistant extends PopupScreen implements ActionListener, WindowListener
{
	private Timer timer = new Timer(15000, this);
	private ImageIcon image;
	private static final int MARGIN = 50;
	
	
	/**
	 * Sets up the onscreen assistant but does not display it on the screen just yet.
	 * Starts the timer which will eventually show it
	 */
	public OnscreenAssistant()
	{
		super("That's right");
		
		readImage();
		
		if (image != null)
			setSize(image.getIconWidth(), image.getIconHeight());
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screenSize.width - getWidth() - MARGIN, screenSize.height - getHeight() - MARGIN);
		addWindowListener(this);
		timer.start();
	}
	
	
	
	/**
	 * Attempts to read the "assistant.png" image from the project directory or from
	 * inside the .jar. If found, it will initialize image.
	 */
	private void readImage()
	{
		try
		{
			URL url = getClass().getResource("/resources/assistant.png");
			if (url == null) //test if found inside .jar
				image = new ImageIcon("resources/assistant.png");
			else
				image = new ImageIcon(url);
		}
		catch (Exception e)
		{
			timer.stop();
		}
	}
	
	
	/**
	 * Draws the image of the assistant on a white background.
	 * @param g the Graphics object to draw on
	 */
	@Override
	public void paint(Graphics g)
	{
		if (image == null)
			return;
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.drawImage(image.getImage(), 0, 0, this);
	}
	
	
	
	/**
	 * @return 100% opacity, so it's not transparent
	 */
	@Override
	public float getDesiredOpacityLevel()
	{
		return 1f;
	}

	
	
	/**
	 * Called by the Timer. Sets the window to be true, which makes it appear.
	 * @param e the ActionEvent corresponding to the event, can be null
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		setVisible(true);
	}
	
	
	
	/**
	 * Called when the user clicks the mouse while over this component.
	 * This method hides the window and stops the timer.
	 * @param e the MouseEvent corresponding to the click
	 */
	@Override
	public void mouseClicked(MouseEvent e)
	{
		removeWindowListener(this);
		setVisible(false);
		timer.stop();
	}

	
	
	@Override
	public void windowOpened(WindowEvent e)
	{ }

	@Override
	public void windowClosing(WindowEvent e)
	{ }

	@Override
	public void windowClosed(WindowEvent e)
	{ }

	@Override
	public void windowIconified(WindowEvent e)
	{
		windowDeactivated(e);
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{ }

	@Override
	public void windowActivated(WindowEvent e)
	{ }

	
	
	/**
	 * Called when the user tries to unfocus the window.
	 * This makes the window visible again, bringing it back on top of all other windows.
	 * @param e the WindowEvent corresponding to the deactivation
	 */
	@Override
	public void windowDeactivated(WindowEvent e)
	{
		setVisible(true);
	}
}
