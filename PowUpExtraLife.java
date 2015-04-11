/*
 * Created on May 10, 2005
 *
 */

/**
 * @author Adrian Martinez, Phil Enock, Ashok Pillai
 * This power up gives the user an extra life
 */
public class PowUpExtraLife extends PowerUp {
	/**
	 * Constructor: calls super constructor
	 * @param x x-coord of center of PowerUp
	 * @param y y-coord of center of PowerUp
	 * @param radius radius of PowerUp bounds
	 */
	public PowUpExtraLife(double centerX, double centerY, double radius,GameWorld gw){
		super(centerX,centerY,radius,gw);
	}
	/**
	 * adds a life to the ship
	 * @param s the ship receiving the power up
	 */
	public void doPowerUp(Ship s) {
		if(s.isUserShip()) gameWorld.addLife();
		gameWorld.remove(this);

	}
	
	/**
	 * @return what kind of power up this is
	 */
	public String toString(){
		return "extra life";
	}

}
