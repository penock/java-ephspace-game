

/**
 * @author Adrian Martinez, Ashok Pillai, Phil Enock
 *	This class simply passes the commands on to the ship. These calls are not 
 * made directly in order to ensure that no ship can do anything that the user 
 * cannot do; this basically acts as a list of allowable commands on the ship.
 */
public class ShipOperator {
	//The ship being controlled
	protected Ship ship;
	
	/**
	 * Sets the ship that this controls
	 * @param ship the new ship to control
	 */
	public void setShip(Ship ship){
		this.ship=ship;
	}
	
	/**
	 * returns ship that operator controls
	 * @return ship that operator controls
	 */
	public Ship getShip(){
		return ship;
	}
	
	//ship command methods
	public void accelerate(){
		ship.accelerate();
	}
	
	public void turnRight(){
		ship.turnRight();
	}
	
	public void turnLeft(){
		ship.turnLeft();
	}
	
	public void fire(){
		ship.fire();
	}
	
	public void stopAccelerating(){
		ship.stopAccelerating();
	}
	
	public void stopTurningRight(){
		ship.stopTurningRight();
	}
	
	public void stopTurningLeft(){
		ship.stopTurningLeft();
	}
	
	public void decelerate(){
		ship.decelerate();
	}
	
	public void stopDecelerating(){
		ship.stopDecelerating();
	}
}
