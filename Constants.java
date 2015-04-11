/*
 * Created on Apr 26, 2005
 *
 */

/**
 * @author Ashok Pillai, Adrian Martinez, Phil Enock
 * This class stores constants which fit in one of two categories:
 *   1. Constants that need to be accessed by multiple classes
 *   2. Constants that we want to organize in one code location, as opposed to
 *      spread across many classes
 * 
 * Other constants may be stored in the classes in which they are used, such as
 * UserInfoDisplay, because the constants are more appropriately confined to
 * that class.
 */
import java.awt.Color;

public final class Constants {

	public static final boolean DEBUG_MODE = false;
	
	
	//frames per second
    static final double  FRAME_RATE = 30.0;
	    
    public static final int NUM_COMPUTER_SHIPS = 3;
    
    //ship movement constants
    private static final double DESIRED_SHIP_VELOCITY = 120.0; // pixels/s
    public static final double DEFAULT_SHIP_VELOCITY = DESIRED_SHIP_VELOCITY / FRAME_RATE; //pixels/frame
	public static final double MAX_ALLOWED_SHIP_SPEED = 
								3*DEFAULT_SHIP_VELOCITY;
	public static final double MIN_ALLOWED_SHIP_VELOCITY = DEFAULT_SHIP_VELOCITY/4;
	private static final double DESIRED_FULL_ACCELERATION = 2; //seconds
	public static final double SHIP_ACCELERATION = MAX_ALLOWED_SHIP_SPEED / (DESIRED_FULL_ACCELERATION * FRAME_RATE);//pixels/frame*frame
	public static final double SHIP_DECELERATION=SHIP_ACCELERATION*3;
	
	//ship hit points and damage constants
	public static final int DEFAULT_HIT_POINTS = 200;
	public static final int HP_INCREMENT = DEFAULT_HIT_POINTS/4;
	public static final int SHIP_COLLISION_DAMAGE = (DEFAULT_HIT_POINTS/10)*-1;
	public static final int ASTEROID_DAMAGE = (DEFAULT_HIT_POINTS/6)*-1;
	public static final int BULLET_DAMAGE = (DEFAULT_HIT_POINTS/5)*-1;
	
	//weapon and bullet constants
	private static final double DESIRED_FIRING_RATE = 6.0; //bullets/s
	public static final long DEFAULT_FIRING_RATE = Math.round(FRAME_RATE/DESIRED_FIRING_RATE)+1; //frames/bullet
	public static final long MAX_FIRING_RATE = Math.round(FRAME_RATE/(5*DESIRED_FIRING_RATE))+1;
	
	public static final double DEFAULT_BULLET_SPEED = 1.5*MAX_ALLOWED_SHIP_SPEED;
	private static final double DESIRED_BULLET_LIFESPAN = 0.6; //seconds
	public static final long BULLET_LIFESPAN = Math.round(DESIRED_BULLET_LIFESPAN * FRAME_RATE); //# frames
	public static final double BULLET_RADIUS = 3.0; //pixels
	public static final Color HUMAN_BULLET_COLOR = Color.ORANGE;
	public static final Color COMPUTER_BULLET_COLOR = Color.RED;
	
	public static final int MAX_NUM_WEAPON_UPGRADES = 6;
	public static final double BULLET_SPEED_UPGRADE_RATIO = 1.2;
	public static final double BULLET_LIFESPAN_UPGRADE_RATIO = 1.1;
	public static final double SCATTER_DELTA=Math.PI/6;

	//asteroid constants
	public static final int NUM_ASTEROIDS = 5;
    public static final double ASTEROID_RADIUS = 16;
	private static final double DESIRED_ASTEROID_BASE_VELOCITY = 45.0; // pixels/s
    public static final double ASTEROID_BASE_SPEED = DESIRED_ASTEROID_BASE_VELOCITY / FRAME_RATE; //pixels/frame
	
    //rotation constants
	private static final double DESIRED_SHIP_ROTATION = Math.PI; //radians/s
	public static final double SHIP_ROTATION = DESIRED_SHIP_ROTATION/FRAME_RATE; //radians/frams
	public static final double ASTEROID_ROTATION = SHIP_ROTATION/2;
	
	// image file names and related constants
    public static final String ASTEROID_IMAGE_FILE = "images/asteroid.gif";
    public static final String USER_SHIP_IMAGE_FILE = "images/spaceship-sq.png";
    public static final double USER_RADIUS_RATIO = .75; 
    public static final String COMPUTER_SHIP_IMAGE_FILE = "images/spaceship-round.png";
    public static final double COMPUTER_RADIUS_RATIO = 1; 
    public static final String POWERUP_IMAGE_FILE = "images/david2sm.png";
    
    // image constants: must be set up when image is loaded (to detect size)
    private static int USER_SHIP_HEIGHT=0, USER_SHIP_WIDTH=0;
    private static int COMPUTER_SHIP_HEIGHT=0, COMPUTER_SHIP_WIDTH=0;
    
    // user ship/data constants
    public static final int STARTING_LIVES = 3;
	public static final int USER_START_X = 150;
	public static final int USER_START_Y = 150;
    
    // score constants
    public static final int ASTEROID_POINTS = 5;
    public static final int SHIP_KILL_POINTS = 50;
    public static final int HIT_SCORE_DELTA = 5;
    public static final int LEVEL_UP_DELTA = 1000;
    
    // powerup constants
    public static final double POWUP_GEN_PROB_ASTEROID = 0.15; //probablitly a powerup is generated after an asteroid is destroyed
    public static final double POWUP_GEN_PROB_SHIP = 0.35; //probability a powerup is generated after a ship is killed

    // game constants
    private static final double GAME_RESTART_SECS = 5; 
    public static final int GAME_RESTART_COUNTDOWN = (int)(GAME_RESTART_SECS * FRAME_RATE); 
    
    /**
     * Sets ship height and width constants. To be called by GameWorld, since it loads 
     * the image file.
     * @param height ship height constant.
     * @param width ship width constant
     */
    public static void setUserShipBounds(int height, int width) {
    	USER_SHIP_HEIGHT = height;
    	USER_SHIP_WIDTH = width;
    }
    
    /**
     * Returns ship height constant.
     * @return ship height constant.
     */
    public static int getUserShipHeight() {
    	return USER_SHIP_HEIGHT;
    }

    /**
     * Returns ship width constant.
     * @return ship width constant.
     */
    public static int getUserShipWidth() {
    	return USER_SHIP_WIDTH;
    }
    /**
     * Sets ship height and width constants. To be called by GameWorld, since it loads 
     * the image file.
     * @param height ship height constant.
     * @param width ship width constant
     */
    public static void setComputerShipBounds(int height, int width) {
    	COMPUTER_SHIP_HEIGHT = height;
    	COMPUTER_SHIP_WIDTH = width;
    }
    
    /**
     * Returns ship height constant.
     * @return ship height constant.
     */
    public static int getComputerShipHeight() {
    	return COMPUTER_SHIP_HEIGHT;
    }
    /**
     * Returns ship width constant.
     * @return ship width constant.
     */
    public static int getComputerShipWidth() {
    	return COMPUTER_SHIP_WIDTH;
    }
}