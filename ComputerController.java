
/**
 * @author Adrian Martinez, Ashok Pillai, Phil Enock
 * This class controls a ShipOperator 
 */
public class ComputerController{
	
	/** The operator that this controls */
	private ShipOperator operator;
	
	/** Strategy implemented by controller */
	private AIStrategy strategy;
	
	/**  
	 * Constructs the computer controller
	 * @param operator the operator that this controls
	 * @param strat AI strategy that will be implemented
	 */	
	public ComputerController(ShipOperator operator, AIStrategy strat){
		this.operator=operator;
		strategy = strat;
	}
	/**
	 * Code that updates the computer player. Should be executed each frame.
	 *
	 */
	public void update(){
		strategy.executeStrategy(operator);
	}
	
	/**
	 * return the ship that controller controls
	 * @return ship that controller controls
	 */
	public Ship getShip(){
		return operator.getShip();
	}
		
}