import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;

import com.croftsoft.core.animation.ComponentPainter;
import com.croftsoft.core.animation.sprite.TextSprite;
import com.croftsoft.core.animation.updater.NullComponentUpdater;

/*
 * Created on May 8, 2005
 *
 */

/**
 * @author 05pme
 * 
 * Class that specifies User display elements in EphSpace Game
 */
public class UserInfoDisplay implements ComponentPainter, ShipListener {

	// message display constants
	private static final double INFO_MSG_SECONDS = 4;
	private static final int INFO_MSG_COUNTDOWN = (int)(INFO_MSG_SECONDS * Constants.FRAME_RATE);
	private static final int MSG_INDENT_LEFT = 15;
	private static final int MSG_INDENT_TOP = 25;
	private static final String USER_DIED_MSG = "You died... try again!";
	private static final String USER_POWERUP_HEAD_MSG = "Got ";
	private static final String USER_POWERUP_TAIL_MSG = " power-up!! Sweet!";
	private static final String GAME_OVER_MSG = "GAME OVER";
	private static final String TRYAGAIN_MSG = "... press spacebar to try again";
	private static final String RESTART_MSG = "Up and at 'em!  Good luck.";
	
	// public, for the GameWorld to use
	public static final String START_MSG = "Welcome to EphSpace.  Good luck.";
	
	// shields display constants
	private static final Color SHIELDS_FRAME_COLOR = Color.WHITE;
	private static final Color SHIELDS_NORMAL_FILL_COLOR = new Color(0, 130, 0);
	private static final Color SHIELDS_DANGER_FILL_COLOR = Color.RED;
	private static final double SHIELDS_DANGER_PERCENTAGE = .20;
	private static final int SHIELDS_HEIGHT = 20;
	private static final int SHIELDS_MARGIN = 10;
	private static final int SHIELDS_WIDTH = 110;
	private static final int SHIELDS_RESIZE_DOWN_INC = 1;
	private static final int SHIELDS_RESIZE_UP_INC = 2;
	
	// text display constants
//	private static final int DISPLAY_WIDTH = 120;
	private static final int DISPLAY_INDENT = 18;
	private static final int DISPLAY_HEIGHT = 50;
	private static final int MSG_GAMEOVER_INDENT = 120;
	private static final int MSG_GAMEOVER_HEIGHT = 30;
	
	// height of a text item
	private static final int ITEM_HEIGHT = 20;
	
	// text, position constants
	private static final String LIVES_LABEL = "Lives: ";
	private static final Color LIVES_COLOR = Color.CYAN;
	private static final String SCORE_LABEL = "Score: ";
	private static final Color SCORE_COLOR = Color.CYAN;
	private static final String LEVEL_LABEL = "Level: ";
	private static final String SHIELDS_LABEL = "Shields: ";
	private static final Color MSG_COLOR = new Color (255, 239, 62);
	private static final Color MSG_GAMEOVER_COLOR = Color.RED;
	
	// font constants
	private static final String FONT = "Arial";
	private static final int FONT_STYLE = Font.PLAIN;
	private static final int FONT_SIZE = 16;
	private static final int FONT_GAMEOVER_SIZE = 24;
	
	private GameWorld world;
	
	// hit points display state
	private Rectangle hpRect;
	private final Rectangle hpFrame;
	private Color shieldsFillColor = SHIELDS_NORMAL_FILL_COLOR; 
	private int targetHPWidth;
	private int shieldsIncrement = 0;
	
	// message text display state
	private int msgCountdown;

	// text instance variables
	private TextSprite livesText;
	private TextSprite scoreText;
	private TextSprite levelText;
	private TextSprite shieldsText;
	private TextSprite msgText;
	private TextSprite gameOverText;

	// keeps track of "game over" state
	private int gameRestartCountdown;
	private boolean gameOver = false;
	
	/**
	 * Creates a new user info display.
	 * @param world the game world
	 */
	public UserInfoDisplay(GameWorld world, JComponent component) {
		this.world = world;
		
		livesText = new TextSprite (DISPLAY_INDENT,
				 component.getHeight() - DISPLAY_HEIGHT, 0,0,0,NullComponentUpdater.INSTANCE,
				 LIVES_LABEL + world.getLives(), 
				 new Font(FONT, FONT_STYLE, FONT_SIZE),
				 LIVES_COLOR
				 );

		scoreText = new TextSprite (DISPLAY_INDENT,
				 component.getHeight() - DISPLAY_HEIGHT + ITEM_HEIGHT, 0,0,0,NullComponentUpdater.INSTANCE,
				 SCORE_LABEL + world.getScore(), 
				 new Font(FONT, FONT_STYLE, FONT_SIZE),
				 LIVES_COLOR
				 );
		
		levelText = new TextSprite (DISPLAY_INDENT,
				 component.getHeight() - DISPLAY_HEIGHT + 2*ITEM_HEIGHT, 0,0,0,NullComponentUpdater.INSTANCE,
				 LEVEL_LABEL + world.getLevel(), 
				 new Font(FONT, FONT_STYLE, FONT_SIZE),
				 LIVES_COLOR
				 );
		
		hpRect = new Rectangle(component.getWidth() / 2 - (SHIELDS_WIDTH / 2),
				   component.getHeight() - SHIELDS_HEIGHT - SHIELDS_MARGIN,
				   SHIELDS_WIDTH,
				   SHIELDS_HEIGHT
				  );

		hpFrame = new Rectangle(component.getWidth() / 2 - (SHIELDS_WIDTH / 2),
				   component.getHeight() - SHIELDS_HEIGHT - SHIELDS_MARGIN,
				   SHIELDS_WIDTH,
				   SHIELDS_HEIGHT
				  );
		targetHPWidth = (int)hpFrame.getWidth();

		shieldsText = new TextSprite (hpFrame.getX() + targetHPWidth + DISPLAY_INDENT,
				 component.getHeight() - DISPLAY_HEIGHT + 2*ITEM_HEIGHT, 0,0,0,NullComponentUpdater.INSTANCE,
				 SHIELDS_LABEL + world.getLevel(), 
				 new Font(FONT, FONT_STYLE, FONT_SIZE),
				 SHIELDS_NORMAL_FILL_COLOR
				 );
		
		msgText = new TextSprite (MSG_INDENT_LEFT,
				 MSG_INDENT_TOP, 0,0,0,NullComponentUpdater.INSTANCE,
				 "", 
				 new Font(FONT, FONT_STYLE, FONT_SIZE),
				 MSG_COLOR
				 );

		gameOverText = new TextSprite (DISPLAY_INDENT + MSG_GAMEOVER_INDENT,
				 component.getHeight() - (MSG_GAMEOVER_HEIGHT / 2), 0,0,0,NullComponentUpdater.INSTANCE,
				 "", 
				 new Font(FONT, FONT_STYLE, FONT_GAMEOVER_SIZE),
				 MSG_GAMEOVER_COLOR
				 );

	}
	
	/**
	 * Shows a message on the screen for a few seconds.
	 * @param s the message
	 */
	public void showMessage(String s) {
		msgText.setText(s);
		msgCountdown = INFO_MSG_COUNTDOWN;
	}
	
	/**
	 * A listener method in listener pattern, called by user's Ship.
	 * @param userShip the user's ship
	 */
	public void update(Ship userShip) {
		assert world != null && userShip.isUserShip(); // since we're only listening to user ship, for now
		livesText.setText(LIVES_LABEL + world.getLives());
		scoreText.setText(SCORE_LABEL + world.getScore());
		levelText.setText(LEVEL_LABEL + world.getLevel());
		shieldsText.setText(SHIELDS_LABEL + userShip.getHitPoints()+"/"+userShip.getMaxHP());
				
		PowerUp pow = userShip.getLastPowerUp();
		if(pow != null)
			showMessage(USER_POWERUP_HEAD_MSG + pow.toString().toUpperCase() + USER_POWERUP_TAIL_MSG);
		updateShields(userShip.getHitPoints(),userShip.getMaxHP());
	}
	
	/**
	 * Updates shields display.
	 * @param hp new value of hit points
	 */
	private void updateShields(int hp, int max) {
		double percentFull = (double)hp / (double)max;
		
		if(percentFull <= SHIELDS_DANGER_PERCENTAGE){
			shieldsFillColor = SHIELDS_DANGER_FILL_COLOR;
			shieldsText.setColor(SHIELDS_DANGER_FILL_COLOR);
		}
		else{
			shieldsFillColor = SHIELDS_NORMAL_FILL_COLOR;
			shieldsText.setColor(SHIELDS_NORMAL_FILL_COLOR);
		}
		targetHPWidth = (int)(hpFrame.getWidth() * percentFull);

		if (hp <= 0) showMessage(USER_DIED_MSG);
	}
	
    /**
     * Paints user info display text and shields indicator. 
     *@param component main window of game
     *@param graphics "canvas" of main window
     */
	public void paint(JComponent component, Graphics2D graphics) {
		//draw gameover sequnce and update display
		if(gameOver) {
			if(--gameRestartCountdown == 0) {
				gameOverText.setText(GAME_OVER_MSG + TRYAGAIN_MSG);
				world.enableRestart();
			}
			gameOverText.paint(component, graphics);
			livesText.paint(component, graphics);
			scoreText.paint(component, graphics);
			levelText.paint(component, graphics);
			shieldsText.paint(component, graphics);
			
			return;
		}
		
		//update regualr display
		livesText.paint(component, graphics);
		scoreText.paint(component, graphics);
		levelText.paint(component, graphics);
		shieldsText.paint(component, graphics);
		
		
		if ((int)hpRect.getWidth() == targetHPWidth) {
			shieldsIncrement = 0;
		}
		else if((int)hpRect.getWidth() > targetHPWidth) {
			shieldsIncrement = -SHIELDS_RESIZE_DOWN_INC;
		}
		else {
			shieldsIncrement = SHIELDS_RESIZE_UP_INC;
		}
		
		if(shieldsIncrement != 0) {
			hpRect.setSize((int)(hpRect.getWidth() + shieldsIncrement), 
					   (int)hpRect.getHeight());
		}

		// draw shields
		graphics.setColor(shieldsFillColor);
		graphics.fillRect((int)hpRect.getX(), (int)hpRect.getY(), (int)hpRect.getWidth(), (int)hpRect.getHeight());
		graphics.setColor(SHIELDS_FRAME_COLOR);
		graphics.drawRect((int)hpFrame.getX(), (int)hpFrame.getY(), (int)hpFrame.getWidth(), (int)hpFrame.getHeight());
		
		// draw message text, if a message is active
		if(msgCountdown-- > 0)
			msgText.paint(component, graphics);
	}
	
	/**
	 * Triggers the "game over" sequence.
	 */
	public void gameOver() {
		msgCountdown = 0;
		gameOver = true;
		gameRestartCountdown = Constants.GAME_RESTART_COUNTDOWN;
		gameOverText.setText(GAME_OVER_MSG);
	}
	
	/**
	 * Triggers the restart game message and state.
	 */
	public void restart() {
		gameOver = false;
		showMessage(RESTART_MSG);
	}
}