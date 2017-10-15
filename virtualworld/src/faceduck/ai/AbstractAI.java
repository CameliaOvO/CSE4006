package faceduck.ai;

import java.util.ArrayList;

import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

public abstract class AbstractAI implements AI {

	/**
	 * constructor for AbstractAI
	 */
	public AbstractAI() {

	}

	/**
	 * abstract act command
	 */
	public abstract Command act(World world, Actor actor);

	/**
	 * 
	 * @param world
	 * @param actor
	 * @return ArrayList of Direction that actor can move in world
	 */
	public ArrayList<Direction> abletogo(World world, Actor actor) {
		ArrayList<Direction> available = new ArrayList<Direction>();

		Location loc = world.getLocation(actor);
		for (Direction dir : Direction.values()) {
			Location newloc = new Location(loc, dir);
			if (world.isValidLocation(newloc) && world.getThing(newloc) == null) {
				available.add(dir);
			}
		}
		return available;
	}

	/**
	 * 
	 * @param world
	 * @param viewrange
	 *            range which can be viewed at loc
	 * @param loc
	 *            location of middle point
	 * @return ArrayList of Location which is valod and can be viewed at loc.
	 */
	public ArrayList<Location> viewingarea(World world, int viewrange, Location loc) {
		ArrayList<Location> viewing = new ArrayList<Location>();
		for (int i = -viewrange; i <= viewrange; i++) {
			for (int j = -viewrange; j <= viewrange; j++) {
				Location viewloc = new Location(loc.getX() + i, loc.getY() + j);
				if (world.isValidLocation(viewloc)) {
					viewing.add(viewloc);
				}
			}
		}
		return viewing;

	}
}
