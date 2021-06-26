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
public class PacmanLogic extends JPanel implements ActionListener {//programming logic of pacman game
    //actionListerner -> game react on pessing button

    //game basic setting
	private Dimension d; //encapsulates the width and height of a game window.
    private final Font font = new Font("Roman", Font.BOLD, 14);//font class(name, stle, size)

    //map basic settinf
    private final int BLOCK_SIZE = 24;//pixel size of a block
    private final int NUM_BLOCKS = 15;//15 blocks in width and height
    private final int SCREEN_SIZE = NUM_BLOCKS * BLOCK_SIZE;//diagonal size of window

    //rules of games
    private final int MAX_GHOSTS = 12;//max number of enemies
    private final int SPEED = 6;//speed of pacman
    private final int validSpeeds[] = {1, 2, 3, 4, 6, 8};
    private final int maxSpeed = 6;//maximum speed of pacman

        
    //game status
    private boolean inGame = false;//determine whether game running
    private boolean die = false;//check if pacman alive

    //game setting
    private int num_ghost = 6;//number of ghots in game
    private int lives, scores;
    private int currentSpeed = 3;

    //directoon, position of ghost
    private int[] dx, dy;//difference in x, difference in y//needed for positions of the ghost
    private int[] ghostX, ghostY, ghostDx, ghostDy, ghostSpeed;//x,y location of speed of ghost//determine the position of the ghost

    //direction, position of ghost
    private int pacmanX, pacmanY, pacmanDx, pacmanDy;//x, y coordinate of pacman. //determine the changes in position
    private int moveX, moveY;//store info of pressed key buttons

    private final short levelData[] = {
        19, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 18, 22,
        17, 16, 16, 16, 16, 24, 16, 16, 16, 16, 16, 16, 16, 16, 20,
        25, 24, 24, 24, 28, 0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
        0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 16, 16, 20,
        19, 18, 18, 18, 18, 18, 16, 16, 16, 16, 24, 24, 24, 24, 20,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,  0, 21,
        17, 16, 16, 16, 16, 16, 16, 16, 16, 20, 0,  0,  0,  0, 21,
        17, 16, 16, 16, 24, 16, 16, 16, 16, 20, 0,  0,  0,  0, 21,
        17, 16, 16, 20, 0, 17, 16, 16, 16, 16, 18, 18, 18, 18, 20,
        17, 24, 24, 28, 0, 25, 24, 24, 16, 16, 16, 16, 16, 16, 20,
        21, 0,  0,  0,  0,  0,  0,  0, 17, 16, 16, 16, 16, 16, 20,
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
    private short[] map;

    //image data
    private Image heart, ghost;//store the image of heart and ghost
    private Image upPac, downPac, leftPac, rightPac;//store the movement animation of pacman

    //take the whole screen data and redraw the whole screen. So the animation will woks
    private Timer timer;

    //controls. How keyboard controlrs pacman
    class TAdapter extends KeyAdapter { //for game controller
        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode(); //collect the info of keyboard button users press

            if (inGame) { //only enter this if else, if the game is running
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
                    moveX = -1;//move to teh left
                    moveY = 0;
                } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                    moveX = 1; //move to the right
                    moveY = 0;
                } else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
                    moveX = 0; //move up
                    moveY = -1;
                } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
                    moveX = 0; //move down
                    moveY = 1;
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

//===================================================================================================================================
//setup the game
    public PacmanLogic() { //constructors which will call akl the other functions
        loadImages();//load image
        initVar(); // initilise all vairables
        addKeyListener(new TAdapter()); //controller function
        setFocusable(true); //for the window
        initGame(); //start the game
    }
    
    
    private void loadImages() {//load images
        downPac = new ImageIcon("./images/down.gif").getImage();
        upPac = new ImageIcon("./images/up.gif").getImage();
        leftPac = new ImageIcon("./images/left.gif").getImage();
        rightPac = new ImageIcon("./images/right.gif").getImage();
        ghost = new ImageIcon("./images/ghost.gif").getImage();
        heart = new ImageIcon("./images/heart.png").getImage();

    }
    
    private void initVar() {
        map = new short[NUM_BLOCKS * NUM_BLOCKS];//create 2d array to store map
        d = new Dimension(400, 400);

        //create a 6-element array for each properties of ghost
        ghostX = new int[MAX_GHOSTS];
        ghostDx = new int[MAX_GHOSTS];
        ghostY = new int[MAX_GHOSTS];
        ghostDy = new int[MAX_GHOSTS];
        ghostSpeed = new int[MAX_GHOSTS];
        dx = new int[4];
        dy = new int[4];
        
        timer = new Timer(40, this);//take care of animation. It determine how often the image is redraw// 40-> 40 milliseconds //this -> apply the timer o this class
        timer.start();//start the timer
    }

    private void initGame() {
    	lives = 3; //number of lives
        scores = 0;
        initLevel(); //draw the game map
        num_ghost = 6;
        currentSpeed = 3;
    }

    private void initLevel() {
        int i;
        for (i = 0; i < NUM_BLOCKS * NUM_BLOCKS; i++) {
            map[i] = levelData[i];
        }//copy the whoel leveldata to the map

        displayLevel();
    }

    private void displayLevel() {//define the position of the ghost
        int dx = 1;
        int random;

        for (int i = 0; i < num_ghost; i++) {
            //start position of ghost
            ghostY[i] = 4 * BLOCK_SIZE; 
            ghostX[i] = 4 * BLOCK_SIZE;
            ghostDy[i] = 0;
            ghostDx[i] = dx;
            dx = -dx;
            random = (int) (Math.random() * (currentSpeed + 1));//create random speed of teh ghost

            if (random > currentSpeed) {
                random = currentSpeed;
            }

            ghostSpeed[i] = validSpeeds[random];
        }

        //start position of pacman
        pacmanX = 7 * BLOCK_SIZE;  
        pacmanY = 11 * BLOCK_SIZE;
        pacmanDx = 0;	//reset direction move
        pacmanDy = 0;
        moveX = 0;		// reset direction controls
        moveY = 0;
        die = false;
    }

//===========================================================================================================================
//run the game
    private void play(Graphics2D g2d) {
        if (die){
            death();
        }else {
            pacmanControl();
            displayPacman(g2d);
            moveGhosts(g2d);
            isWin();
        }
    }

    private void death() {
        lives--;

        if (lives == 0) {
            inGame = false; //game end when there is no more life
        }

        displayLevel(); //if there is still lives left, reset pacman position=
    }

    private void pacmanControl() {
        int pos;
        short ch;

        if (pacmanX % BLOCK_SIZE == 0 && pacmanY % BLOCK_SIZE == 0) {
            pos = pacmanX / BLOCK_SIZE + NUM_BLOCKS * (int) (pacmanY / BLOCK_SIZE); //query the position of the pacman
            ch = map[pos];

            if ((ch & 16) != 0) {//16 is the point pacman can eat
                map[pos] = (short) (ch & 15); //white point dissapear
                scores++; //increase score
            }

            if (moveX != 0 || moveY != 0) {
                //if pacman is not at the border
                if (!((moveX == -1 && moveY == 0 && (ch & 1) != 0) || (moveX == 1 && moveY == 0 && (ch & 4) != 0) || (moveX == 0 && moveY == -1 && (ch & 2) != 0) || (moveX == 0 && moveY == 1 && (ch & 8) != 0))) {
                    pacmanDx = moveX;
                    pacmanDy = moveY;
                }
            }

            // Check for standstill
            if ((pacmanDx == -1 && pacmanDy == 0 && (ch & 1) != 0) || (pacmanDx == 1 && pacmanDy == 0 && (ch & 4) != 0) || (pacmanDx == 0 && pacmanDy == -1 && (ch & 2) != 0) || (pacmanDx == 0 && pacmanDy == 1 && (ch & 8) != 0)) {
                pacmanDx = 0;
                pacmanDy = 0;
            }
        } 

        //change pacman position (moving)
        pacmanX = pacmanX + SPEED * pacmanDx;
        pacmanY = pacmanY + SPEED * pacmanDy;
    }

    private void displayPacman(Graphics2D g2d) { //check pacman animation
        if (moveX == -1) {
            g2d.drawImage(leftPac, pacmanX + 1, pacmanY + 1, this);
        } else if (moveX == 1) {
            g2d.drawImage(rightPac, pacmanX + 1, pacmanY + 1, this);
        } else if (moveY == -1) {
            g2d.drawImage(upPac, pacmanX + 1, pacmanY + 1, this);
        } else {
            g2d.drawImage(downPac, pacmanX + 1, pacmanY + 1, this);
        }
    }

    private void moveGhosts(Graphics2D g2d) {
        int pos;
        int count;

        for (int i = 0; i < num_ghost; i++) {
            if (ghostX[i] % BLOCK_SIZE == 0 && ghostY[i] % BLOCK_SIZE == 0) {
                pos = ghostX[i] / BLOCK_SIZE + NUM_BLOCKS * (int) (ghostY[i] / BLOCK_SIZE);//query location of ghost

                count = 0;

                //check direction only if they hit the wall
                if ((map[pos] & 1) == 0 && ghostDx[i] != 1) {
                    dx[count] = -1;
                    dy[count] = 0;
                    count++;
                }

                if ((map[pos] & 2) == 0 && ghostDy[i] != 1) {
                    dx[count] = 0;
                    dy[count] = -1;
                    count++;
                }

                if ((map[pos] & 4) == 0 && ghostDx[i] != -1) {
                    dx[count] = 1;
                    dy[count] = 0;
                    count++;
                }

                if ((map[pos] & 8) == 0 && ghostDy[i] != -1) {
                    dx[count] = 0;
                    dy[count] = 1;
                    count++;
                }

                if (count == 0) {

                    if ((map[pos] & 15) == 15) {
                        ghostDx[i] = 0;
                        ghostDy[i] = 0;
                    } else {
                        ghostDx[i] = -ghostDx[i];
                        ghostDy[i] = -ghostDy[i];
                    }

                } else {

                    count = (int) (Math.random() * count);

                    if (count > 3) {
                        count = 3;
                    }

                    ghostDx[i] = dx[count];
                    ghostDy[i] = dy[count];
                }

            }

            //move ghost
            ghostX[i] = ghostX[i] + (ghostDx[i] * ghostSpeed[i]);
            ghostY[i] = ghostY[i] + (ghostDy[i] * ghostSpeed[i]);
            //draw ghost in new position
            drawGhost(g2d, ghostX[i] + 1, ghostY[i] + 1);

            //if pacman toucb the ghost
            if (pacmanX > (ghostX[i] - 12) && pacmanX < (ghostX[i] + 12) && pacmanY > (ghostY[i] - 12) && pacmanY < (ghostY[i] + 12) && inGame) {
                die = true;
            }
        }
    }

    private void drawGhost(Graphics2D g2d, int x, int y) {
        g2d.drawImage(ghost, x, y, this);
    }

    private void isWin() { //check whetehr is there any points left
        int i = 0;
        boolean finished = true;

        while (i < NUM_BLOCKS * NUM_BLOCKS && finished) {
            if ((map[i]) != 0) {
                finished = false; //check 1 by 1
            }

            i++;
        }

        if (finished) { //restart the game abd give bonus point
            scores += 50;

            if (num_ghost < MAX_GHOSTS) {
                num_ghost++;
            } 

            if (currentSpeed < maxSpeed) {
                currentSpeed++; //faster speed, so the game is more difficult
            }

            initLevel(); //initilise the gamelevel again
        }
    }

    
//=====================================================================================================================================
//functions which take care of graphical work
    public void paintComponent(Graphics g) {
        super.paintComponent(g);//call its parentclass

        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.black);//background color
        g2d.fillRect(0, 0, d.width, d.height);//paint the window black

        displayMaze(g2d);
        displayScores(g2d);

        if (inGame) {
            play(g2d);
        } else {
            IntroScreen(g2d);
        }

        Toolkit.getDefaultToolkit().sync();
        g2d.dispose();
    }

    private void IntroScreen(Graphics2D g2d) {
        String message = "Press 'SPACE'";
        g2d.setColor(Color.yellow);
        g2d.drawString(message, (SCREEN_SIZE)/4, 150);//define position and draw the string
    }

    private void displayScores(Graphics2D g) {
        g.setFont(font);
        g.setColor(new Color(5, 181, 79));
        String s = "scores: " + scores;
        g.drawString(s, SCREEN_SIZE / 2 + 96, SCREEN_SIZE + 16);//determine position and draw scores

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, i * 28 + 8, SCREEN_SIZE + 1, this);//draw hearts
        }
    }

    private void displayMaze(Graphics2D g2d) {
        short i = 0;
        int x, y;

        for (y = 0; y < SCREEN_SIZE; y += BLOCK_SIZE) {
            for (x = 0; x < SCREEN_SIZE; x += BLOCK_SIZE) {
                g2d.setColor(new Color(0,72,251)); //border color
                g2d.setStroke(new BasicStroke(5)); //thichkness of border
                
                if ((levelData[i] == 0)) { 
                    g2d.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);//color whoel block blue
                }

                if ((map[i] & 1) != 0) { 
                    g2d.drawLine(x, y, x, y + BLOCK_SIZE - 1);//draw border line between 2 points
                }

                if ((map[i] & 2) != 0) { 
                    g2d.drawLine(x, y, x + BLOCK_SIZE - 1, y);
                }

                if ((map[i] & 4) != 0) { 
                    g2d.drawLine(x + BLOCK_SIZE - 1, y, x + BLOCK_SIZE - 1, y + BLOCK_SIZE - 1);
                }

                if ((map[i] & 8) != 0) { 
                    g2d.drawLine(x, y + BLOCK_SIZE - 1, x + BLOCK_SIZE - 1,y + BLOCK_SIZE - 1);
                }

                if ((map[i] & 16) != 0) { //white dot
                    g2d.setColor(new Color(255,255,255));
                    g2d.fillOval(x + 10, y + 10, 6, 6);
                    //draw oval with height and width
                }

                i++;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
