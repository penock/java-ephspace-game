/*
 * Created on Apr 24, 2005
 *
 */

	    import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import javax.swing.JComponent;

import com.croftsoft.apps.mars.model.Model;
import com.croftsoft.apps.mars.model.ModelAccessor;
import com.croftsoft.core.animation.AnimatedComponent;
import com.croftsoft.core.animation.ComponentPainter;
import com.croftsoft.core.animation.ComponentUpdater;
import com.croftsoft.core.awt.image.ImageLib;
import com.croftsoft.core.lang.NullArgumentException;
import com.croftsoft.core.math.geom.Point2DD;
import com.croftsoft.core.math.geom.PointXY;
import com.croftsoft.core.math.geom.ShapeLib;
import com.croftsoft.core.util.ArrayKeeper;
import com.croftsoft.core.util.ArrayLib;
import com.croftsoft.core.util.StableArrayKeeper;
	     
	     
	     /**
	      * 
	      * @author Adrian Martinez, Ashok Pillai, Phil Enock
	      * 
	      * This class manages the GameWorld and knows which models are
	      * in the world at all times.  It is based on Croft's SeriGame,
	      * from the Mars library.
	      */
public class GameWorld
	       implements Serializable, ComponentPainter, ComponentUpdater {

	     private static final long  serialVersionUID = 0L;
	     
	     // buffered images
	     private BufferedImage ASTEROID_IMAGE; // can't make final due to try/catch uncertainty
	     private BufferedImage USER_SHIP_IMAGE;
	     private BufferedImage COMPUTER_SHIP_IMAGE;
	     private BufferedImage POWERUP_IMAGE;
	     
	     // user data
	     private int userLives = Constants.STARTING_LIVES;
	     private int userScore;
	     private int userLevel;
	     
	     // ship controlled by human player
	     private Ship userShip;
	     
	     // The ArrayKeeper (see com.croftsoft.core.util.ArrayKeeper) that holds the models
	     private final ArrayKeeper modelArrayKeeper;
	     
	     // needed for CroftSoft code relating to distance in ShapeLib
	     private final Point2DD center;
	     
	     /** user's info display */
	     private final UserInfoDisplay userInfoDisplay;
	     
	     /** the JComponent which has the painting surface */
	     private final AnimatedComponent animatedComponent;
	     
	     /** the ComputerControllers for computer ships */
	     private final Vector compPlayers=new Vector();
	     
	     /** the random generator for placing objects randomly in the world */
	     private final Random randGen=new Random();
	     
	     /** the UserController */ 
	     private UserController userController;

	     /** keeps track of whether the game state is active or in "game over" state*/
		 private boolean gameOver = false;
	     
	     /**
	      * Constructor initializes center and modelArrayKeeper
	      * @param animatedComponent the JComponent which has the painting surface.
	      */  
	     public GameWorld (AnimatedComponent animatedComponent) {
	     	assert animatedComponent != null;
	     	
	     	this.animatedComponent = animatedComponent;
	     	modelArrayKeeper = new StableArrayKeeper ( new Model [ 0 ] );
	     	center = new Point2DD ( );

	     	userInfoDisplay = new UserInfoDisplay(this, animatedComponent);
	     	userInfoDisplay.showMessage(UserInfoDisplay.START_MSG);
	     	
	     	// load images
	     	try {
	     		/* on FreeBSD machines, we used to use this class loader:
										ClassLoader.getSystemClassLoader(),
				   but that caused an AccessControlException on my PC. -Phil*/

	     		ASTEROID_IMAGE = ImageLib.loadAutomaticImage(Constants.ASTEROID_IMAGE_FILE,
	     								Transparency.TRANSLUCENT,
										animatedComponent,
										Thread.currentThread().getContextClassLoader(),
										null);
	     		
	     		USER_SHIP_IMAGE = ImageLib.loadAutomaticImage(Constants.USER_SHIP_IMAGE_FILE,
	     								Transparency.TRANSLUCENT,
										animatedComponent,
										Thread.currentThread().getContextClassLoader(),
										null);

	     		Constants.setUserShipBounds(USER_SHIP_IMAGE.getHeight(),USER_SHIP_IMAGE.getWidth());

	     		COMPUTER_SHIP_IMAGE = ImageLib.loadAutomaticImage(Constants.COMPUTER_SHIP_IMAGE_FILE,
							Transparency.TRANSLUCENT,
						animatedComponent,
						Thread.currentThread().getContextClassLoader(),
						null);

	     		Constants.setComputerShipBounds(COMPUTER_SHIP_IMAGE.getHeight(),COMPUTER_SHIP_IMAGE.getWidth());

	     		POWERUP_IMAGE = ImageLib.loadAutomaticImage(Constants.POWERUP_IMAGE_FILE,
							Transparency.TRANSLUCENT,
							animatedComponent,
							Thread.currentThread().getContextClassLoader(),
							null);

	     	} catch (IOException e) {
	     		System.out.println("IO Exception:" + e);
	     		System.exit(0);
	     	}
	    	     	
	     	//initialize game
	     	initGame();
	     }

	     /**
	      * Initializes the game: creates ships and asteroids.
	      *
	      */
	     private void initGame() {
	        for(int i=0; i<Constants.NUM_ASTEROIDS; i++){
	          	createRandomAsteroid();
	          }
	                      
	          createUser(Constants.USER_START_X, Constants.USER_START_Y);
	          
	          for(int i=0; i<Constants.NUM_COMPUTER_SHIPS; i++){
	          	createRandomComputerShip();
	          }
	          
	 	     userLives = Constants.STARTING_LIVES;
		     userScore = 0;
		     userLevel = 0;
		     userShip.notifyListeners();
	     }
	     
	     /**
	      * Clears the world.
	      */
	     private void clear(){
	       modelArrayKeeper.setArray ( new Model [ 0 ] );
	     }

	     /** 
	      * Creates a bullet fired by a given ship.
	      * @param s the ship who is firing the bullet
	      * @param speed the speed of the bullet
	      * @param lifespan the lifespan of the bullet
	      * @return the bullet, which has been created and placed in world
	      */
	     public Bullet createBullet (Ship s, double speed, long lifespan, Color color, double heading){
	     	assert s != null;
	     	
	       Bullet  bullet
	         = new Bullet(this, s, speed, lifespan, heading);

	      	bullet.setAnimator( 
     				new BulletAnimator(bullet, color));
	       
	       modelArrayKeeper.insert(bullet);

	       return bullet;
	     }

	     /**
	      * pre: centerX and Y are valid coordinates
	      * 
	      * @param centerX the starting x-coord of the asteroid
	      * @param centerY the starting y-coord of the asteroid
	      * @param radius the size of the asteroid
	      * @return the new asteroid that has been put into the world
	      */
	     public Asteroid  createAsteroid (
	     		double centerX, 
	     		double centerY,
				double heading,
				double velocity,
				double radius) {
	     	
	     	assert radius > 0;
	     	assert velocity >= 0;
	     	
	     	Asteroid  asteroid = new Asteroid (
	     			this, centerX, centerY, heading, velocity, radius);

	     	asteroid.setAnimator( 
     				new ImageModelAnimator(asteroid,
     									   ASTEROID_IMAGE)
						        );
	     	
	     	modelArrayKeeper.insert(asteroid);

	     	return asteroid;
	     }
	     /**
	      * creates a new Asteroid in a random location on the world 
	      * that is not already occupied
	      * also generates random heading and velocity
	      * @return the asteroid that has been created
	      */
	     public Asteroid createRandomAsteroid() {
	     	boolean failed = true;
	     	Asteroid newAsteroid=null;
	     	while(failed){
	     		double x = randGen.nextDouble()*animatedComponent.getBounds().getWidth();
	     		double y = randGen.nextDouble()*animatedComponent.getBounds().getHeight();
	     		double heading = randGen.nextDouble()*2*Math.PI;
	     		double velocity = randGen.nextDouble()*2*Constants.ASTEROID_BASE_SPEED;
	     		Model[] currentModels = getModels();
	     		newAsteroid = createAsteroid(x,y,heading,velocity,Constants.ASTEROID_RADIUS);
	     		
	     		failed = false;
	     		if (currentModels==null){
	     			break;
	     		}
	     		
	     		for(int i=0; i<currentModels.length; i++){
	     			if(newAsteroid.getShape().intersects(currentModels[i].getShape().getBounds())){
	     				failed=true;
	     				remove(newAsteroid);
	     				break;
	     			}
	     		}
	      	}
	     	return newAsteroid;
	     }

	     /**
	      * pre: centerX and Y are valid coordinates
	      * 
	      * @param centerX the starting x-coord of the ship
	      * @param centerY the starting y-coord of the ship
	      * @return the new ship that has been put in the world
	      */
	     public Ship  createComputerShip (
	       double  centerX,
	       double  centerY ) { 
	     	
	       Ship  ship = new Ship ( this, centerX, centerY, Constants.COMPUTER_BULLET_COLOR, false);
	       ShipOperator  shipOperator = new ShipOperator ();
	       ship.setShipOperator (shipOperator);
	       
	       shipOperator.setShip (ship);
	       ComputerController comp=
	       		new ComputerController(shipOperator, new AIStratRandom(this));
	      
	       compPlayers.add(comp);

	       ship.setAnimator( 
	       		new ImageModelAnimator(ship,
	       				COMPUTER_SHIP_IMAGE)
	       );
	       
	       modelArrayKeeper.insert (ship);
	       
	       return ship;
	     }
	     
	     /**
	      * Creates a computer ship in a random place that doesn't overlap
	      * with any model currently in the world.
	      * @return the ship that has been created
	      */
	     public Ship createRandomComputerShip() {
	     	boolean failed = true;
	     	Ship newShip=null;
	     	while(failed){
	     		double x = randGen.nextDouble()*animatedComponent.getBounds().getWidth();
	     		double y = randGen.nextDouble()*animatedComponent.getBounds().getHeight();
	     		Model[] currentModels = getModels();
	     		newShip = createComputerShip(x,y);
	     		
	     		failed = false;
	     		
	     		//avoids currentModels.length null pointer
	     		if (currentModels==null){
	     			break;
	     		}
	     		
	     		for(int i=0; i<currentModels.length; i++){
	     			if(newShip.getShape().intersects(currentModels[i].getShape().getBounds())){
	     				failed=true;
	     				remove(newShip);
	     				break;

	     			}
	     		}
	      	}
	     	return newShip;
	     }
	     
	     /**
	      * pre: centerX and Y are valid coordinates
	      * Places new PowerUp of random type in world at given coordinates
	      * @param centerX x-coord of center of PowerUp
	      * @param centerY y-coord of center of PowerUp
	      * @return new PowerUp object that has been placed in world
	      */
	     public PowerUp createPowerUp(double centerX, double centerY){
	     		     	
	       PowerUp pUp;
	       int powerUpChoice=randGen.nextInt(8);
	       
	       if(powerUpChoice==0){
	       	pUp = new PowUpSpeedUp(centerX,centerY,POWERUP_IMAGE.getWidth()/2,this);
	       }
	       else if(powerUpChoice==1){
	       	pUp = new PowUpSlowDown(centerX, centerY, POWERUP_IMAGE.getWidth()/2, this);
	       }
	       
	       else if (powerUpChoice==2){
	       	pUp = new PowUpFullHP(centerX, centerY, POWERUP_IMAGE.getWidth()/2, this);
	       }
	       
	       else if (powerUpChoice==3){
	       	pUp = new PowUpExtraLife(centerX, centerY, POWERUP_IMAGE.getWidth()/2, this);
	       }
	       else if(powerUpChoice==4){
	       	pUp = new PowUpScatter(centerX, centerY, POWERUP_IMAGE.getWidth()/2, this);
	       }
	       else if(powerUpChoice==5){
	       	pUp = new PowUpReverseCannon(centerX, centerY, POWERUP_IMAGE.getWidth()/2, this);
	       }
	       else if(powerUpChoice==6){
	       	pUp = new PowUpShieldUpgrade(centerX, centerY, POWERUP_IMAGE.getWidth()/2, this);
	       }
	       else{
	       	pUp = new PowUpWeaponUpgrade(centerX, centerY, POWERUP_IMAGE.getWidth()/2, this);
	       }

	      	pUp.setAnimator( 
     				new ImageModelAnimator(pUp,POWERUP_IMAGE));
	       
	       modelArrayKeeper.insert(pUp);

	       return pUp;
	     }
	     
	     /**
	      * pre: centerX and Y are valid coordinates
	      * Places new PowerUp of random type in world at given coordinates with a certain probablity
	      * @param centerX x-coord of center of PowerUp
	      * @param centerY y-coord of center of PowerUp
	      * @param prob probability powerup is created
	      */
	     public void maybeCreatePowerUp(double centerX, double centerY, double prob){
	     	double random = randGen.nextDouble();
	     	if(random<=prob){
	     		createPowerUp(centerX,centerY);
	     	}
	     }

	     
	     /**
	      * pre: centerX and Y are valid coordinates
	      * 
	      * @param centerX the starting x-coord of the ship
	      * @param centerY the starting y-coord of the ship
	      * @param color the ship's color
	      * @param component the component that knows about user input
	      * @return the new ship that has been put in the world
	      */
	     public Ship  createUser (
	       double  centerX,
	       double  centerY){
     	
	       userShip = new Ship ( this, centerX, centerY, Constants.HUMAN_BULLET_COLOR, true);
	       ShipOperator  shipOperator = new ShipOperator ();
	       userShip.setShipOperator (shipOperator);
	       userController=new UserController(shipOperator, animatedComponent);
	       shipOperator.setShip (userShip);

	       userShip.setAnimator( 
	       		new ImageModelAnimator(userShip,
	       				USER_SHIP_IMAGE)
	       );
	       
	       userShip.addListener(userInfoDisplay);
	       
	       modelArrayKeeper.insert (userShip);

	       return userShip;
	     }

	     
	     
	     /**
	      * removes the model from the world, 
	      * if model is a Ship then also remove its ComputerController
	      * @param model a model in the world to be removed
	      */
	     public void  remove ( AbstractModel  model ) {
	     	if(model instanceof Ship){
	     		removeShipController((Ship) model);
	     	}
	     	modelArrayKeeper.remove ( model );   
	     }
	     
	     /**
	      * pre: should only be called by remove method
	      * removes the computer controller of ship from vector of computer controllers
	      * @param s
	      */
	     private void removeShipController(Ship s){
	     	assert s!=null && compPlayers!=null;
	     	for(int i=0; i<compPlayers.size(); i++){
	     		if(((ComputerController)(compPlayers.get(i))).getShip().equals(s)){
	     			compPlayers.remove(i);
	     		}
	     	}
	     }
	     
	     /**
	      * removes asteroid when it is shot, spawns a new one
	      * and adds points if human player shot it, removes bullet
	      * @param a
	      * @param s
	      */
	     public void shotAsteroid(Asteroid a, Ship s, Bullet b){
	     	respawnAsteroid(a);
			changeScore(Constants.ASTEROID_POINTS, s);
			remove(b);
	     }
	     
	     /**
	      * removes current asteroid and generates a new one;
	      * @param asteroid
	      */
	     public void respawnAsteroid(Asteroid asteroid){
	     	double x = asteroid.getX();
	     	double y = asteroid.getY();
	     	remove(asteroid);
	     	maybeCreatePowerUp(x,y,Constants.POWUP_GEN_PROB_ASTEROID);
	     	createRandomAsteroid();
	     }

	     /**
	      * 
	      * @return an array of the models in the world
	      */
	     public Model [ ]  getModels ( ){
	     	
	       return ( Model [ ] ) modelArrayKeeper.getArray ( );
	    
	     }

	     /**
	      * Copied from SeriWorld.  Finds models that intersect shape
	      * @pre modelAccessors 
	      * @param shape the shape 
	      * @param modelAccessors
	      * @return array of models intersecting shape
	      */
	     public ModelAccessor [] getModelAccessors (
	     		Shape shape,
				ModelAccessor []  modelAccessors ){
	     	
	     	Model [ ]  allModels = getModels ( );

	     	if ( shape == null ){
	     		return allModels;
	     	}

	     	NullArgumentException.check ( modelAccessors );

	     	int  index = 0;

	     	for ( int  i = 0; i < allModels.length; i++ ){
	     		
	     		Model  model = allModels [ i ];

	     		if ( ShapeLib.intersects ( shape, model.getShape ( ) ) ){
	     			if ( index < modelAccessors.length ){
	     				modelAccessors [ index ] = model;
	     			}
	     			else{		
	     				modelAccessors = ( ModelAccessor [ ] )
						ArrayLib.append ( modelAccessors, model );
	     			}
	     			index++;
	     		}
	     	}

	     	if ( index < modelAccessors.length ){
	         modelAccessors [ index ] = null;
	     	}

	     	return modelAccessors;
	     }

	     	/** returns an array of the asteroids */
	     public Asteroid [ ]  getAsteroids ( ){
	       return ( Asteroid [ ] )
	         modelArrayKeeper.getArray ( Asteroid.class );
	     }

	     /** @return an array of the ships */
	     public Ship []  getShips (){
	       return ( Ship [] ) modelArrayKeeper.getArray ( Ship.class );
	     }

	     /** @return an array of the bullets */
	     public Bullet []  getBullets (){
	       return ( Bullet [] ) modelArrayKeeper.getArray ( Bullet.class );
	     }

	     /** @return an array of the powerups */
	     public PowerUp[]  getPowerUps (){
	       return ( PowerUp[] ) modelArrayKeeper.getArray ( PowerUp.class );
	     }

	     /** @post prepares all the models */
	     public void  prepare ( ){
	       Model [ ]  models = getModels ( );

	       for ( int  i = 0; i < models.length; i++ )
	       {
	         models [ i ].prepare ( );
	       }
	     }

	     /**
	      * Updates all Models and ModelAnimators and ComputerControllers.
	      * 
	      */
	     public void update(JComponent component) {
		       prepare();
	     		Model [ ]  models = getModels ( );

		       for ( int  i = 0; i < models.length; i++ )
		       {
		         ((AbstractModel)models[ i ]).update(component);
		       }
		       
		       for(int i=0; i<compPlayers.size(); i++){
		       	((ComputerController)(compPlayers.get(i))).update();
		       }

		       //level up: creates new computer ship
		       if(((int)(userScore/Constants.LEVEL_UP_DELTA)) > userLevel){
		       		userLevel++;
		       		createRandomComputerShip();
		       		userInfoDisplay.showMessage("Level Up!!!");
		       }
		       
		       updateAnimators(component);
	     }

	     /**
	      * Notifies all Models' animators that the Models have been updated.
	      */
	     public void updateAnimators(JComponent component){
	     	Model[] models=getModels();
	     	for(int i=0; i <models.length; i++){
	     		((AbstractModel)models[i]).getAnimator().update(component);
	     	}
	     }
	     
	     /**
	      * Called during the paint phase of the game loop. Paints all models
	      * through their ModelAnimators.
	      * @param component the animatedComponent drawing surface.
	      * @param graphics the drawing surface.
	      */
	     public void  paint (
	            JComponent  component,
	            Graphics2D  graphics ) {

	     	Model [ ]  models = getModels ( );

	     	for ( int  i = 0; i < models.length; i++ ) {
	     		((AbstractModel)models [ i ]).getAnimator().paint(component, graphics);
		    }

	     	userInfoDisplay.paint(component, graphics);
	     }
	     
	     /** 
	      * Croft method. We may at some point base some code on this, if we
	      * decide to reuse bullet objects, so that garbage collection happens
	      * less often. 
	      * @param originX x-coordinate of bullet
	      * @param originY y-coordinate of bullet
	      * @param heading direction bullet will travel
	      */
	     public void  fireBullet (
	       double  originX,
	       double  originY,
	       double  heading){
	     	
	       boolean  bulletFired = false;

	       Bullet [ ]  bullets = ( Bullet [ ] )
	         modelArrayKeeper.getArray ( Bullet.class );

	       //runs through the bullets to see if they need to be updated
	       for ( int  i = 0; i < bullets.length; i++ ){
	       	
	       	Bullet  bullet = bullets [ i ];

	       	//if there is an available bullet, fire it
	       	if ( !bullet.isActive  ( )
	       			&& !bullet.isUpdated ( ) ){
	         	
//	       		bullet.fire ( originX, originY, heading );

	       		bulletFired = true;

	       		break;
	       	}
	       }
	       
	       //otherwise, create a new bullet
	       if ( !bulletFired ){
//	       	createBullet ( originX, originY, heading );
	       }
	     }


	    

	
	     /** 
	      * Croft method.
	      * @param pointXY the point being looked at
	      * @param classes the classes that are being looked for
	      * @param model the model that should be ignored
	      * @return a model at the point if one exists, null otherwise
	      */
	     public Model  getModel (
	       PointXY    pointXY,
	       Class [ ]  classes,
	       Model      model ){
	     	
	     	double  x = pointXY.getX ( );
	     	double  y = pointXY.getY ( );

	     	Model [ ]  models = getModels ( );

	     	for ( int  i = 0; i < models.length; i++ ){
	     		
	     		Model  otherModel = models [ i ];

	     		if ( ( otherModel != model )
	     				&& otherModel.isActive ( )
						&& otherModel.getShape ( ).contains ( x, y )){
	     			for ( int  j = 0; j < classes.length; j++ ){
	     				if ( classes [ j ].isInstance ( otherModel ) ){
	          
	     					return otherModel;
	     				}
	     			}
	     		}        
	       }

	     	return null;
	     }

	     /**
	      * Croft method.
	      * @param c The class desired
	      * @return an array of the models of the specified class,
	      *    or all models if c is null.
	      */
	     private Model [ ]  getModels (Class  c ){
	       if ( c == null ){
	         return ( Model [ ] ) modelArrayKeeper.getArray ( );
	       }

	       return ( Model [ ] ) modelArrayKeeper.getArray ( c );
	     }

	     /**
	      * Croft method.
	      * @param pointXY The point being looked at
	      * @param models an empty model array
	      * @param c the class of the returned models
	      * @return an array of models of the specified type at pointXY
	      */
	     public Model [ ]  getModels (
	       PointXY    pointXY,
	       Model [ ]  models,
	       Class      c ) {
	       Model [ ]  allModels = getModels ( c );

	       if ( pointXY == null )
	       {
	         return allModels;
	       }

	       NullArgumentException.check ( models );

	       double  x = pointXY.getX ( );

	       double  y = pointXY.getY ( );

	       int  index = 0;

	       for ( int  i = 0; i < allModels.length; i++ )
	       {
	         Model  model = allModels [ i ];

	         if ( model.isActive ( )
	           && model.getShape ( ).contains ( x, y ) )
	         {
	           if ( index < models.length )
	           {
	             models [ index ] = model;
	           }
	           else
	           {
	             models = ( Model [ ] ) ArrayLib.append ( models, model );
	           }

	           index++;
	         }
	       }

	       if ( index < models.length )
	       {
	         models [ index ] = null;
	       }

	       return models;
	     }

	     public Model [ ]  getModels (
	       Shape      shape,
	       Model [ ]  models,
	       Class      c )
	     //////////////////////////////////////////////////////////////////////
	     {
	       Model [ ]  allModels = getModels ( c );

	       if ( shape == null )
	       {
	         return allModels;
	       }

	       NullArgumentException.check ( models );

	       int  index = 0;

	       for ( int  i = 0; i < allModels.length; i++ )
	       {
	         Model  model = allModels [ i ];

	         if ( model.isActive ( )
	           && ShapeLib.intersects ( shape, model.getShape ( ) ) )
	         {
	           if ( index < models.length )
	           {
	             models [ index ] = model;
	           }
	           else
	           {
	             models = ( Model [ ] ) ArrayLib.append ( models, model );
	           }

	           index++;
	         }
	       }

	       if ( index < models.length )
	       {
	         models [ index ] = null;
	       }

	       return models;
	     }
	     
	     /**
	      * A Croftsoft method
	      * @param pointXY the point being measured from
	      * @param c the class being searched for
	      * @param model a model to exclude
	      * @return the nearest model to the point of class c, excluding model
	      */
	     private Model  getModelClosest (
	       PointXY  pointXY,
	       Class    c,
	       Model    model )
	     //////////////////////////////////////////////////////////////////////
	     {
	       int  index = -1;

	       double  closestDistance = Double.POSITIVE_INFINITY;

	       Model [ ]  models = ( Model [ ] ) modelArrayKeeper.getArray ( );

	       for ( int  i = 0; i < models.length; i++ )
	       {
	         Model  otherModel = models [ i ];

	         if ( ( otherModel != model )
	           && otherModel.isActive ( )
	           && c.isInstance ( otherModel ) )
	         {
	           double  distance = ShapeLib.getCenter (
	             otherModel.getShape ( ), center ).distanceXY ( pointXY );

	           if ( distance < closestDistance )
	           {
	             closestDistance = distance;

	             index = i;           
	           }
	         }
	       }

	       if ( index > -1 )
	       {
	         return models [ index ];
	       }

	       return null;
	     }
	     
	          
	     /**
	      * Returns user's lives.
	      * @return user's number of lives
	      */
	     public int getLives() {
	     	return userLives;
	     }

		/**
		 * Returns user's score.
		 * @return user's score.
		 */
		public int getScore() {
			return userScore;
		}
		
		/**
		 * Returns user's level.
		 * @return user's level.
		 */
		public int getLevel(){
			return userLevel;
		}
		
		/**
		 * pre destroyer is not null
		 * post the user's score is now userScore + deltaScore
		 * if the user is the destroyer
		 * @param deltaScore the score the user should now have
		 * @param destroyer the ship that has done something to earn points
		 */
		public void changeScore(int deltaScore, Ship destroyer){
			assert destroyer != null;
			if(destroyer==userShip && !gameOver){
				userScore+=deltaScore;
				userShip.notifyListeners();
			}
		}
		
		/**
		 * Same as above, but without the Ship parameter.
		 * Always sets new score
		 * @param deltaScore
		 */
		public void changeScore(int deltaScore){
			if(!gameOver) {
				userScore+=deltaScore;
				userShip.notifyListeners();
			}
		}
		
		/**
		 * take appropriate action to kill ship,
		 * depending on if it is controlled by computer or human
		 * @param s
		 */
		public void killShip(Ship s){
			assert s != null;
			double x = s.getX();
			double y = s.getY();
			if(s==userShip){
				userLives--;
				if(userLives<=0){
					gameOver();
				}
				resetPlayerShip();
				s.notifyListeners();
			}
			else {
				remove(s);
				createRandomComputerShip();
				changeScore(Constants.SHIP_KILL_POINTS);
			}
			maybeCreatePowerUp(x,y,Constants.POWUP_GEN_PROB_SHIP);
		}
		/**
		 * resets the human player's ship and places it 
		 * in a random, unoccupied location on screen
		 *
		 */
		private void resetPlayerShip(){
			assert userShip != null;
			userShip.reset();
			boolean failed = true;
			
			while(failed){
				double x = randGen.nextDouble() * animatedComponent.getBounds().getWidth();
				double y = randGen.nextDouble() * animatedComponent.getBounds().getHeight();
				userShip.setCenter(x,y);
				Model[] currentModels = getModels();
	     			     		
	     		failed = false;
	     		if (currentModels==null){
	     			break;
	     		}
	     		
	     		for(int i=0; i<currentModels.length; i++){
	     			if(currentModels[i]!=userShip && userShip.getShape().intersects(currentModels[i].getShape().getBounds())){
	     				failed=true;
	     				break;
	     			}
	     		}
			}	
		}
		
		/**
		 * Adds 1 to user's lives.
		 */
		public void addLife() {
			userLives++;
		}
		
		/**
		 * Ends the game.
		 */
		public void gameOver(){
			remove(userShip);
			userInfoDisplay.gameOver();
			gameOver = true;
		}

		/**
		 * Restarts an ended game.
		 */
		public void restart() {
			clear();
			initGame();
			userInfoDisplay.restart();
			gameOver = false;
		}
		
		/**
		 * Enables the user's restarting an ended game. 
		 */
		public void enableRestart() {
			userController.enableRestart(this);
		}
		
		/**
		 * returns GameWorld's random generator
		 * @return GameWorld's random generator
		 */
		public Random getRandom(){
			return randGen;
		}
    }