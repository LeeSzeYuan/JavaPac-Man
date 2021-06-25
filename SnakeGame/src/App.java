import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;//  imported to override start method.
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas; // Canvas class  creates an image that can be drawn on using a set of graphics commands provided by a GraphicsContext  
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.awt.BorderLayout;

public class App extends Application {

	// variable
	static int speed = 5;
	static int foodcolor = 0;
	static int width = 20;
	static int height = 20;
	static int foodX = 0;
	static int foodY = 0;
	static int cornersize = 25;
	static List<Corner> snake = new ArrayList<>(); // Corners are squares of which the snake consists
	static Dir direction = Dir.left; 
	static boolean gameOver = false;
	static Random rand = new Random();


	public enum Dir {// the enum 'Dir' contains the directions for the controls
		left, right, up, down
	}

	public static class Corner {
		int x;
		int y;

		public Corner(int x, int y) {
			this.x = x;
			this.y = y;
		}

	}

	// start method is the starting point of constructing a JavaFX application
	public void start(Stage primaryStage) { //pass in Root Stage object represents the primary window of your JavaFX application. You can create new Stage
		try {
			int in;

			// JOptionPane 
			String[] options = {"pacman", "snake"};
			ImageIcon dab = new ImageIcon("src/images/pacmanAndSnake.jpg");
			JLabel icon = new JLabel(dab);
			JLabel text = new JLabel("                             Wanna play a game?");

			JPanel panel = new JPanel();
			panel.setLayout(new BorderLayout());
			panel.add(icon,BorderLayout.CENTER);
			panel.add(text,BorderLayout.SOUTH);

			in = JOptionPane.showOptionDialog(null, panel , "Game Stimulator", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null , options, options[0]);
			
			if(in == 0){
				new Pacman();
			}else if(in == 1){

				newFood();
				// We create a VBox and a Canvas as background. 
				VBox root = new VBox();//Instantiate VBox layout class
				Canvas c = new Canvas(width * cornersize, height * cornersize); // create canvas object with specified width and height
				GraphicsContext gc = c.getGraphicsContext2D(); //GraphicContext to paint the snake , this returns the GraphicsContext associated with this Canvas.
				root.getChildren().add(c); // Adding nodes to the VBox layout object

				new AnimationTimer() { // animationtime is flipbook 
					long lastTick = 0;

					public void handle(long now) {
						// this method is going to be called in every frame while the AnimationTimer is active.
						// now - The timestamp of the current frame given in nanoseconds. This value will be the same for all AnimationTimers called during one frame.
						if (lastTick == 0) { 
							lastTick = now;
							tick(gc);
							return;
						}

						if (now - lastTick > 1000000000 / speed) {
							lastTick = now;
							tick(gc);
						}// 1 ticks = we get a new frame every 'speed' second.
						// more speed = more frames = faster snake
					}

				}.start();

				Scene scene = new Scene(root, width * cornersize, height * cornersize); // create the window of the application according to the size 

				// control
				scene.addEventFilter(KeyEvent.KEY_PRESSED, key -> { // set the keyboard control to W,A,S,D OR arrow direction or numpad 8,4,2,6
					if ( (key.getCode() == KeyCode.W) || (key.getCode() == KeyCode.UP) || (key.getCode() == KeyCode.NUMPAD8) ) { //KeyCode will returnt the constant of the key, KeyCode is an enum class 
						direction = Dir.up;
					}
					if ( (key.getCode() == KeyCode.A) || (key.getCode() == KeyCode.LEFT) || (key.getCode() == KeyCode.NUMPAD4) ) {
						direction = Dir.left;
					}
					if ( (key.getCode() == KeyCode.S) || (key.getCode() == KeyCode.DOWN) || (key.getCode() == KeyCode.NUMPAD2) ) {
						direction = Dir.down;
					}
					if ( (key.getCode() == KeyCode.D) || (key.getCode() == KeyCode.RIGHT) || (key.getCode() == KeyCode.NUMPAD6) ){
						direction = Dir.right;
					}

				});

				// add start snake parts , add the snake to 3 parts(Corners)at the beginning
				snake.add(new Corner(width / 2, height / 2)); 
				snake.add(new Corner(width / 2, height / 2));
				snake.add(new Corner(width / 2, height / 2));

				primaryStage.setScene(scene);//set a JavaFX Scene object on the Stage
				primaryStage.setTitle("SNAKE GAME"); // The title will display in the title bar of the Stage Window
				primaryStage.show(); // show() makes the Stage visible and the exits the show() method immediately
			}else if(in == -1){
				System.exit(0);
			}
		} catch (Exception e) {
			e.printStackTrace(); //  handle exceptions and errors,Java’s throwable class, 
								 //  prints the throwable along with other details like the line number and class name where the exception occurred.
		}
	}

	// tick
	public static void tick(GraphicsContext gc) {
		if (gameOver) { // Fill 'Game Over' in RED in size 50 on position 100,250 
			gc.setFill(Color.RED);
			gc.setFont(new Font("", 50));
			gc.fillText("GAME OVER", 100, 250); //fillText(String text,double x-axis ,double y-axis )
			return;
		}

		for (int i = snake.size() - 1; i >= 1; i--) {
			snake.get(i).x = snake.get(i - 1).x;
			snake.get(i).y = snake.get(i - 1).y;
		}

		switch (direction) { // choose direction of the snake 
		case up:                    
			snake.get(0).y--; //Game over if the snake touches a border   
			if (snake.get(0).y < 0) {
				gameOver = true;
			}
			break;
		case down:
			snake.get(0).y++;
			if (snake.get(0).y > height) {
				gameOver = true;
			}
			break;
		case left:
			snake.get(0).x--;
			if (snake.get(0).x < 0) {
				gameOver = true;
			}
			break;
		case right:
			snake.get(0).x++;
			if (snake.get(0).x > width) {
				gameOver = true;
			}
			break;

		}

		// eat food // let the snake grow 
		if (foodX == snake.get(0).x && foodY == snake.get(0).y) {
			snake.add(new Corner(-1, -1));
			newFood();
		}

		// self destroy // game fover if the snake hit the snake 
		for (int i = 1; i < snake.size(); i++) {
			if (snake.get(0).x == snake.get(i).x && snake.get(0).y == snake.get(i).y) {
				gameOver = true;
			}
		}

		// fill
		// background, fill the background balck 
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width * cornersize, height * cornersize);

		// score, fill the score in whtie with standard font on position 10,3
		gc.setFill(Color.WHITE);
		gc.setFont(new Font("", 30));
		gc.fillText("Score: " + (speed - 6), 10, 30);

		// random foodcolor
		Color cc = Color.WHITE; // create a color variable and assign it with white color constant

		switch (foodcolor) {
		case 0:
			cc = Color.PURPLE; // assign the purple constant to cc
			break;
		case 1:
			cc = Color.LIGHTBLUE;
			break;
		case 2:
			cc = Color.YELLOW;
			break;
		case 3:
			cc = Color.PINK;
			break;
		case 4:
			cc = Color.ORANGE;
			break;
		}
		gc.setFill(cc);
		gc.fillOval(foodX * cornersize, foodY * cornersize, cornersize, cornersize);
		// fillOval(double x, double y, double w, double h)
		// x - the X coordinate of the upper left bound of the oval.
		// y - the Y coordinate of the upper left bound of the oval.
		// w - the width at the center of the oval.
		// h - the height at the center of the oval.

		// snake
		for (Corner c : snake) {
			gc.setFill(Color.LIGHTGREEN);
			gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 1, cornersize - 1);
			gc.setFill(Color.GREEN);
			gc.fillRect(c.x * cornersize, c.y * cornersize, cornersize - 2, cornersize - 2);

		}

	}

	// food
	public static void newFood() {
		start: while (true) {
			foodX = rand.nextInt(width);// We place a new food on random location foodX * foodY on the cancas (if there is no snake)
			foodY = rand.nextInt(height);

			for (Corner c : snake) {
				if (c.x == foodX && c.y == foodY) {
					continue start;
				}
			}
			foodcolor = rand.nextInt(5); // Choose a new color and increase the speed 
			speed++;
			break;

		}
	}

	public static void main(String[] args) { 
						//  args is the argument passed by the console. 
		launch(args);   //  launch() method is a static method located in the Application class.
		                //launch() method  detect from which class it is called and launches the JavaFX runtime and  JavaFX application.	
        
	}
}
