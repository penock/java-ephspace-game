import java.awt.Color;

import javax.swing.JComponent;

import com.croftsoft.apps.mars.model.ModelAccessor;
/*
 * Created on Apr 24, 2005
 *
 */

/**
 * @author Ashok Pillai, Adrian Martinez, Phil Enock
 * Serves as the animator class for Bullet objects
 * (uses the graphical circle rather than an Image) 
 */
public class BulletAnimator extends ModelAnimator {
	
	/**
	 * Constructs a new BulletAnimator. 
	 * @param modelAccessor the modelAccessor associated with this animator.
	 * @param animatedComponent the component (main window).
	 */
	public BulletAnimator(ModelAccessor modelAccessor, Color bulletColor) {
		super(modelAccessor, bulletColor);
	}

	/**
	 * Updates the state of the animation.  
	 */
	public void update (JComponent component) {
		assert component!=null;
		
		super.update(component);
	}
}