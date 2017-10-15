package faceduck.actors;

import faceduck.ai.FoxAI;
import faceduck.ai.RabbitAI;
import faceduck.skeleton.interfaces.AI;
import faceduck.skeleton.interfaces.Command;
import faceduck.skeleton.interfaces.Edible;
import faceduck.skeleton.interfaces.Fox;
import faceduck.skeleton.interfaces.Rabbit;
import faceduck.skeleton.interfaces.World;
import faceduck.skeleton.util.Direction;
import faceduck.skeleton.util.Location;

public class FoxImpl implements Fox {
	private static final int FOX_MAX_ENERGY = 160;
	private static final int FOX_VIEW_RANGE = 5;
	private static final int FOX_BREED_LIMIT = FOX_MAX_ENERGY * 3 / 4;
	private static final int FOX_COOL_DOWN = 2;
	private static final int FOX_INITAL_ENERGY = FOX_MAX_ENERGY * 1 / 2;

	private int energy = FOX_INITAL_ENERGY;

	public FoxImpl(int initenergy) {
		// TODO Auto-generated constructor stub
		this.energy = initenergy;
	}

	public FoxImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getEnergy() {
		// TODO Auto-generated method stub
		return energy;
	}

	@Override
	public int getMaxEnergy() {
		// TODO Auto-generated method stub
		return FOX_MAX_ENERGY;
	}

	@Override
	public int getBreedLimit() {
		// TODO Auto-generated method stub
		return FOX_BREED_LIMIT;
	}

	/**
	 * eat rabbit in dir.
	 */
	@Override
	public void eat(World world, Direction dir) {
		Location eatloc = new Location(world.getLocation(this), dir);
		int consumed = ((Edible) world.getThing(eatloc)).getEnergyValue();
		world.remove(world.getThing(eatloc));
		energy += consumed;
		if (energy > FOX_MAX_ENERGY) {
			energy = FOX_MAX_ENERGY;
		}

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

	/**
	 * Breed to dir in world.
	 */
	@Override
	public void breed(World world, Direction dir) {
		Location breedloc = new Location(world.getLocation(this), dir);
		Fox child = new FoxImpl(this.getEnergy() / 2);
		this.energy = this.getEnergy() / 2;
		world.add(child, breedloc);
	}

	@Override
	public void act(World world) {
		// TODO Auto-generated method stub
		if (world == null) {
			throw new NullPointerException("World must not be null.");
		}
		FoxAI fox_ai = new FoxAI();
		Command cmd = fox_ai.act(world, this);
		if (cmd != null) {
			cmd.execute(world, this);
		}
		energy -= 1;

		if (energy <= 0) {
			world.remove(this);
		}

	}

	@Override
	public int getViewRange() {
		// TODO Auto-generated method stub
		return FOX_VIEW_RANGE;
	}

	@Override
	public int getCoolDown() {
		// TODO Auto-generated method stub
		return FOX_COOL_DOWN;
	}

}
