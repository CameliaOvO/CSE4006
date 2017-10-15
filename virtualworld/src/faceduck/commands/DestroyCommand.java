/**
 * 
 */
package faceduck.commands;

import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Badger;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Location;

/**
 * This command will cause the {@link Badger} to destroy some location.
 * 
 */
public class DestroyCommand implements Command {

	private Location loc;
	
	/**
	 * Instantiates an destroy command to be executed in the given location.
	 *
	 * @param loc
	 *            The middle point in which the command will be performed.
	 *
	 * @throws NullPointerException
	 *             If location is null.
	 */
	public DestroyCommand(Location loc) {
		if (loc == null) {
			throw new NullPointerException("Location cannot be null.");
		}
		this.loc = loc;
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * {@inheritDoc}
	 *
	 * @throws IllegalArgumentException
	 *             If actor is not an instance of Badger.
	 */
	@Override
	public void execute(World world, Actor actor) {
		// TODO Auto-generated method stub
		if (actor == null) {
			throw new NullPointerException("Actor cannot be null");
		} else if (world == null) {
			throw new NullPointerException("World cannot be null");
		} else if (!(actor instanceof Badger)) {
			throw new IllegalArgumentException(
					"actor must be an instance of Badger.");
		}
		((Badger) actor).destroy(world, loc);


	}

}
