
/*
 * Created on May 1, 2005
 *
 */

/**
 * @author 05amp
 *
 *	PowerUp that increases the maximum possible speed 
 *	of a ship that hits it
 */
public class PowUpSpeedUp extends PowerUp {

	/**
	 * Constructor: calls super constructor
	 * @param x x-coord of center of PowerUp
	 * @param y y-coord of center of PowerUp
	 * @param radius radius of PowerUp bounds
	 */
	public PowUpSpeedUp(double centerX, double centerY, double radius,GameWorld gw){
		super(centerX,centerY,radius,gw);
	}
	
	/**
	 * Speeds up the ship by the original velocity/2
	 * @param s ship that powerup acts upon
	 */
	public void doPowerUp(Ship s) {
		assert s!=null;
		if((s.getMaxVelocity()+Constants.DEFAULT_SHIP_VELOCITY/2)
				<Constants.MAX_ALLOWED_SHIP_SPEED) {
			
			s.setMaxVelocity(s.getVelocity()+(Constants.DEFAULT_SHIP_VELOCITY/2));
		}else{
			s.setMaxVelocity(Constants.MAX_ALLOWED_SHIP_SPEED);
		}
		
		gameWorld.remove(this);
	}
	
	/**
	 * @return the type of power up this is
	 */
	public String toString(){
		return "Speed up";
	}
	
}
