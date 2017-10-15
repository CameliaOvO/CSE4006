package faceduck.ai;

import java.util.ArrayList;
import java.util.Random;

import faceduck.commands.MoveCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

/**
 * The AI for a Gnat. This AI will pick a random direction and then return a
 * command which moves in that direction.
 *
 * This class serves as a simple example for how other AIs should be
 * implemented.
 */
public class GnatAI extends AbstractAI implements AI {

	/*
	 * Your AI implementation must provide a public default constructor so that the
	 * it can be initialized outside of the package.
	 */
	public GnatAI() {
	}

	/*
	 * GnatAI is dumb. It disregards its surroundings and simply tells the Gnat to
	 * move in a random direction.
	 */
	@Override
	public Command act(World world, Actor actor) {
		ArrayList<Direction> available = abletogo(world, actor);
		Random random = new Random();
		Command cmd;
		Direction dir;
		if (available.size() > 0) {
			dir = available.get(random.nextInt(available.size()));
			cmd = new MoveCommand(dir);
		} else {
			cmd = null;
		}

		return cmd;
	}
}
