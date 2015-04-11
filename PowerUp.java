import javax.swing.JComponent;

import com.croftsoft.core.math.geom.Circle;

/*
 * Created on May 1, 2005
 *
 */

/**
 * @author 05amp
 *	PowerUp class.
 *
 */
public abstract class PowerUp extends AbstractModel {

	
	/**
	 * Constructor: initializes instance variables
	 * @param x x-coord of center of PowerUp
	 * @param y y-coord of center of PowerUp
	 * @param radius radius of PowerUp bounds
	 */
	public PowerUp(double x, double y, double radius, GameWorld gw){
		super(x,y,0,0,gw);
		assert radius>0;
		collisionShape=new Circle(x,y,radius);
	}
	/**
	 * executes powerup action on ship
	 * @param ship that powerup acts upon
	 */
	public abstract void doPowerUp(Ship s);
	

	/**
	 * Updates the state of the PowerUp.
	 * (state never changes since it is stationary)  
	 */
	public void update(JComponent component) {
		updated=true;
	}





}
