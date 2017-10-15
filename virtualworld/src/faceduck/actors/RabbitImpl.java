package faceduck.actors;

import faceduck.ai.RabbitAI;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Edible;
import faceduck.skeleton.interfaces.Rabbit;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

public class RabbitImpl implements Rabbit {
	private static final int RABBIT_MAX_ENERGY = 20;
	private static final int RABBIT_VIEW_RANGE = 3;
	private static final int RABBIT_BREED_LIMIT = RABBIT_MAX_ENERGY * 2 / 4;
	private static final int RABBIT_ENERGY_VALUE = 20;
	private static final int RABBIT_COOL_DOWN = 4;
	private static final int RABBIT_INITAL_ENERGY = RABBIT_MAX_ENERGY * 1 / 2;

	private int energy = RABBIT_INITAL_ENERGY;

	/**
	 * 
	 * @param initenergy
	 *            breed를 통해 태어날 토끼의 에너지
	 */
	public RabbitImpl(int initenergy) {
		// TODO Auto-generated constructor stub
		this.energy = initenergy;
	}

	public RabbitImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getEnergy() {
		// TODO Auto-generated method stub
		return this.energy;
	}

	@Override
	public int getMaxEnergy() {
		// TODO Auto-generated method stub
		return RABBIT_MAX_ENERGY;
	}

	@Override
	public int getBreedLimit() {
		// TODO Auto-generated method stub
		return RABBIT_BREED_LIMIT;
	}

	/**
	 * Eat grass in dir
	 */
	@Override
	public void eat(World world, Direction dir) {
		Location eatloc = new Location(world.getLocation(this), dir);
		int consumed = ((Edible) world.getThing(eatloc)).getEnergyValue();
		world.remove(world.getThing(eatloc));
		energy += consumed;
		if (energy > RABBIT_MAX_ENERGY) {
			energy = RABBIT_MAX_ENERGY;
		}

	}

	/**
	 * Move to dir in world
	 */
	@Override
	public void move(World world, Direction dir) {
		// TODO Auto-generated method stub
		Location newloc = new Location(world.getLocation(this), dir);
		world.remove(this);
		world.add(this, newloc);
	}

	/**
	 * breed on dir in world
	 */
	@Override
	public void breed(World world, Direction dir) {
		// TODO Auto-generated method stub
		Location breedloc = new Location(world.getLocation(this), dir);
		Rabbit child = new RabbitImpl(this.getEnergy() / 2);
		this.energy = this.getEnergy() / 2;
		world.add(child, breedloc);
	}

	@Override
	public void act(World world) {
		// TODO Auto-generated method stub
		if (world == null) {
			throw new NullPointerException("World must not be null.");
		}
		RabbitAI rabbit_ai = new RabbitAI();
		Command cmd = rabbit_ai.act(world, this);
		if (cmd != null)
			cmd.execute(world, this);
		energy -= 1;

		if (energy <= 0) {
			world.remove(this);
		}
	}

	@Override
	public int getViewRange() {
		// TODO Auto-generated method stub
		return RABBIT_VIEW_RANGE;
	}

	@Override
	public int getCoolDown() {
		// TODO Auto-generated method stub
		return RABBIT_COOL_DOWN;
	}

	@Override
	public int getEnergyValue() {
		// TODO Auto-generated method stub
		return RABBIT_ENERGY_VALUE;
	}

}
