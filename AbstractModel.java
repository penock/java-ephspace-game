import java.awt.Rectangle;
import java.awt.Shape;
import java.io.Serializable;

import javax.swing.JComponent;

import com.croftsoft.apps.mars.model.Model;
import com.croftsoft.core.math.geom.Circle;


/*
 * Created on Apr 26, 2005
 *
 */

/**
 * @author Adrian Martinez, Phil Enock, Ashok Pillai
 *
 * An abstract class representing a model.  Every Model in our program is an AbstractModel.
 * Constructors of subclass need to specify animator.
 */
public abstract class AbstractModel implements Comparable, Model, Serializable {

	    private static final long  serialVersionUID = 0L;

	    /** center X coordinate of model */
	 	protected double x;
	 	
	 	/** center Y coordinate of model */
		protected double y;
		
		/** heading angle for the direction that model is going in */
		protected double heading;
		
		/** velocity of the model */
		protected double velocity;

		/** whether or not model has been updated during this game loop 
		 */
		protected boolean updated;
		
		/** whether or not the model is active/on screen */
		protected boolean active=true;

		/**underlying collision shape of the model (all are circles) */
		protected Circle collisionShape;
		
		/** the world, required for callbacks */
		protected GameWorld gameWorld; 

		/** the animator associated with this model */
	    protected ModelAnimator animator;
	     
	    /**
	     * constructor, initializes instance variables
	     * @param x x-coordinate of center of model
	     * @param y y-coordinate of center of model
	     * @param heading heading of model
	     * @param velocity velocity of model
	     * @param gameWorld game world model resides in
	     */
	    public AbstractModel(double x, double y, double heading, double velocity, GameWorld gameWorld){
	    	assert velocity >=0 && gameWorld !=null;
	    	this.x=x;
	    	this.y=y;
	    	this.heading=heading;
	    	this.velocity=velocity;
	    	this.gameWorld=gameWorld;
	    }
	    
	    /**
	     * Updates state of the model.
	     * @param component the component (main window)
	     */
	    public void update (JComponent component) {
	    	moveATurn(component);
	    	updated = true;
		    component.repaint();
	    }

		/**
		 * Move according to the current velocity one frame.  If necessary, model
		 * will "pac-man" across to the other side of the screen.
		 * @param component the JComponent is necessary to get the bounds of the playing surface
		 */
		protected void moveATurn(JComponent component) {
			Rectangle bounds = component.getBounds();
	        x += velocity * Math.cos ( heading );
	        y += velocity * Math.sin ( heading );
	        double radius = collisionShape.getRadius();
	        
	        //if boundary is crossed, move over to other side
	        if (x - radius > bounds.getWidth())
	        	x = -radius + 1;
	        else if (x + radius < 0)
	        	x = bounds.getWidth() + radius - 1;
	        if (y - radius > bounds.getHeight())
	       	    y = -radius + 1;
	        else if (y + radius < 0)
	        	y = bounds.getHeight() + radius - 1;

	        setCenter (x, y);
	    }
	    
	 	/** not using this method; it's here for compatibility with Croft */
	 	public void  update ( double  timeDelta ) {}

	 	/**
	 	 * Returns the heading angle for the direction that model is going in.
	 	 * @return heading angle for the direction that model is going in
	 	 */
	 	public double getHeading () {
	 		return heading;
	 	}
	 	
	     /**
	      * Compares Z values. Necessary to implement comparable.
	      * @param other another model
	      */
	     public int  compareTo ( Object  other )   {
	     	assert other != null;
	       double  otherZ = ( ( Model ) other ).getZ ( );

	       double  z = getZ();

	       return (int) Math.round(z - otherZ);
	     }
	     
	     /**
	      * Returns the animator for this model.
	      * This method is used to call a paint.  
	      * @return the animator for this model
	      */
	     public ModelAnimator getAnimator(){
	     	return animator;
	     }
	     
	     /**
	      * Sets the animator for this model.
	      * @param anim the new animator for this model
	      */
	     public void setAnimator(ModelAnimator anim){
	     	animator=anim;
	     }
	     
	     /**
	      * inherited method from Croft library that we aren't using
	      */
	     public double getZ(){
	     	return 0.0;
	     }
	     
	     /**
	      * @return whether the model is active or not
	      */
	     public boolean isActive(){
	     	return active;
	     }
	     
	     /**
	      * @return whether the model has been updated or not (Croft method)
	      */
	     public boolean isUpdated(){
	     	return updated;
	     }
	     
	     /**
	      * post:  the state is prepared to be updated
	      */
	     public void prepare(){
	     	updated=false;
	     }
	     
	     /**
	      * @return the collision shape for this model
	      */
	     public Shape getShape(){
	     	return collisionShape;
	     }
	     
	 	/** 
	 	 * sets center of model to be given coordinates
	 	 *@param x - x coordinate of center
	 	 *@param y - y coordinate of center
	 	 */
	 	public void setCenter(double x, double y) {
	 		this.x=x;
	 		this.y=y;
	 		collisionShape.setCenter(x,y);
	 	}
	 	
	 	/**
	 	 * 
	 	 * @return the x coord of the center of the model
	 	 */
	 	public double getX(){
	 		return x;
	 	}
	 	
	 	/**
	 	 * 
	 	 * @return the y coord of the center of the model
	 	 */
	 	public double getY(){
	 		return y;
	 	}
}	