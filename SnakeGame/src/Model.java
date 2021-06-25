// package src;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

//swing is a GUI widget toolkit
//to develop GUI or window-based applications in java
public class Model extends JPanel implements ActionListener {//programming logic of pacman game
    //actionListerner -> game react on pessing button

	private Dimension d; //encapsulates the width and height of a game window.
    private final Font smallFont = new Font("Arial", Font.BOLD, 14);//font class(name, stle, size)
    private boolean inGame = false;//determine whether game running
    private boolean dying = false;//check if pacman alive

    private final int BLOCK_SIZE = 24;//pixel size of a block
    private final int N_BLOCKS = 15;//15 blocks in width and height
    private final int SCREEN_SIZE = N_BLOCKS * BLOCK_SIZE;//diagonal size of window
    private final int MAX_GHOSTS = 12;//max number of enemies
    private final int PACMAN_SPEED = 6;//speed of pacman

    private int N_GHOSTS = 6;//number of ghots in game
    private int lives, score;
    private int[] dx, dy;//difference in x, difference in y//needed for positions of the ghost
    private int[] ghost_x, ghost_y, ghost_dx, ghost_dy, ghostSpeed;//x,y location of speed of ghost//determine the position of the ghost

    private Image heart, ghost;//store the image of heart and ghost
    private Image up, down, left, right;//store the movement animation of pacman

    private int pacman_x, pacman_y, pacmand_x, pacmand_y;//x, y coordinate of pacman. //determine the changes in position
    private int req_dx, req_dy;//store info of pressed key buttons

    private final short levelData[] = {
    	19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
        0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
        19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0,  0,  0,   0, 21,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
        17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
        21, 0,  0,  0,  0,  0,  0,   0, 17, 16, 16, 16, 16, 16, 20,
        17, 18, 18, 22, 0, 19, 18, 18, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 26, 24, 24, 24, 24, 24, 24, 24, 24, 24, 28
    };//the layout of map //225 number of values
    /*
    0 = blue
    1 = left border
    2 = top border
    4 = right border
    8 = bottom border
    16 = white gots
    example: 1 + 2 + 16 = 19 (left bprder, top border and white dots)
    */

    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;//maximum speed of pacman

    private int currentSpeed = 3;
    private short[] screenData;//take the whole screen data and redraw the whole screen. So the animation will woks
    private Timer timer;

    public Model() { //constructors which will call akl the other functions

        loadImages();//load image
        initVariables(); // initilise all vairables
        addKeyListener(new TAdapter()); //controller function
        setFocusable(true); //for the window
        initGame(); //start the game
    }
    
    
    private void loadImages() {//load images
    	down = new ImageIcon("./images/down.gif").getImage();
    	up = new ImageIcon("./images/up.gif").getImage();
    	left = new ImageIcon("./images/left.gif").getImage();
    	right = new ImageIcon("./images/right.gif").getImage();
        ghost = new ImageIcon("./images/ghost.gif").getImage();
        heart = new ImageIcon("./images/heart.png").getImage();

    }
       private void initVariables() {

        screenData = new short[N_BLOCKS * N_BLOCKS];//create 2d array to store screenData
        d = new Dimension(400, 400);

        //create a 6-element array for each properties of ghost
        ghost_x = new int[MAX_GHOSTS];
        ghost_dx = new int[MAX_GHOSTS];
        ghost_y = new int[MAX_GHOSTS];
        ghost_dy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];
        
        timer = new Timer(40, this);//take care of animation. It determine how often the image is redraw// 40-> 40 milliseconds //this -> apply the timer o this class
        timer.start();//start the timer
    }

    private void playGame(Graphics2D g2d) {

        if (dying) {

            death();

        } else {

            movePacman();
            drawPacman(g2d);
            moveGhosts(g2d);
            checkMaze();
        }
    }

    private void showIntroScreen(Graphics2D g2d) {
        String start = "Press SPACE to start";
        g2d.setColor(Color.yellow);
        g2d.drawString(start, (SCREEN_SIZE)/4, 150);//define position and draw the string
    }

    private void drawScore(Graphics2D g) {
        g.setFont(smallFont);
        g.setColor(new Color(5, 181, 79));
        String s = "Score: " + score;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);//determine position and draw scores

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);//draw hearts
        }
    }

    private void checkMaze() { //check whetehr is there any points left

        int i = 0;
        boolean finished = true;

        while (i < N_BLOCKS * N_BLOCKS && finished) {

            if ((screenData[i]) != 0) {
                finished = false; //check 1 by 1
            }

            i++;
        }

        if (finished) { //restart the game abd give bonus point

            score += 50;

            if (N_GHOSTS < MAX_GHOSTS) {
                N_GHOSTS++;
            } 

            if (currentSpeed < maxSpeed) {
                currentSpeed++; //faster speed, so the game is more difficult
            }

            initLevel(); //initilise the gamelevel again
        }
    }

    private void death() {

    	lives--;

        if (lives == 0) {
            inGame = false; //game end when there is no more life
        }

        continueLevel(); //if there is still lives left, reset pacman position=
    }

    private void moveGhosts(Graphics2D g2d) {

        int pos;
        int count;

        for (int i = 0; i < N_GHOSTS; i++) {
            if (ghost_x[i] % BLOCK_SIZE == 0 && ghost_y[i] % BLOCK_SIZE == 0) {
                pos = ghost_x[i] / BLOCK_SIZE + N_BLOCKS * (int) (ghost_y[i] / BLOCK_SIZE);//query location of ghost

                count = 0;

                //check direction only if they hit the wall
                if ((screenData[pos] & 1) == 0 && ghost_dx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 2) == 0 && ghost_dy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((screenData[pos] & 4) == 0 && ghost_dx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((screenData[pos] & 8) == 0 && ghost_dy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((screenData[pos] & 15) == 15) {
                        ghost_dx[i] = 0;
                        ghost_dy[i] = 0;
                    } else {
                        ghost_dx[i] = -ghost_dx[i];
                        ghost_dy[i] = -ghost_dy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghost_dx[i] = dx[count];
                    ghost_dy[i] = dy[count];
                }

            }

            //move ghost
            ghost_x[i] = ghost_x[i] + (ghost_dx[i] * ghostSpeed[i]);
            ghost_y[i] = ghost_y[i] + (ghost_dy[i] * ghostSpeed[i]);
            //draw ghost in new position
            drawGhost(g2d, ghost_x[i] + 1, ghost_y[i] + 1);

            //if pacman toucb the ghost
            if (pacman_x > (ghost_x[i] - 12) && pacman_x < (ghost_x[i] + 12) && pacman_y > (ghost_y[i] - 12) && pacman_y < (ghost_y[i] + 12) && inGame) {
                dying = true;
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y) {
    	g2d.drawImage(ghost, x, y, this);
    }

    private void movePacman() {

        int pos;
        short ch;

        if (pacman_x % BLOCK_SIZE == 0 && pacman_y % BLOCK_SIZE == 0) {
            pos = pacman_x / BLOCK_SIZE + N_BLOCKS * (int) (pacman_y / BLOCK_SIZE); //query the position of the pacman
            ch = screenData[pos];

            if ((ch & 16) != 0) {//16 is the point pacman can eat
                screenData[pos] = (short) (ch & 15); //white point dissapear
                score++; //increase score
            }

            if (req_dx != 0 || req_dy != 0) {
                //if pacman is not at the border
                if (!((req_dx == -1 && req_dy == 0 && (ch & 1) != 0)
                        || (req_dx == 1 && req_dy == 0 && (ch & 4) != 0)
                        || (req_dx == 0 && req_dy == -1 && (ch & 2) != 0)
                        || (req_dx == 0 && req_dy == 1 && (ch & 8) != 0))) {
                    pacmand_x = req_dx;
                    pacmand_y = req_dy;
                }
            }

            // Check for standstill
            if ((pacmand_x == -1 && pacmand_y == 0 && (ch & 1) != 0)
                    || (pacmand_x == 1 && pacmand_y == 0 && (ch & 4) != 0)
                    || (pacmand_x == 0 && pacmand_y == -1 && (ch & 2) != 0)
                    || (pacmand_x == 0 && pacmand_y == 1 && (ch & 8) != 0)) {
                pacmand_x = 0;
                pacmand_y = 0;
            }
        } 

        //change pacman position (moving)
        pacman_x = pacman_x + PACMAN_SPEED * pacmand_x;
        pacman_y = pacman_y + PACMAN_SPEED * pacmand_y;
    }

    private void drawPacman(Graphics2D g2d) { //check pacman animation
        if (req_dx == -1) {
        	g2d.drawImage(left, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dx == 1) {
        	g2d.drawImage(right, pacman_x + 1, pacman_y + 1, this);
        } else if (req_dy == -1) {
        	g2d.drawImage(up, pacman_x + 1, pacman_y + 1, this);
        } else {
        	g2d.drawImage(down, pacman_x + 1, pacman_y + 1, this);
        }
    }

    private void drawMaze(Graphics2D g2d) {

        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {

                g2d.setColor(new Color(0,72,251)); //border color
                g2d.setStroke(new BasicStroke(5)); //thichkness of border
                
                if ((levelData[i] == 0)) { 
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);//color whoel block blue
                }

                if ((screenData[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);//draw border line between 2 points
                }

                if ((screenData[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((screenData[i] & 4) != 0) { 
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 8) != 0) { 
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,y + BLOCK_SIZE - 1);
                }

                if ((screenData[i] & 16) != 0) { //white dot
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                    //draw oval with height and width
                }

                i++;
            }
        }
    }

    private void initGame() {

    	lives = 3; //number of lives
        score = 0;
        initLevel(); //draw the game map
        N_GHOSTS = 6;
        currentSpeed = 3;
    }

    private void initLevel() {

        int i;
        for (i = 0; i < N_BLOCKS * N_BLOCKS; i++) {
            screenData[i] = levelData[i];
        }//copy the whoel leveldata to the screenData

        continueLevel();
    }

    private void continueLevel() {//define the position of the ghost

    	int dx = 1;
        int random;

        for (int i = 0; i < N_GHOSTS; i++) {
            //start position of ghost
            ghost_y[i] = 4 * BLOCK_SIZE; 
            ghost_x[i] = 4 * BLOCK_SIZE;
            ghost_dy[i] = 0;
            ghost_dx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));//create random speed of teh ghost

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random];
        }

        //start position of pacman
        pacman_x = 7 * BLOCK_SIZE;  
        pacman_y = 11 * BLOCK_SIZE;
        pacmand_x = 0;	//reset direction move
        pacmand_y = 0;
        req_dx = 0;		// reset direction controls
        req_dy = 0;
        dying = false;
    }

 
    public void paintComponent(Graphics g) {
        super.paintComponent(g);//call its parentclass

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);//background color
        g2d.fillRect(0, 0, d.width, d.height);//paint the window black

        drawMaze(g2d);
        drawScore(g2d);

        if (inGame) {
            playGame(g2d);
        } else {
            showIntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }


    //controls
    class TAdapter extends KeyAdapter { //for game controller

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode(); //collect the info of keyboard button users press

            if (inGame) { //only enter this if else, if the game is running
                if (key == KeyEvent.VK_LEFT) {
                    req_dx = -1;//move to teh left
                    req_dy = 0;
                } else if (key == KeyEvent.VK_RIGHT) {
                    req_dx = 1; //move to the right
                    req_dy = 0;
                } else if (key == KeyEvent.VK_UP) {
                    req_dx = 0; //move up
                    req_dy = -1;
                } else if (key == KeyEvent.VK_DOWN) {
                    req_dx = 0; //move down
                    req_dy = 1;
                } else if (key == KeyEvent.VK_ESCAPE && timer.isRunning()) {
                    inGame = false; //pause/quit the game when the game is running
                } 
            } else {
                if (key == KeyEvent.VK_SPACE) {
                    inGame = true;
                    initGame();//start game
                }
            }
        }
}

	
    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
		
	}
