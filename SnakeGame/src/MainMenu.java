import java.awt.Container;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridLayout;



import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;




public class MainMenu {

        JFrame window;
        Container con; 
        JPanel titleNamePanel;
        JLabel titleNameLabel;
        JPanel ChoiceButtonPanel,SnakeButtonPanel;
        Font titleFont = new Font("Times New Roman", Font.PLAIN , 90 );
        Font NormalFont = new Font("Times New Roman", Font.PLAIN , 30 );

        JButton PacmanButton, SnakeButton;

        ChoiceHandler chHandler = new ChoiceHandler();
        public static int state =0 ;
    public static void main(String[] args){
        new MainMenu();
    }
    
    public MainMenu(){


        window =  new JFrame();
        window.setSize(800,600);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.getContentPane().setBackground(Color.black);
        window.setLayout(null);
        con = window.getContentPane();
        

        titleNamePanel = new JPanel();
        titleNamePanel.setBounds(100,100,600,150);
        titleNamePanel.setBackground(Color.black);
        titleNameLabel = new JLabel("Game Simulator");
        titleNameLabel.setForeground(Color.white);
        titleNameLabel.setFont(titleFont);
        titleNamePanel.add(titleNameLabel);

        ChoiceButtonPanel = new JPanel();
        ChoiceButtonPanel.setBounds(250,350,300,150);
        ChoiceButtonPanel.setBackground(Color.black);
        ChoiceButtonPanel.setLayout(new GridLayout(1,2));

        
        PacmanButton = new JButton();   
        PacmanButton.setBackground(Color.black);
        PacmanButton.setText("Pacman");
        PacmanButton.setForeground(Color.white);
        PacmanButton.setFocusPainted(false); // diable the box inside the Jbutton
        PacmanButton.setFont(NormalFont);
        PacmanButton.addActionListener(chHandler);
        PacmanButton.setActionCommand("c1");
        ChoiceButtonPanel.add(PacmanButton);

        SnakeButton = new JButton();   
        SnakeButton.setBackground(Color.black);
        SnakeButton.setText("Snake");
        SnakeButton.setForeground(Color.white);
        SnakeButton.setFocusPainted(false); // diable the box inside the Jbutton
        SnakeButton.setFont(NormalFont);
        SnakeButton.addActionListener(chHandler);
        PacmanButton.setActionCommand("c2");

        ChoiceButtonPanel.add(SnakeButton);

        con.add(titleNamePanel);
        con.add(ChoiceButtonPanel); 

        window.setVisible(true); 
        

    }
    public MainMenu(int a ){
        state = a; 

    }
    public int getState(){
        return state;
    }

    public JFrame getObject(){
        return window;
    }

}
