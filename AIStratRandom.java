import java.util.Random;

/*
 * Created on May 12, 2005
*/

/**
 * @author 05amp
 *
 * AI Strategy that is randomly turning and firing at random intervals
 *
 */
public class AIStratRandom extends AIStrategy {

	/** The counter of frames that helps determine random actions */
	private int counter;
	

	/** generates the random actions for the computer */
	private Random r;
	
	
	/**
	 * constructor calls superconstructor
	 * @param g world that ship is in
	 */
	public AIStratRandom(GameWorld g){
		super(g);
		r=g.getRandom();
	}

	/**
	 * executes random movement and firing and delegates to operator 
	 *@param operator operator of ship which does actual moving and firing
	 */
	public void executeStrategy(ShipOperator operator) {
		assert operator!=null;
		if(r.nextInt(30)==0){
			operator.fire();
		}
		if(counter==0){
		
			operator.stopTurningRight();
			operator.stopTurningLeft();
			operator.stopAccelerating();
		

			int rand=r.nextInt(3);
			if(rand==2){
				operator.turnRight();
			}else if (rand==1){
				operator.turnLeft();
			}else if(rand==0){
				operator.accelerate();
			}

		}
		
		counter=(counter+1)%15; 
	
	}
	
	
}
