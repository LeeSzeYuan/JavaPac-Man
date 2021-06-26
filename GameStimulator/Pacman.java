// package src;

import javax.swing.*;


public class Pacman extends JFrame{//launch the game

	public Pacman() {
		add(new Model());//add Model() as component
		this.setVisible(true);//display window application
		this.setTitle("Pacman");//Sets the title for this frame
		this.setSize(380,420);//size of the component
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);//Sets the operation that will happen by default when the user initiates a "close" on this frame 
		// If a window has this set as the close operation and is closed in an applet, a SecurityException may be thrown
		this.setLocationRelativeTo(null);
		//If the component is null, or the GraphicsConfiguration associated with this component is null, the window is placed in the center of the screen
	}

}
