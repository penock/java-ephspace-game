import java.awt.Color;

/*
 * Created on May 2, 2005
 *
 */

/**
 * @author 05amp
 *	Weapon class that shoots bullets from a ship
 */
public abstract class Weapon {
	
	//instance variables
		//number of frames that have to elapse before next bullet can be fired
	protected long firingRate = Constants.DEFAULT_FIRING_RATE;
	protected long bulletLifeSpan = Constants.BULLET_LIFESPAN;
	protected double bulletSpeed = Constants.DEFAULT_BULLET_SPEED;
	protected int numUpgrades; //number of time weapon has been upgraded
	protected int shotCounter; //elapsed frames since last shot;
	protected Color bulletColor; //color of bullets fired by weapon
	protected Ship shooter; /** the ship that possesses this weapon */
	protected GameWorld theWorld; /** the game world */
	
	/**
	 * Contructor: initializes instance variables
	 * @param s ship that is shooting weapon
	 * @param world GameWorld ship is shooting in
	 * @param c Color of bullets fired by weapon
	 */
	public Weapon(Ship s, GameWorld world, Color c){
		assert s!=null && world !=null;
		shooter=s;
		theWorld=world;
		bulletColor=c;
	}
	
	/**
	 * Decrements time necessary between succesive firings
	 * of weapon as long as limit has not been reached
	 *
	 */
	public void increaseFiringRate(){
		if(firingRate>Constants.MAX_FIRING_RATE){
			firingRate--;
		}
	}
	/**
	 * upgrades weapon by increasing bullet speed 
	 * and lifespan of bullet, 
	 * as long as weapon still can be upgraded
	 *
	 */
	public void upgrade(){
		if(numUpgrades<Constants.MAX_NUM_WEAPON_UPGRADES){
			bulletSpeed*=Constants.BULLET_SPEED_UPGRADE_RATIO ;
			bulletLifeSpan*=Constants.BULLET_LIFESPAN_UPGRADE_RATIO ;
		}
		numUpgrades++;
	}
	/**
	 * fires weapon
	 * must also reset shot counter
	 */
	public abstract void fire();
	
	/**
	 * increments time since weapon was last fired
	 *
	 */
	public void update(){
		shotCounter++;
	}
}
