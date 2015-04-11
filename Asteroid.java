import javax.swing.JComponent;

import com.croftsoft.core.animation.ComponentUpdater;
import com.croftsoft.core.math.geom.Circle;


/*
 * Created on Apr 24, 2005
 *
 */

/**
 * @author Adrian Martinez, Ashok Pillai, Phil Enock
 * Asteroid is an object that moves in a straight direction through space
 *
 */

public class Asteroid extends AbstractModel implements ComponentUpdater // implements ComponentUpdater is TEMPORARY (for phase 1 code only) 
		{

	//Instance variables
	private final double radius;
	
	/** orientation the orientation of the asteroid image */
	private double orientation = 0;

	/**
	 * Constructor
	 * @param x initial x coord
	 * @param y initial y coord
	 * @param heading direction of travel in radians
	 * @param velocity speed in pixels/second
	 * @param radius in pixels
	 */
	public Asteroid(GameWorld gameWorld, double x, double y,
					double heading, double velocity,
					double radius) {
		super(x,y,heading,velocity,gameWorld);
		assert radius >=0;
		this.radius = radius;
		
		collisionShape = new Circle(x,y,radius);
	}
	

	

	/**
	 * Updates the state of the Asteroid.
	 *@param component window asteroid is drawn in  
	 */
	public void update(JComponent component) {
		assert component !=null;
		
		//updates orientation
		orientation += Constants.ASTEROID_ROTATION;
		if (orientation > 2 * Math.PI)
			orientation -= 2 * Math.PI;
		else if (orientation < 0)
			orientation += 2 * Math.PI;
		animator.updateOrientation(orientation);

		super.update(component);
	}

}