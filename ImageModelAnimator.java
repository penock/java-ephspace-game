import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import com.croftsoft.apps.mars.model.ModelAccessor;
import com.croftsoft.core.math.geom.Circle;
/*
 * Created on Apr 24, 2005
 *
 */

/**
 * @author 05pme
 * Serves as the animator class for any simple modelAccessor that uses an image, such
 * as Asteroid. 
 */
public class ImageModelAnimator extends ModelAnimator {
	
	/** reference to the buffered image (there is one instance per image file, shared by all animators of that type) */
	private BufferedImage image; 
	
	/** the current rotation transformation, applied when painting to align the object correctly */
	private AffineTransformOp orientationTransform;
	
	/**
	 * Constructs a new ImageModelAnimator, associated with the model passed in (as a ModelAccessor). 
	 * @param modelAccessor the modelAccessor associated with this animator.
	 * @param image the image used by this animator.
	 * @param animatedComponent the component (main window).
	 */
	public ImageModelAnimator(ModelAccessor modelAccessor,
							  BufferedImage image) {
		super(modelAccessor);
		
		assert image != null && modelAccessor != null;
		
		this.image = image;
		updateOrientation(0.0);
	}

	/**
	 * Updates the state of the animator (if active).
	 * Basically, this triggers a repaint every frame. 
	 * @param component the main game window
	 */
	public void update (JComponent component) {
		super.update(component);
	}
	
	/**
	 * Updates the orientation of the image.
	 * @param orientation the desired orientation of the image 
	 */
	public void updateOrientation (double orientation ) {
		
		orientationTransform = new AffineTransformOp(
				AffineTransform.getRotateInstance(orientation,
												  image.getWidth() / 2.0,	// center x
												  image.getHeight() / 2.0), // center y (around which to rotate)
				AffineTransformOp.TYPE_NEAREST_NEIGHBOR
							 );

	}
	
    /**
     *  Paints image of model on graphics object of an AnimatedComponent 
     *  (as outlined by CroftSoft library) 
     *@param component is main window of game
     *@param graphics is "canvas" of main window
     */
	public void  paint (JComponent  component, Graphics2D  graphics )   {
    	assert modelAccessor != null && 
			   graphics != null &&
			   orientationTransform != null;
    	
    	if (modelAccessor.isActive()) {
    		
    		Rectangle bounds = modelAccessor.getShape().getBounds();
    		
    		graphics.drawImage(image,
    				this.orientationTransform,
    				(int)(bounds.getCenterX() - image.getWidth() / 2.0), 
    				(int)(bounds.getCenterY() - image.getHeight() / 2.0));
    		
    		Circle shipCirc = (Circle)modelAccessor.getShape();

    		if(Constants.DEBUG_MODE) {
	    		graphics.setColor(Color.GRAY);
	    		// draw collision shape
	    		graphics.drawOval((int)shipCirc.getMinX(), (int)shipCirc.getMinY(), (int)shipCirc.getWidth(), (int)shipCirc.getHeight());
	    		// draw center dot
	    		graphics.fillOval((int)shipCirc.getCenterX() - 1, (int)shipCirc.getCenterY() - 1, 2, 2);
    		}
    	}
    	
	}
}