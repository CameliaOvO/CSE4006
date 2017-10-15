package faceduck.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import faceduck.actors.Grass;
import faceduck.commands.BreedCommand;
import faceduck.commands.EatCommand;
import faceduck.commands.MoveCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Fox;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

/**
 * The AI for a Rabbit.
 * 
 */
public class RabbitAI extends AbstractAI implements AI {

	/**
	 * constructor for RabbitAI
	 */
	public RabbitAI() {
	}

	/**
	 * 이 AI는 자신이 움직일 수 있는 방향, 볼 수 있는 구역, 현재 번식 가능한지, 배고픈지(BreedLimit보다 적거나 같은 에너지를
	 * 가지고 있는지)를 검사합니다. 볼 수 있는 곳에 여우가 있는지를 검사하고, 최대한 여우의 반대 방향으로 이동하려 합니다.
	 * 
	 * 여우를 피하려고 할 때 번식할 수 있다면 자신이 가려는 방향으로 토끼를 하나 생성하고, 그렇지 않은 경우 자신이 그 자리로 이동합니다.
	 * 
	 * 여우를 피할 필요가 없을 경우 풀을 먹을 수 있는지 확인하고 먹을 수 없다면 풀을 찾아 가능한 방향으로 번식하거나 이동합니다.
	 */
	@Override
	public Command act(World world, Actor actor) {
		Command cmd = null;

		Random random = new Random();

		Location loc = world.getLocation(actor);
		int viewrange = actor.getViewRange();
		ArrayList<Location> viewing = viewingarea(world, viewrange, loc);

		ArrayList<Direction> available = abletogo(world, actor);
		ArrayList<Integer> weight = new ArrayList(Collections.nCopies(Direction.values().length, 0));
		boolean canBreed = false;
		boolean isHungry = false;
		// check breed
		if (actor instanceof Animal) {
			if (((Animal) actor).getBreedLimit() <= ((Animal) actor).getEnergy()) {
				canBreed = true;
			}
			if (((Animal) actor).getEnergy() <= ((Animal) actor).getBreedLimit()) {
				isHungry = true;
			}
		}

		for (Location cell : viewing) {
			Object obj = world.getThing(cell);
			if (obj instanceof Fox) {
				if (available.contains(cell.dirTo(loc))) {
					int dist = (viewrange - loc.distanceTo(cell));

					int tmp = weight.get(cell.dirTo(loc).ordinal()) + dist * dist;
					weight.set(cell.dirTo(loc).ordinal(), tmp);
				}
			}
		}

		if (Collections.max(weight) > viewrange * viewrange) {
			Direction dir;
			int maxdir = -1;
			int maxval = -1;
			for (int i = 0; i < weight.size(); i++) {
				if (maxval < weight.get(i)) {
					maxval = weight.get(i);
					maxdir = i;
				}
			}
			dir = Direction.values()[maxdir];

			if (canBreed) {
				cmd = new BreedCommand(dir);
			} else {
				cmd = new MoveCommand(dir);
			}
		}

		else {
			ArrayList<Direction> checkgrass = new ArrayList<Direction>();
			for (Direction dir : Direction.values()) {
				Location newloc = new Location(loc, dir);
				if (world.isValidLocation(newloc) && world.getThing(newloc) instanceof Grass) {
					checkgrass.add(dir);
				}
			}
			if (checkgrass.size() >= 1) {
				if (isHungry) {
					cmd = new EatCommand(checkgrass.get(random.nextInt(checkgrass.size())));
				} else {
				}

			} else {

				if (available.size() > 0) {
					Direction dir = available.get(random.nextInt(available.size()));
					if (canBreed) {
						cmd = new BreedCommand(dir);
					} else {
						cmd = new MoveCommand(dir);
					}
				}
			}
		}

		return cmd;

	}
}
