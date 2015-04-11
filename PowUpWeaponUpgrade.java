/*
 * Created on May 12, 2005
 *
 */

/**
 * @author Adrian Martinez, Phil Enock, Ashok Pillai
 * This power up gives the user an extra life
 */
public class PowUpWeaponUpgrade extends PowerUp {
	/**
	 * Constructor: calls super constructor
	 * @param x x-coord of center of PowerUp
	 * @param y y-coord of center of PowerUp
	 * @param radius radius of PowerUp bounds
	 */
	public PowUpWeaponUpgrade(double centerX, double centerY, double radius,GameWorld gw){
		super(centerX,centerY,radius,gw);
	}
	/**
	 * upgrades weapon of ship
	 * @param s the ship receiving the power up
	 */
	public void doPowerUp(Ship s) {
		s.getWeapon().upgrade();
		gameWorld.remove(this);
	}
	
	/**
	 * @return what kind of power up this is
	 */
	public String toString(){
		return "weapon upgrade";
	}

}
