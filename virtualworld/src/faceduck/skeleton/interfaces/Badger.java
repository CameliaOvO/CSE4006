/**
 * 
 */
package faceduck.skeleton.interfaces;

import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

/**
 * @author Seonha Park
 * A Badger is an {@link Actor} which can bite, move, and/or destroy.
 */
public interface Badger extends Animal {

	/**
	 * Badger가 파괴할 수 있는 영역을 가져
	 * @return BADGER_DAMAGE
	 */
	public int getDamage();

	/**
	 * Destroy Command를 실행
	 * @param world
	 * @param loc
	 */
	public void destroy(World world, Location loc);
	
	/**
	 * Bite Command를 실행
	 * @param world
	 * @param dir
	 */
	public void bite(World world, Direction dir);
	
}
