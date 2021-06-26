import javax.swing.JFrame;

public class SnakeGameFrame extends JFrame{

	SnakeGameFrame(){
		SnakeGamePanel gamePanel = new SnakeGamePanel();	
        this.add(gamePanel); //add GameFrame() as component

		//this.add(new GamePanel()); another way for the previous 2 line of code 

		this.setTitle("Snake"); //Sets the title for this frame 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // If a window has this set as the close operation and is closed in an applet, a SecurityException may be thrown
		this.setResizable(false); // set to false so that user cannot re-size the frame
		this.pack();// packs the components within the window based on the component’s preferred sizes.
		this.setVisible(true); //display window application
		this.setLocationRelativeTo(null);
		//If the component is null, or the GraphicsConfiguration associated with this component is null, the window is placed in the center of the screen
	}
}


