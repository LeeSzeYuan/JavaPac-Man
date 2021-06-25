// package src;
import javax.swing.*;

public class run{
    static private int in;

	public static void main(String[] args) {
        String start = "Wanna play a game?";
        String[] options = {"pacman", "snake"};
        in = JOptionPane.showOptionDialog(null, start, "Methods to draw a line graph.", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        
        if(in == 0){
            new Pacman();
        }else if(in == 1){
            System.out.print("Snake");
        }
		
	}
}
