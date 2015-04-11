import java.awt.Color;

/*
 * Created on May 2, 2005
 * 
 */

/**
 * @author 05amp
 *	Cannon weapon that fires a straight stream of bullets
 *
 */
public class Cannon extends Weapon {
	
	
		
	
	/**
	 * Constructor initializes instance variables
	 * @param s ship that fires weapon
	 * @param world world weapon fires bullets in
	 */
	public Cannon(Ship s, GameWorld world, Color c){
		super(s, world, c);
	}
	/**
	 * fires weapon, 
	 * ensuring that maximum firing rate is not exceeded
	 * resets counter for when weapon was last fired
	 */
	public void fire(){
		if(shotCounter>=firingRate){
			theWorld.createBullet(shooter, bulletSpeed, bulletLifeSpan, 
					bulletColor, shooter.getOrientation());
			shotCounter=-1;
		}
	}
}
