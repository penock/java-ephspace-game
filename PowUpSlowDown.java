/*
 * Created on May 9, 2005
 *
 */

/**
 * @author Adrian Martinez, Phil Enock, Ashok Pillai
 * 
 * This class defines a power up that slows down the max speed
 * of the ship
 */
public class PowUpSlowDown extends PowerUp {


	/**
	 * Constructor: calls super constructor
	 * @param x x-coord of center of PowerUp
	 * @param y y-coord of center of PowerUp
	 * @param radius radius of PowerUp bounds
	 */
	public PowUpSlowDown(double centerX, double centerY, double radius,GameWorld gw){
		super(centerX,centerY,radius,gw);
	}

	/**
	 * Slows down the ship by a factor of 3/4.
	 * @param s ship that powerup acts upon
	 */
	public void doPowerUp(Ship s) {
		assert s!=null;
		if((s.getMaxVelocity()*.75)
				>(Constants.MIN_ALLOWED_SHIP_VELOCITY)){
			
			s.setMaxVelocity(s.getMaxVelocity()*.75);
			
		}else {
			s.setMaxVelocity(Constants.MIN_ALLOWED_SHIP_VELOCITY);
		}
		
		gameWorld.remove(this);
	}
	
	/**
	 * @return the type of power up this is
	 */
	public String toString(){
		return "Slow down";
	}

}
