import java.awt.Color;

/*
 * Created on May 10, 2005
 *
 */

/**
 * @author 06aam
 * This weapon fires three bullets with every shot.  It's devastating.
 */
public class ScatterCannon extends Weapon {

	
	/**
	 * Constructor initializes instance variables
	 * @param s ship that fires weapon
	 * @param world world weapon fires bullets in
	 */
	public ScatterCannon(Ship s, GameWorld world, Color c){
		super(s, world, c);
	}
	
	/**
	 * fires the three bullets at three different angles
	 */
	public void fire() {
		if(shotCounter>=firingRate){
			theWorld.createBullet(shooter, bulletSpeed, bulletLifeSpan, 
					bulletColor, shooter.getOrientation());
			theWorld.createBullet(shooter, bulletSpeed, bulletLifeSpan,
					bulletColor, 
					shooter.getOrientation()+Constants.SCATTER_DELTA);
			theWorld.createBullet(shooter, bulletSpeed, bulletLifeSpan,
					bulletColor,
					shooter.getOrientation()-Constants.SCATTER_DELTA);
			shotCounter=-1;
		}

	}

}
