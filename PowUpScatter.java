/*
 * Created on May 10, 2005
 *
 */

/**
 * @author Adrian Martinez, Phil Enock, Ashok Pillai
 * This class changes the receivers weapon to a ScatterCannon.
 * 
 */
public class PowUpScatter extends PowerUp {
	/**
	 * Constructor: calls super constructor
	 * @param x x-coord of center of PowerUp
	 * @param y y-coord of center of PowerUp
	 * @param radius radius of PowerUp bounds
	 */
	public PowUpScatter(double centerX, double centerY, double radius,GameWorld gw){
		super(centerX,centerY,radius,gw);
	}


	/**
	 * changes the weapon to a ScatterCannon
	 * @param the ship that got this powerUp
	 */
	public void doPowerUp(Ship s) {
		s.setWeapon(new ScatterCannon(s, gameWorld, s.getColor()));
		gameWorld.remove(this);
	}
	
	/**
	 * @return the type of power up this is
	 */
	public String toString(){
		return "Scatter shot";
	}

}
