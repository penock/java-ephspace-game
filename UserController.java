import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/*
 * Created on May 2, 2005
 *
 */

/**
 * @author Adrian Martinez, Ashok Pillai, Phil Enock
 * This class handles user input on a ship. It can fire, turn, and accelerate
 */
public class UserController implements KeyListener {

	/** the operator for the ship being controlled */
	protected ShipOperator operator;
	
	/** flags determining the current action of the ship */
	protected boolean accelerating, turningRight, turningLeft;

	/** the game world */
	private GameWorld gameWorld;

	/** whether a restart from an ended game is enabled */
	private boolean restartEnabled = false;
	
	/**
	 * Constructs the userController and starts the keylistening
	 * @param operator The operator for the ship to be controlled
	 * @param component the Component the ship will be drawn onto (which can take a keyListener)
	 */
	public UserController(ShipOperator operator, Component component){
		assert operator!=null && component!=null;
		this.operator=operator;
		
		component.addKeyListener(this);
		
	}
	
	/**
	 * Interprets the space bar as a command to fire
	 */
	public void keyTyped(KeyEvent arg0) {
		if(arg0.getKeyChar()==' '){
			if(restartEnabled) {
				restartEnabled = false;
				gameWorld.restart();
			}
			operator.fire();
		}

	}

	/**
	 * Interprets left, right, and up arrow keys
	 * @param arg0 the key being pressed
	 */
	public void keyPressed(KeyEvent arg0) {
		//up arrow pressed => acceleration
		if(arg0.getKeyCode()==KeyEvent.VK_UP){
			operator.accelerate();

		}
		
		//left arrow pressed => stop turning right or turn left
		if(arg0.getKeyCode()==KeyEvent.VK_LEFT){
			if(turningRight){
				operator.stopTurningRight();
			}else{
				operator.turnLeft();
			}
			
			turningLeft=true;
		}
		
		//right arrow pressed => stop turning left or turn right
		if(arg0.getKeyCode()==KeyEvent.VK_RIGHT){
			if(turningLeft){
				operator.stopTurningLeft();
			}
			else{
				operator.turnRight();
			}
			turningRight=true;
		}
		
		//down arrow pressed => deceleration
		if(arg0.getKeyCode()==KeyEvent.VK_DOWN){
			operator.decelerate();
		}

	}

	/**
	 * Tells the shipOperator to stop accelerating or turning
	 * @param arg0 the key being released
	 */
	public void keyReleased(KeyEvent arg0) {
		//up arrow released => stop accelerating
		if(arg0.getKeyCode()==KeyEvent.VK_UP){
			operator.stopAccelerating();
		}
		
		//left arrow released => allow right turn or stop turning left
		if(arg0.getKeyCode()==KeyEvent.VK_LEFT){
			if(turningRight){
				operator.turnRight();
				
			}else{
			operator.stopTurningLeft();
			}
			turningLeft=false;
		}
		
		//right arrow released => allow left turn or stop turning right
		if(arg0.getKeyCode()==KeyEvent.VK_RIGHT){
			if(turningLeft){
				operator.turnLeft();
				
			}else{
				operator.stopTurningRight();
			}
			turningRight=false;
		}
		
		//down arrow released => stop decelerating
		if(arg0.getKeyCode()==KeyEvent.VK_DOWN){
			operator.stopDecelerating();
		}

	}
	/**
	 * Returns the ship operator.
	 * @return the shipOperator
	 */
	public ShipOperator getOperator(){
		return operator;
	}

	/**
	 * Enables restarting by the user's pressing spacebar.
	 */
	public void enableRestart(GameWorld gameWorld) {
		restartEnabled = true;
		this.gameWorld = gameWorld;
	}
}