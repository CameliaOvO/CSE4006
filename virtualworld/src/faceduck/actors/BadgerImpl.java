/**
 * 
 */
package faceduck.actors;

import java.util.ArrayList;

import faceduck.ai.BadgerAI;
import faceduck.ai.GnatAI;
import faceduck.skeleton.interfaces.Badger;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

/**
 * @author Seonha Park
 * Implement class for Badger
 *
 */
public class BadgerImpl implements Badger {

	private static final int BADGER_MAX_ENERGY = Integer.MAX_VALUE;
	private static final int BADGER_VIEW_RANGE = 7;
	private static final int BADGER_BREED_LIMIT = 0;
	private static final int BADGER_COOL_DOWN = 16;
	private static final int BADGER_DAMAGE = 2;

	/**
	 * @return 오소리가 한번에 파괴할 수 있는 크기를 반환합니다.
	 */
	@Override
	public int getDamage() {
		return BADGER_DAMAGE;
	}

	@Override
	public int getEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @return 오소리의 최대 체력을 반환합니다. 오소리는 죽지 않기 때문에 정수 최대값으로 설정했습니다.
	 */
	@Override
	public int getMaxEnergy() {
		// TODO Auto-generated method stub
		return BADGER_MAX_ENERGY;
	}

	/**
	 * @return 오소리의 번식을 위한 최소 에너지를 반환합니다. 번식하지 않습니다.
	 */
	@Override
	public int getBreedLimit() {
		// TODO Auto-generated method stub
		return BADGER_BREED_LIMIT;
	}


	@Override
	public void eat(World world, Direction dir) {
		// TODO Auto-generated method stub

	}


	/**
	 * Move to dir in world.
	 */
	@Override
	public void move(World world, Direction dir) {
		// TODO Auto-generated method stub
		Location newloc = new Location(world.getLocation(this), dir);
		world.remove(this);
		world.add(this, newloc);

	}


	@Override
	public void breed(World world, Direction dir) {
		// TODO Auto-generated method stub

	}

	/**
	 * call AI and act in world.
	 */
	@Override
	public void act(World world) {
		// TODO Auto-generated method stub
		if (world == null) {
			throw new NullPointerException("World must not be null.");
		}

		BadgerAI badger_ai = new BadgerAI();
		Command cmd = badger_ai.act(world, this);

		if (cmd != null) {
			cmd.execute(world, this);
		}

	}

	/**
	 * @return 오소리가 한번에 볼 수 있는 크기를 반환합니다.
	 */
	@Override
	public int getViewRange() {
		// TODO Auto-generated method stub
		return BADGER_VIEW_RANGE;
	}

	/**
	 * @return 오소리의 쿨다운 시간을 반환합니다.
	 */
	@Override
	public int getCoolDown() {
		// TODO Auto-generated method stub
		return BADGER_COOL_DOWN;
	}


/**
 * loc을 중심으로 BADGER_DAMAGE 만큼의 구역을 파괴합니다. 단 자기 자신 혹은 정원사는 살려둡니다.
 */
	@Override
	public void destroy(World world, Location loc) {
		// TODO Auto-generated method stub
		ArrayList<Location> ruins = new ArrayList<Location>();
		for (int i = -BADGER_DAMAGE; i <= BADGER_DAMAGE; i++)
			for (int j = -BADGER_DAMAGE; j <= BADGER_DAMAGE; j++) {
				Location ruinedloc = new Location(loc.getX() + i, loc.getY() + j);
				if (world.isValidLocation(ruinedloc) && world.getThing(ruinedloc) != null)
					ruins.add(ruinedloc);
			}
		for (Location dest : ruins) {
			if (world.getThing(dest) instanceof Badger) {
				continue;
			}
			world.remove(world.getThing(dest));
		}
		world.remove(this);
		world.add(this, loc);

	}

	/**
	 * dir 방향에 있는 물체를 파괴합니다.
	 */
	@Override
	public void bite(World world, Direction dir) {
		// TODO Auto-generated method stub
		Location biteloc = new Location(world.getLocation(this), dir);
		world.remove(biteloc);

	}

}
