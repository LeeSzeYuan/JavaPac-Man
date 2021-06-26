import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class SnakeGamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 320;//1300
	static final int SCREEN_HEIGHT = 420;//750
	static final int UNIT_SIZE = 20; // the size of the item 
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE); // the number of item that can be fit in the panel
	static final int DELAY = 175; // this will affect the speed of the snake 
	final int x[] = new int[GAME_UNITS]; // x,y hold xand y coordinate of the body fo the snake 
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 3;
	int foodEaten;
	int foodX;
	int foodY;
	char direction = 'R';// the snake will begin with moving to the right 
	boolean running = false;
	Timer timer;
	Random random;
	
	SnakeGamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black); 
		this.setFocusable(true); // Used to activate or deactivate the focus event (component of the graphical user interface that is
								 // selected to receive the input) of the view, both in the tactile / mouse mode, and in the keyboard (cursor) mode.
		this.addKeyListener(new MyKeyAdapter()); // get input 
		startGame();
	}
	public void startGame() {
		newFood();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics gc) {
		super.paintComponent(gc);
		draw(gc);
	}
	public void draw(Graphics g) {
		
		if(running) {// if the program running
			g.setColor(Color.red);
			g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE); // set the property of the food eg. its size and shape
		
			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) { // it is for the head of snake 
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else { // other part of the snake 
					g.setColor(new Color(45,180,0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE); // fill the snake in rect and set i property 
				}			
			}
			//score display 
			g.setColor(Color.white);
			g.setFont( new Font("Times New Roman",Font.BOLD, 20));
			FontMetrics m = getFontMetrics(g.getFont()); 
			g.drawString("Score: "+foodEaten, (SCREEN_WIDTH - m.stringWidth("Score: "+foodEaten))/2, g.getFont().getSize()); // display the score 
		}
		else {
			gameOver(g);
		}
		
	}
	public void newFood(){
		foodX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE; // we * with unit size to make the food place evenly in the panel 
		foodY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE; // we still cast int ,just to make it does not cause any error 
	}
	public void move(){ //to move the snake 
		for(int i = bodyParts;i>0;i--) { 
			x[i] = x[i-1]; // the x coordinate of the index before it will be assign to it 
			y[i] = y[i-1];
		}
		
		switch(direction) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		
	}
	public void checkFood() {
		if((x[0] == foodX) && (y[0] == foodY)) { // when the head is at the postion of the food 
			bodyParts++; // increase bodypart by 1 
			foodEaten++;
			newFood(); // call to create new food 
		}
	}
	public void checkCollisions() {
		//checks whether head collides with body
		for(int i = bodyParts;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				running = false;
			}
		}
		//check whether head touches left border
		if(x[0] < 0) {
			running = false;
		}
		//check whether head touches right border
		if(x[0] > SCREEN_WIDTH) {
			running = false;
		}
		//check whether head touches top border
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT) {
			running = false;
		}
		
		if(!running) {
			timer.stop();// stop the timer 
		}
	}
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.white);
		g.setFont( new Font("Times New Roman",Font.BOLD, 20));
		FontMetrics m1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+ foodEaten, (SCREEN_WIDTH - m1.stringWidth("Score: "+ foodEaten))/2, g.getFont().getSize());
		// we use m1.stringWidth("Score: "+ foodEaten))/2  to make put the score at the center of the program 
		
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Times New Roman",Font.BOLD, 50));
		FontMetrics m2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - m2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent a) {
		
		if(running) {
			move();
			checkFood();
			checkCollisions();
		}
		repaint();// if the game is not running, call repaint method to repaint the panel
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent keyInput) {
			switch(keyInput.getKeyCode()) { // getKeyCode returns the integer keyCode associated with the key in this event.
			case KeyEvent.VK_LEFT:   // when the user press left arrow key
									if(direction != 'R') {  // for making sure the snake wont turn 180 degree and end the game 
										direction = 'L';
									}
									break;
			case KeyEvent.VK_RIGHT:  // when the user press right arrow key
									if(direction != 'L') {
										direction = 'R';
									}
									break;
			case KeyEvent.VK_UP: // when the user press up arrow key
									if(direction != 'D') {
										direction = 'U';
									}
									break;
			case KeyEvent.VK_DOWN: // when the user press down arrow key
									if(direction != 'U') {
										direction = 'D';
									}
									break;

            case KeyEvent.VK_A:  // when the user press A key
									if(direction != 'R') {
										direction = 'L';
									}
									break;

            case KeyEvent.VK_D: // when the user press D key
									if(direction != 'L') {
										direction = 'R';
									}
									break;

            case KeyEvent.VK_W: // when the user press W key
									if(direction != 'D') {
										direction = 'U';
									}
									break;      

            case KeyEvent.VK_S: // when the user press S key
									if(direction != 'U') {
										direction = 'D';
									}
									break;        
			
			case KeyEvent.VK_ESCAPE : if(running){
				running = false;
				timer.stop();
			  }
			  break;
			case KeyEvent.VK_SPACE : if(!running){
				running = true;
				startGame();//start game
			  }
			  break;
			}
		}
	}
}