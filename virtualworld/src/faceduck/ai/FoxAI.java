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
import faceduck.skeleton.interfaces.Rabbit;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

/**
 * The AI for a Fox.
 */
public class FoxAI extends AbstractAI implements AI {

	/**
	 * constructor for FoxAI
	 */
	public FoxAI() {
	}

	/**
	 * 이 AI는 자신이 움직일 수 있는 방향, 볼 수 있는 구역, 현재 번식 가능한지, 배고픈지(BreedLimit보다 적거나 같 에너지를
	 * 가지고 있는지)를 검사합니다. 그리고 번식 가능하다면 개체수 조절을 위해 자신이 볼 수 있는 곳에 자신 외에 또 다른 여우가 있는지를
	 * 검사합니다. 또 다른 여우가 있다면 상태를 번식할 수 없음으로 바꿉니다.
	 * 
	 * 먹을 수 있는 방향에 토끼가 있는지 확인하고 배가 고프다면 그 중 하나를 잡아먹고, 그렇지 않으면 다른 여우를 위해 토끼를 남겨두고 토끼가
	 * 많은 방향을 찾아 이동합니다. 이 과정에서 번식을 할 수 있다면 토끼가 많은 방향으로 번식하고, 그렇지 않으면 자신이 그 방향으로
	 * 움직입니다.
	 * 
	 * 먹을 수 있는 방향에 토끼가 없다면 네 방향 중 한 곳으로 번식하거나 그 방향으로 이동합니다.
	 * 
	 * 이 모든 경우에 해당되지 않는다면(풀 사이에 갇히는 등의 경우) 아무 행동도 하지 않습니다.
	 */
	@Override
	public Command act(World world, Actor actor) {
		Command cmd = null;

		Random random = new Random();

		Location loc = world.getLocation(actor);
		int viewrange = actor.getViewRange();
		ArrayList<Location> viewing = viewingarea(world, viewrange, loc);

		ArrayList<Direction> available = abletogo(world, actor);

		boolean canMove = !available.isEmpty();
		ArrayList<Integer> weight = new ArrayList(Collections.nCopies(Direction.values().length, 0));
		boolean canBreed = (((Animal) actor).getBreedLimit() <= ((Animal) actor).getEnergy());
		boolean isHungry = false;
		// check breed
		if (actor instanceof Animal) {
			for (Location cell : viewing) {
				Object obj = world.getThing(cell);
				if (obj instanceof Fox) {
					if (cell.getX() == loc.getX() && cell.getY() == loc.getY()) {
					} else {
						canBreed = false;
					}
				}
			}

			if (((Animal) actor).getEnergy() <= ((Animal) actor).getBreedLimit()) {
				isHungry = true;
			}
		}

		ArrayList<Direction> checkrabbit = new ArrayList<Direction>();
		for (Direction dir : Direction.values()) {
			Location newloc = new Location(loc, dir);
			if (world.isValidLocation(newloc) && world.getThing(newloc) instanceof Rabbit) {
				checkrabbit.add(dir);
			}
		}
		if (checkrabbit.size() >= 1) {
			if (isHungry) {
				cmd = new EatCommand(checkrabbit.get(random.nextInt(checkrabbit.size())));
			} else {
				for (Location cell : viewing) {
					Object obj = world.getThing(cell);
					if (obj instanceof Rabbit) {
						if (available.contains(loc.dirTo(cell))) {
							int dist = (viewrange - loc.distanceTo(cell));

							int tmp = weight.get(loc.dirTo(cell).ordinal()) + dist * dist;
							weight.set(cell.dirTo(loc).ordinal(), tmp);
						}
					}
				}

				if (Collections.max(weight) > viewrange * viewrange) {
					int maxdir = -1;
					int maxval = -1;
					for (int i = 0; i < weight.size(); i++) {
						if (maxval < weight.get(i)) {
							maxval = weight.get(i);
							maxdir = i;
						}
					}
					Direction dir = Direction.values()[maxdir];
					if (canBreed) {
						cmd = new BreedCommand(dir);
					} else {
						cmd = new MoveCommand(dir);
					}
				}

			}

		} else if (canMove) {
			Direction dir = available.get(random.nextInt(available.size()));
			if (canBreed) {
				cmd = new BreedCommand(dir);
			} else {
				cmd = new MoveCommand(dir);
			}
		}

		return cmd;
	}
}
