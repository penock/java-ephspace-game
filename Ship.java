import java.awt.Color;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JComponent;

import com.croftsoft.core.math.geom.Circle;


/*
 * Created on Apr 24, 2005
 *
 */

/**
 * @author Adrian Martinez, Ashok Pillai, Phil Enock
 * This class represents a ship in the LostInSpace Game
 */
public class Ship extends AbstractModel {


	/**ShipListeners of this Ship*/
	private Vector listeners;
	
	/**Components of the velocity vector */
	private double velocityY;
	private double velocityX;
	
	/** direction the ship is pointed */
	private double orientation;
	
	/**The operator controlling the ship */
	protected ShipOperator operator;
	
	/**The maximum allowed velocity */
	private double maxVelocity;
	
	/**The current weapon */
	private Weapon weapon;
	
	/**hit points*/
	private int hitPoints;
	
	/** max level of hit points*/
	private int maxHP;
	
	/**flags determining what the ship should do on calls to update */
	private boolean accelerating, turningLeft, turningRight, decelerating;
	
	/**color of bullets fired by ship*/
	private Color bulletColor;

	/**indicates whether or not this Ship is the user's ship*/
	private boolean isUserShip;

	/** The last power up this ship received */
	private PowerUp lastPowerUp;
	
	/**
	 * Constructs a new ship (animator gets set by the world)
	 * pre: world is not null, centerX and centerY are valid coordinates
	 * @param world the GameWorld the ship will be part of
	 * @param centerX x coordinate of the starting location
	 * @param centerY y coordinate of the starting location
	 * @param color the color (representing team) of the ship
	 */
	public Ship(GameWorld world, double centerX, 
				double centerY, Color color, boolean isUserShip){
		super(centerX,centerY,0,0,world);
		if(isUserShip)
			collisionShape=new Circle(x,y,Constants.USER_RADIUS_RATIO * Constants.getUserShipHeight()/2);
		else
			collisionShape=new Circle(x,y,Constants.COMPUTER_RADIUS_RATIO * Constants.getComputerShipHeight()/2);
		bulletColor=color;
		listeners = new Vector();
		this.isUserShip = isUserShip;
		reset();
	}

	/**
	 * Returns true if this is the user's ship.
	 * @return true if this is the user's ship.
	 */
	public boolean isUserShip() {
		return isUserShip;
	}
	
	/**
	 * moves the ship to the point specified
	 */
	public void setCenter(double x, double y) {
		this.x=x;
		this.y=y;
		collisionShape.setCenter(x,y);

	}


	/**
	 * Updates the ship to the next frame.  The animator will be updated by the world
	 * @param component the component the ship is painted on
	 */
	public void update(JComponent component) {
		assert component != null;
		
		//do appropriate motion
		if(accelerating){
			doAccelerate();
		}
		
		if(turningRight){
			doTurnRight();
		}
		
		if(turningLeft){
			doTurnLeft();
		}
		
		if(decelerating){
			doDecelerate();
		}

		super.update(component); // this makes the call to moveATurn
		
		checkCollisions();
		
		animator.updateOrientation((orientation+Math.PI/2)%(2*Math.PI));
		
		weapon.update();
		

	}


	/**
	 * Move according to the current velocity one frame.  If necessary, ship
	 * will "pac-man" across to the other side of the screen
	 * @param component The JComponent is necessary to get the bounds of the playing surface
	 */
	protected void moveATurn(JComponent component) {
		double width=((double)(component.getBounds().width));
		double height=((double)(component.getBounds().height));
		double radius = collisionShape.getRadius();
		
		x += velocityX;
		y += velocityY;
		
        if (x - radius > width)
        	x = -radius + 1;
        else if (x + radius < 0)
        	x = width + radius - 1;
        if (y - radius > height)
       	    y = -radius + 1;
        else if (y + radius < 0)
        	y = height + radius - 1;
        
		collisionShape.setCenter(x,y);
	}

	/**
	 * Returns the current max velocity of the ship.
	 * @return the current max velocity of the ship.
	 */
	public double getMaxVelocity() {
		return maxVelocity;
	}
	
	/**
	 * Check for collisions with asteroids, ships, bullets, and powerups
	 *
	 */
	private void checkCollisions(){

		//determine if collision with another ship occured,
		//and update heading and hitpoints if so
		Ship[] otherShips = gameWorld.getShips();
		for (int i=0; i<otherShips.length; i++){
			if (otherShips[i] == this) continue;
			if (((Circle)(otherShips[i].getShape())).intersectsCircle(collisionShape)){
				if(!otherShips[i].isUpdated()){
					reverseHeading();
					((Ship)otherShips[i]).reverseHeading();
					modifyHitPoints(Constants.SHIP_COLLISION_DAMAGE);
					((Ship)otherShips[i]).modifyHitPoints(Constants.SHIP_COLLISION_DAMAGE);
				}
			}
		}
		
		// determine if collision with asteroid occured
		// and update heading and hitpoints if so
		Asteroid[] asteroids = gameWorld.getAsteroids();
		for (int i=0; i<asteroids.length; i++){
			if (((Circle)(asteroids[i].getShape())).intersectsCircle(collisionShape)) {
				reverseHeading();
				modifyHitPoints(Constants.ASTEROID_DAMAGE);
				gameWorld.respawnAsteroid((Asteroid)(asteroids[i]));
			}
		}
		
		//determine if collision with powerup occurred
		//and execute powereup if so
		PowerUp[] powerups = gameWorld.getPowerUps();
		for (int i=0; i<powerups.length; i++){
			if (((Circle)(powerups[i].getShape())).intersectsCircle(collisionShape)) {
				((PowerUp)powerups[i]).doPowerUp(this);
				lastPowerUp = powerups[i];
				notifyListeners();

			}
		}
		
		//determine if colliosn with enemy bullet occured
		//and update hitpoints and score, and remove bullet
		Bullet[] bullets = gameWorld.getBullets();
		for (int i=0; i<bullets.length; i++){
			if (((Circle)(bullets[i].getShape())).intersectsCircle(collisionShape)) {
				if(((Bullet)bullets[i]).getShooter()!=this){
					modifyHitPoints(Constants.BULLET_DAMAGE);
					gameWorld.changeScore(Constants.HIT_SCORE_DELTA, 
							((Bullet)(bullets[i])).getShooter());
					gameWorld.remove(((Bullet)bullets[i]));
				}
			}
		}	
	}

	
	
	/**
	 * sets the ship's operator if it changes
	 * @param operator the new ship operator
	 */
	public void setShipOperator(ShipOperator operator){
		assert operator != null;
		this.operator=operator;
	}
	

	/**
	 * Changes the maximum speed of the ship
	 * @param newMax the desired new maximum velocity for the ship
	 */
	public void setMaxVelocity(double newMax){
		maxVelocity=newMax;
		if(getVelocity()>maxVelocity){
			
			//tempX prevents the new velocityX from changing
			//the value returned by getHeading()
			double tempX=maxVelocity*Math.cos(getHeading());
			velocityY=maxVelocity*Math.sin(getHeading());
			velocityX=tempX;
			
		}
	}
	
	/**
	 * 
	 * @return the speed of the ship
	 */
	public double getVelocity(){
		return Math.sqrt(Math.pow(velocityX,2)+Math.pow(velocityY,2));
	}
	
	/**
	 * @return the direction of the ship's motion
	 */
	public double getHeading(){
		if(velocityX>0){
			return Math.atan(velocityY/velocityX);
		}
		else {
			return Math.atan(velocityY/velocityX)+Math.PI;
		}
	}
	
	/**
	 * 
	 * @return the direction the ship is pointing
	 */
	public double getOrientation(){
		return orientation;
	}
	
	/**
	 * Tells the weapon to fire
	 *
	 */
	public void fire(){
		weapon.fire();
	}

	/**
	 * Tells the ship to start turning left
	 * pre: the ship should not be turning right
	 */
	public void turnLeft(){
		assert turningRight==false;
		turningLeft=true;
	}
	
	/**
	 * Tells the ship to start turning right
	 * pre: the ship should not be turning left
	 */
	public void turnRight(){
		assert turningLeft==false;
		turningRight=true;
	}
	
	/**
	 * Tells the ship to begin accelerating
	 *
	 */
	public void accelerate(){
		accelerating=true;
	}
	
	/**
	 * Tells the ship to stop accelerating
	 *
	 */
	public void stopAccelerating(){
		accelerating=false;
	}
	
	/**
	 * Tells the ship to stop turning right
	 *
	 */
	public void stopTurningRight(){
		turningRight=false;
	}
	
	/**
	 * Tells the ship to stop turning left
	 *
	 */
	public void stopTurningLeft(){
		turningLeft=false;
	}
	
	/**
	 * Tells the ship to slow down
	 *
	 */
	public void decelerate(){
		decelerating=true;
	}
	
	/**
	 * Tells the ship to stop decelerating
	 *
	 */
	public void stopDecelerating(){
		decelerating=false;
	}
	/**
	 * Changes the velocity according to the orientation
	 *
	 */
	private void doAccelerate(){
		velocityX+=Constants.SHIP_ACCELERATION*Math.cos(orientation);
		velocityY+=Constants.SHIP_ACCELERATION*Math.sin(orientation);
		if(getVelocity()>=maxVelocity){
			
			//tempX prevents the new velocityX from changing
			//the value returned by getHeading()
			double tempX=maxVelocity*Math.cos(getHeading());
			velocityY=maxVelocity*Math.sin(getHeading());
			velocityX=tempX;
		}
	}
	
	/**
	 * Changes the orientation of the ship to the right
	 *
	 */
	private void doTurnRight(){
		orientation=(orientation+Constants.SHIP_ROTATION)%(2*Math.PI);
	}

	/**
	 * Changes the orientation of the ship to the left
	 *
	 */
	private void doTurnLeft(){
		orientation=(orientation-Constants.SHIP_ROTATION)%(2*Math.PI);
	}
	
	
	/**
	 * Decelerates the ship
	 */
	private void doDecelerate() {
		if(Math.abs(velocityX)-Math.abs(Constants.SHIP_DECELERATION*Math.cos(getHeading()))>0){
			velocityX-=Constants.SHIP_DECELERATION*Math.cos(getHeading());
		}else {
			velocityX=0;
		}
		
		if(Math.abs(velocityY)-Math.abs(Constants.SHIP_DECELERATION*Math.sin(getHeading()))>0){
			velocityY-=Constants.SHIP_DECELERATION*Math.sin(getHeading());
		}else {
			velocityY=0;
		}
		
	}
	
	/**
	 * Sets the weapon to be weap
	 * pre: weap is not null
	 * @param weap the new weapon to be used
	 */
	public void setWeapon(Weapon weap){
		assert weap!=null;
		weapon=weap;
	}
	

	/**
	 * reverses current heading of ship, used for collisions
	 *
	 */
	public void reverseHeading(){
		velocityX*=-1;
		velocityY*=-1;
	}

	/**
	 * Modifies ship's hit points and notifies the listeners 
	 * @param deltaHP the change in hit points desired
	 */
	public void modifyHitPoints(int deltaHP){
		if (active) {
			hitPoints += deltaHP;
			notifyListeners();
			if (hitPoints <= 0) {
				active = false;
				gameWorld.killShip(this);
			}
		}
	}
	
	/**
	 * Returns ship's hit points.
	 * @return ship's hit points
	 */
	public int getHitPoints() {
		return hitPoints;
	}
	
	/**
	 * return maximum hit point capacity of ship
	 * @return maximum hit point capacity of ship
	 */
	public int getMaxHP() {
		return maxHP;
	}
	
	/**
	 * increments the HP capacity of ship by delta
	 * @param delta amount to increment by
	 */
	public void incrementMaxHP(int delta){
		maxHP+=delta;
	}
	
	/**
	 * Adds a listener to this Ship.
	 * @param l ShipListener to be added
	 */
	public void addListener(ShipListener l) {
		assert l!=null;
		listeners.add(l);
	}

	/**
	 * Removes a listener from this Ship.
	 * @param l ShipListener to be removed
	 */
	public void removeListener(ShipListener l) {
		listeners.remove(l);
	}
	
	/**
	 * Notifies listeners of an update to the ship's relevant state.
	 * Package visible so that world can update the display if score
	 * changes.
	 */
	void notifyListeners() {
		for(Iterator iter = listeners.iterator(); iter.hasNext(); ) {
			((ShipListener)iter.next()).update(this);
		}
	}
	
	
	
	/**
	 * @return this ship's color
	 *
	 */
	public Color getColor(){
		return bulletColor;
	}

	
	/**
	 * Resets ship's variables.
	 *
	 */
	public void reset(){
		velocityX=0;
		velocityY=0;
		orientation=0;
		maxVelocity=Constants.DEFAULT_SHIP_VELOCITY;
		weapon=new Cannon(this,gameWorld, bulletColor);
		hitPoints = Constants.DEFAULT_HIT_POINTS;
		maxHP = hitPoints;
		active=true;
	}

	/**
	 * Returns the last power up received by this ship and erases that
	 * information. Should be called only by UserInfoDisplay.
	 * @return the last power up received by this ship
	 */
	public PowerUp getLastPowerUp() {
		if(lastPowerUp != null) {
			PowerUp prevPowerUp = lastPowerUp;
			lastPowerUp = null;
			return prevPowerUp;
		}
		else
			return null;
	}
	
	/**
	 * return ship's current weapon
	 * @return ship's current weapon
	 */
	public Weapon getWeapon(){
		return weapon;
	}
	
}