/*
 * Created on May 8, 2005
 *
 */

/**
 * @author 05pme
 * A ShipListener interface, implemented by UserInfoDisplay.
 */
public interface ShipListener {
	
	/**
	 * Updates the ShipListener with respect to the Ship's hit points.
	 * @param s ship that was updated
	 */
	public void update(Ship s);

}
