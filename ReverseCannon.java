import java.awt.Color;

/*
 * Created on May 12, 2005
 * 
 */

/**
 * @author 05amp
 *	Cannon weapon that fires a straight stream of bullets from rear of ship
 *
 */
public class ReverseCannon extends Weapon {
	
	
		
	
	/**
	 * Constructor initializes instance variables
	 * @param s ship that fires weapon
	 * @param world world weapon fires bullets in
	 */
	public ReverseCannon(Ship s, GameWorld world, Color c){
		super(s, world, c);
	}
	/**
	 * fires weapon backwards, 
	 * ensuring that maximum firing rate is not exceeded
	 * resets counter for when weapon was last fired
	 */
	public void fire(){
		if(shotCounter>=firingRate){
		double target = (shooter.getOrientation()+Math.PI)%(2*Math.PI);
			theWorld.createBullet(shooter, bulletSpeed, bulletLifeSpan, 
					bulletColor, target);
			shotCounter=-1;
		}
	}
}
