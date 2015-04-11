/*
 * Created on May 11, 2005
 *
 */

/**
 * @author 05amp
 *
 * Abstract class for AI strategies of computer ships
 * for EphSpace Game
 */
public abstract class AIStrategy {
	protected GameWorld theWorld; //more advanced strategies may require this
	
	/**
	 * constuctor initializes instance variables
	 * @param g GameWorld ships move in
	 */
	public AIStrategy(GameWorld g){
		assert g!=null;
		theWorld=g;
	}
	
	/**
	 * executes strategy and delegates to ship's operator
	 * @param o operator of ship that does actual movement and firing
	 */
	public abstract void executeStrategy(ShipOperator o);
}
