// package src;
import javax.swing.*;
import java.awt.BorderLayout
;
public class GameStimulator{
    static private int in; // to get a value for the purpose of assigning function

	public static void main(String[] args) {
       
        //JOptionPane 
        String[] options = {"pacman", "snake"};// Option for user to click 
        ImageIcon dab = new ImageIcon("images/pacmanAndSnake.jpg"); // get the  image 
        JLabel icon = new JLabel(dab); // assign image into a JLabel datatype for later use in adding it into the JPanel
        JLabel text = new JLabel("                             Wanna play a game?"); // text that will shown together with the image 

        JPanel panel = new JPanel(); // create a jpanel 
        panel.setLayout(new BorderLayout()); // set the layout 
        panel.add(icon,BorderLayout.CENTER); // to make the image center 
        panel.add(text,BorderLayout.SOUTH);  // to make the text located at the south which is under the image

        in = JOptionPane.showOptionDialog(null, panel , "Game Stimulator", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null , options, options[0]);
        // ShowOption dialog, to make the icon not avialable in the dialog , we have to use the JOptionPane.PLAIN_MESSAGE
        // in variable will get the the index of the option which is an array
        // panel is the JPanel Object that will display an image and a text under it so dab variable is not need
        // to fill in at before the option and option[1]

        if(in == 0){
            new Pacman(); // create Pacman Object and call the constuctor 
        }else if(in == 1){
            new SnakeGame();// create Pacman Object and call the constuctor 
        }else if(in == -1){ // in = -1 when the user click the cross button at the top right 
            System.exit(0); // Terminate the program if the user click the cross button without this the program
                            // wont terminate eventhough the dialog been close, and there will be
                            // no dialog show when running the program after click cross button 
                            
        }
		
	}
}
