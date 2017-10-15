/**
 * 
 */
package faceduck.commands;

import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Badger;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;

/**
 * This command will cause the {@link Badger} to bite.
 * 
 */
public class BiteCommand implements Command {

	private Direction dir;

	/**
	 * Instantiates an bite command to be executed in the given direction.
	 *
	 * @param dir
	 *            The direction in which the command will be performed.
	 *
	 * @throws NullPointerException
	 *             If direction is null.
	 */
	public BiteCommand(Direction dir) {
		if (dir == null) {
			throw new NullPointerException("Direction cannot be null.");
		}
		this.dir = dir;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException
	 *             If actor is not an instance of Badger.
	 */
	@Override
	public void execute(World world, Actor actor) {
		if (actor == null) {
			throw new NullPointerException("Actor cannot be null");
		} else if (world == null) {
			throw new NullPointerException("World cannot be null");
		} else if (!(actor instanceof Badger)) {
			throw new IllegalArgumentException(
					"actor must be an instance of Badger.");
		}
		((Badger) actor).bite(world, dir);
	}

}
