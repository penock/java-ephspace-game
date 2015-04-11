/*
 * Created on May 12, 2005
 *
 */

/**
 * @author Adrian Martinez, Phil Enock, Ashok Pillai
 * This is a powerUp that improves the shields of a ship
 *
 */
public class PowUpShieldUpgrade extends PowerUp {



	/**
	 * Constructor: calls super constructor
	 * @param x x-coord of center of PowerUp
	 * @param y y-coord of center of PowerUp
	 * @param radius radius of PowerUp bounds
	 */
	public PowUpShieldUpgrade(double centerX, double centerY, double radius,GameWorld gw){
		super(centerX,centerY,radius,gw);
	}

	/**
	 * Increments the max HP capacity of ship
	 * Increments hit points by new capacity increase
	 * @param s the ship receiving this powerup
	 */
	public void doPowerUp(Ship s) {
		s.incrementMaxHP(Constants.HP_INCREMENT);
		s.modifyHitPoints(Constants.HP_INCREMENT);
		gameWorld.remove(this);
	}
	
	/**
	 * @return what kind of power up this is
	 */
	public String toString(){
		return "Shield Upgrade";
	}

}
