/*
 * Created on May 9, 2005
 *
 */

/**
 * @author Adrian Martinez, Phil Enock, Ashok Pillai
 * This is a powerUp that gives the ship its full HP
 *
 */
public class PowUpFullHP extends PowerUp {



	/**
	 * Constructor: calls super constructor
	 * @param x x-coord of center of PowerUp
	 * @param y y-coord of center of PowerUp
	 * @param radius radius of PowerUp bounds
	 */
	public PowUpFullHP(double centerX, double centerY, double radius,GameWorld gw){
		super(centerX,centerY,radius,gw);
	}

	/**
	 * Sets hit points to the max hit points allowed
	 * @param s the ship receiving this powerup
	 */
	public void doPowerUp(Ship s) {
		s.modifyHitPoints(s.getMaxHP()-s.getHitPoints());
		gameWorld.remove(this);
	}
	
	/**
	 * @return what kind of power up this is
	 */
	public String toString(){
		return "Full HP";
	}

}
