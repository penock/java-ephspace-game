/**
 * @author Adrian Martinez, Ashok Pillai, Phil Enock
 * Bullet is an object that moves in a straight direction through space
 * for a limited amount of time.
 *
 */

import javax.swing.JComponent;

import com.croftsoft.core.animation.ComponentUpdater;
import com.croftsoft.core.math.geom.Circle;

public class Bullet extends AbstractModel implements ComponentUpdater{
	//instance vars
	private final Ship shooter;
	private long lifespan;//number of updates before bullet "dies"
	private final double radius = Constants.BULLET_RADIUS;

	
	/**
	 * pre: s is not null
	 * Constructor: initializes instance vars
	 * @param g world bullet is created in
	 * @param s ship that fired bullet
	 * @param speed speed of bullet
	 * @param lifespan duration bullet travels unless it hits something first
	 */
	public Bullet(GameWorld g, Ship s, double speed, long lifespan, double heading){
		super(s.getX(),s.getY(),heading,speed,g);
		assert  s != null && lifespan >= 0;
		
		shooter=s;
		this.lifespan=lifespan;
		
		collisionShape = new Circle(x,y,radius);
	}



	/**
	 * Updates the state of the Bullet:
	 * Moves it according to its speed and decrements lifespan
	 * Removes bullet from world if lifespan expires
	 *@param component window bullet moves in
	 */
	public void update(JComponent component) {
		assert component != null;
		
		if(lifespan>0){
			super.update(component);
			
			checkCollisions();
			
			lifespan--;
		}
		else {
			gameWorld.remove(this);
		}
		updated=true;
	}
	
	

	/**
	 * Performs collision detection. 
	 * Notifies gameWorld if collision with asteroid.
	 */
	private void checkCollisions() {
		Asteroid[] asteroids=gameWorld.getAsteroids();
		for (int i=0; i<asteroids.length; i++){
			if (((Circle)(asteroids[i].getShape())).intersectsCircle(collisionShape)){
				gameWorld.shotAsteroid(asteroids[i], shooter, this);
			}
		}
	}

	
	/**
	 * returns ship that fired bullet
	 * @return ship that fired bullet
	 */
	public Ship getShooter(){
		return shooter;
	}
  }
