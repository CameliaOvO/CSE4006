/**
 * 
 */
package faceduck.ai;

import java.util.ArrayList;
import java.util.Random;

import faceduck.actors.BadgerImpl;
import faceduck.actors.Gardener;
import faceduck.actors.Grass;
import faceduck.commands.BiteCommand;
import faceduck.commands.DestroyCommand;
import faceduck.commands.MoveCommand;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Actor;
import faceduck.skeleton.interfaces.Animal;
import faceduck.skeleton.interfaces.Badger;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;
import faceduck.skeleton.util.Util;

/**
 * The AI for a Badger.
 */
public class BadgerAI extends AbstractAI implements AI {

	/**
	 * 이 AI는 자신이 볼 수 있는 영역중 파괴 가능한 구획단위로 검사하면서 Gardener가 없으면서 가장 Animal이 몰려있는 곳을 가지고
	 * 있다가 현재 자신의 위치와 동일하면 비어있는 새로운 곳을 중심점으로 파괴하고, 동일하지 않으면 가장 Animal이 몰려있는
	 * 곳(harmful)을 파괴하려 합니다.
	 * 
	 * 동서남북 중 움직일 수 있는 방향이 없고 40%의 확률로 오소리가 난폭할 때 네 방향중 랜덤한 한 곳을 깨물어 파괴합니다. 동서남북 중
	 * 움직일 수 있는 방향이 있고, 40%의 확률로 오소리가 온순할 때 움직일 수 있는 방향 중 랜덤한 한 곳으로 아무 것도 파괴하지 않고
	 * 이동합니다.
	 */
	@Override
	public Command act(World world, Actor actor) {
		// TODO Auto-generated method stub
		Command cmd = null;

		Random random = new Random();

		Location loc = world.getLocation(actor);
		int viewrange = actor.getViewRange();
		ArrayList<Location> viewing = viewingarea(world, viewrange, loc);

		ArrayList<Direction> available = abletogo(world, actor);

		boolean isGardener = false;
		Location harmful = loc;
		int max_damage = -1;
		for (Location l : viewing) {
			int cell_damage = 0;
			if (actor instanceof Badger) {
				for (int i = -((Badger) actor).getDamage(); i <= ((Badger) actor).getDamage(); i++)
					for (int j = -((Badger) actor).getDamage(); j <= ((Badger) actor).getDamage(); j++) {
						Location ruinedloc = new Location(l.getX() + i, l.getY() + j);
						if (world.isValidLocation(ruinedloc) && world.getThing(ruinedloc) != null) {
							{
								if (world.getThing(ruinedloc) instanceof Animal
										&& !(world.getThing(ruinedloc) instanceof Badger))
									cell_damage += 1;
							}
							if (world.getThing(ruinedloc) instanceof Gardener) {
								isGardener = true;
							}
						}
					}
			}

			if (cell_damage >= max_damage && !isGardener) {
				harmful = l;
				max_damage = cell_damage;
			}
		}
		if (harmful.getX() == loc.getX() && harmful.getY() == loc.getY()) {
			cmd = new DestroyCommand(Util.randomEmptyLoc(world));
		} else {
			cmd = new DestroyCommand(harmful);
		}

		if (available.isEmpty() && random.nextFloat() <= 0.4) {
			cmd = new BiteCommand(Direction.values()[random.nextInt(Direction.values().length)]);
		}

		if (!available.isEmpty() && random.nextFloat() <= 0.4) {
			cmd = new MoveCommand(available.get(random.nextInt(available.size())));
		}

		return cmd;
	}

}
