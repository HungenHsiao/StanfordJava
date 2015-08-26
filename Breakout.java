/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {
	public static void main(String[] args) {
		new Breakout().start(args);
	}

/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH =
	  (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
/** Margins for the score label */
	private static final int SCORE_MARGIN_X = 15;
	private static final int SCORE_MARGIN_Y = 20;
	private static final int GAMEEND_LABEL_MARGIN = 77;
	private static final int TURN_LABEL_MARGIN = 107;
	
	private static final int INSTRUC_LABEL_MARGIN = 103;
	
	
	//private instance variable
	//to keep track of the paddle
	private GRect paddle;
	//last position of the mouse
	private double lastX; 
	//tracks the ball
	private GOval ball;
	//velocity of the ball in x and y coordinates
	private double vx, vy;
	//tracks brick
	private GRect brick;
	
	private GLabel instruc;
	
	private int score;
	
	private int turn;
	
	private GLabel scoreDisplay;

	private double ballSpeed;
	
	private boolean ballInPlay;
	
/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		while (true) {
			displayMsg();
			waitForClick();
			removeAll();
			//sets up the game with bricks, paddle and ball
			turn = 1;
			score = 0;
			ballSpeed = 2.5;
			setup();
			for (int i = 0; i < NTURNS; i++) {
				remove(ball);
				ballInPlay = true;
				ball();
				
				GLabel turnDisplay = new GLabel("TURN #" + turn, TURN_LABEL_MARGIN, getHeight()/2);
				turnDisplay.setColor(Color.RED);
				turnDisplay.setFont("Impact-Bold-50");
				add(turnDisplay);
				waitForClick();
				remove(turnDisplay);
				
				if (ballInPlay) {
					moveBall();
				}
				turn ++;
			}
			GLabel gameover = new GLabel("GAME OVER", GAMEEND_LABEL_MARGIN, getHeight()/2);
			gameover.setFont("Impact-Bold-50");
			gameover.setColor(Color.RED);
			println(gameover.getWidth());
			add(gameover);
			waitForClick();
			remove(gameover);
		}
	}
	
	private void displayMsg() {

		instruc = new GLabel("Click to Start!");
		
		double x = INSTRUC_LABEL_MARGIN;
		double y = getHeight()/2;
		
		instruc.setColor(Color.RED);
		instruc.setLocation(x, y);
		instruc.setFont("Impact-Bold-30");
		add(instruc);
		println(instruc.getWidth() + "," + getWidth());
	}
	
	private void setup() {
		setupBricks();
		setupPaddle();
		ball();
		scoreboard();
	}

	private void setupBricks() {
		for (int i = 0; i < NBRICK_ROWS; i++) {
			for (int j = 0; j < NBRICKS_PER_ROW; j++) {
				//Creates new bricks
				GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				brick.setLocation((getWidth()- APPLICATION_WIDTH)/2 + BRICK_SEP/2 + ((BRICK_SEP+BRICK_WIDTH)*j), (getHeight() - APPLICATION_HEIGHT)/2 + BRICK_Y_OFFSET + (BRICK_HEIGHT+BRICK_SEP)*i);
				add(brick);
				brick.setFilled(true);
				
				//color the bricks according to their row number
				switch (i) {
					case 0: 
						brick.setFillColor(Color.RED);
						brick.setColor(Color.RED);
						break;
					case 1:
						brick.setFillColor(Color.RED);
						brick.setColor(Color.RED);
						break;
					case 2:
						brick.setFillColor(Color.ORANGE);
						brick.setColor(Color.ORANGE);
						break;
					case 3:
						brick.setFillColor(Color.ORANGE);
						brick.setColor(Color.ORANGE);
						break;
					case 4:
						brick.setFillColor(Color.YELLOW);
						brick.setColor(Color.YELLOW);
						break;
					case 5:
						brick.setFillColor(Color.YELLOW);
						brick.setColor(Color.YELLOW);
						break;
					case 6:
						brick.setFillColor(Color.GREEN);
						brick.setColor(Color.GREEN);
						break;
					case 7:
						brick.setFillColor(Color.GREEN);
						brick.setColor(Color.GREEN);
						break;
					case 8:
						brick.setFillColor(Color.CYAN);
						brick.setColor(Color.CYAN);
						break;
					case 9:
						brick.setFillColor(Color.CYAN);
						brick.setColor(Color.CYAN);
						break;						
					default:
						brick.setFillColor(Color.RED);
						break;
				}
			}
		}
	}
	
	//set up the paddle
	private void setupPaddle() {
		//centers the paddle's mid point to the center of the screen
		double x = (getWidth()-PADDLE_WIDTH)/2;
		//the paddle is always the same height from the bottom of the window
		double y = getHeight()-PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		
		paddle = new GRect(x, y, PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		add(paddle);
		addMouseListeners();
	}
		
	public void mouseMoved(MouseEvent e) {
		lastX = e.getX();
		if ((e.getX() < (getWidth()-PADDLE_WIDTH/2))&& (e.getX() > PADDLE_WIDTH/2 )) {
			paddle.setLocation(lastX - PADDLE_WIDTH/2, (getHeight()-PADDLE_Y_OFFSET - PADDLE_HEIGHT));
		}

	}
	
	private void ball() {
		//Diameter of the ball
		int d = BALL_RADIUS*2;
		//initial x position of the ball
		int x = getWidth()/2 - BALL_RADIUS;
		//initial y position of the ball
		int y = getHeight()/2 - BALL_RADIUS;
		
		ball = new GOval(x, y, d, d);
		ball.setFilled(true);
		add(ball);		
	}
	
	private void scoreboard() {
		scoreDisplay = new GLabel("Score: " + score);
		scoreDisplay.setColor(Color.BLUE);
		scoreDisplay.setFont("Impact-Bold-16");
		scoreDisplay.setLocation(SCORE_MARGIN_X, SCORE_MARGIN_Y);
		add(scoreDisplay);
	}
	
	private void moveBall() {
		//y velocity of ball 
		vy = ballSpeed;
		//x velocity of ball
//		vx += ballSpeed;
		
		vx = rgen.nextDouble(1.5, ballSpeed);
		if (rgen.nextBoolean(0.5)) vx = -vx;
		//moving the ball
		while (ball.getX() < getHeight()) {
			ball.move(vx, vy);
			if (score == 100 || score == 200) {
				waitForClick();
				setupBricks();
			}
			checkBounce();
			if (ballInPlay == false) {
				break;
			}
			pause(10);
		}
	}
	
	//check if there's collision with the walls
	private void checkBounce() {
		//check the bottom/top of the window, if ball reaches top,
		//then it bounces back but x direction is still the same
				
		if ((ball.getY() < 0)) {
			vy = -vy;
			ball.move(vx, vy);
		}
		
		//checks the left/right of the window, if ball reaches the left/right,
		//then it bounces back but y direction is still the same
		if ((ball.getX() < 0) || (ball.getX() > getWidth()-(BALL_RADIUS*2))) {
			vx = -vx;
			ball.move(vx, vy);
		}
		
		//checks the bottom of the window, if ball goes beyond the bottom,
		//then the turn ends
		if (ball.getY() > (getHeight()-BALL_RADIUS*2)) {
			ballInPlay = false;
		}
		
		GObject collider = getCollidingObject();
		
		if (collider == paddle) {
			vy = -vy;
			ball.move(vx, vy);
		} else if (collider != null) {
			remove(collider);
			vy = -vy;
			updateScore();

			ball.move(vx, vy);
		}
	}
	
	private void updateScore() {
		remove(scoreDisplay);
		score++;
		scoreboard();
		if ((score % 10) == 0) {
			ballSpeed += 0.5;
			vx = ballSpeed;
			vy = ballSpeed;
		}
	}
	
	private GObject getCollidingObject() {

		//ball changes y direction when collides with anything at the top or bottom
		boolean ballBottomLeft = getElementAt(ball.getX(), ball.getY()+2*BALL_RADIUS) != null;
		boolean ballBottomRight = getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY()+2*BALL_RADIUS) != null; 
		boolean ballTopLeft = getElementAt(ball.getX(), ball.getY()) != null;
		boolean ballTopRight = getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY()) != null;
		
		if (ballBottomLeft) {
			return getElementAt(ball.getX(), ball.getY()+2*BALL_RADIUS);
		} 
		else if (ballBottomRight) {
			return getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY()+2*BALL_RADIUS);
		}
		else if (ballTopLeft) {
			return getElementAt(ball.getX(), ball.getY());
		}
		else if (ballTopRight) {
			return getElementAt(ball.getX()+2*BALL_RADIUS, ball.getY());
		}
		else {
			return null;
		}
	
	}
	
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
}
