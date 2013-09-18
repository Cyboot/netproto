package net.tmt.common.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Map;
import java.util.Map.Entry;

import net.tmt.Constants;
import net.tmt.common.network.dtos.AsteroidDTO;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.Vector2d;

public class AsteroidEntity extends Entity {
	public static int		INIT_SIZE		= 20;
	private int				size			= AsteroidEntity.INIT_SIZE;
	private CountdownTimer	timerDirChange	= new CountdownTimer(5000, 0);

	protected AsteroidEntity(final Vector2d pos, final Vector2d dir, final int size) {
		super(pos, dir);
	}

	public void setSize(final int s) {
		this.size = s;
	}

	public int getSize() {
		return this.size;
	}

	private void breakUp(final EntityHandler handler) {

		// FIXME: causes game to freeze
		//
		// AsteroidEntity part1 = handler.getFactory().addLater()
		// .createAsteroid(this.pos, new Vector2d(), this.size / 2,
		// Constants.SERVER_ID);
		// AsteroidEntity part2 = handler.getFactory().addLater()
		// .createAsteroid(this.pos, new Vector2d(), this.size / 2,
		// Constants.SERVER_ID);

		// FIXME: causes a ConcurrentModificationException
		//
		// AsteroidEntity part1 = handler.getFactory().createAsteroid(this.pos,
		// new Vector2d(), this.size / 2,
		// Constants.SERVER_ID);
		// AsteroidEntity part2 = handler.getFactory().createAsteroid(this.pos,
		// new Vector2d(), this.size / 2,
		// Constants.SERVER_ID);
		// handler.getEntityMap().put(part1.getEntityID(), part1);
		// handler.getEntityMap().put(part2.getEntityID(), part2);

		this.kill();
	}

	@Override
	public int getWidth() {
		return this.size;
	}

	@Override
	public int getHeight() {
		return this.size;
	}


	@Override
	public void kill() {
		super.kill();
		// TODO: spawn (two?) new, smaller asteroids
	}

	public boolean breakableBy(final Entity e) {
		if (e instanceof AsteroidEntity || e instanceof PlayerEntity || e instanceof BulletEntity)
			return true;
		return false;
	}

	@Override
	public void tick(final EntityHandler caller) {
		super.tick(caller);
		Map<Long, Entity> others = caller.getEntityMap();

		if (pos.x < 0 || pos.x > Constants.WIDTH)
			kill();
		if (pos.y < 0 || pos.y > Constants.HEIGHT)
			kill();

		for (Entry<Long, Entity> e : others.entrySet()) {
			Entity other = e.getValue();
			/* check collision for all asteroids */
			if (other.equals(this))
				continue;
			if (this.breakableBy(other)) {
				if (Math.abs(pos.x + this.size / 2) > Math.abs(other.getPos().x - other.getWidth() / 2)
						&& Math.abs(pos.x - this.size / 2) < Math.abs(other.getPos().x + other.getWidth() / 2)
						&& Math.abs(pos.y) + this.size / 2 > Math.abs(other.getPos().y - other.getHeight() / 2)
						&& Math.abs(pos.y - this.size / 2) < Math.abs(other.getPos().y) + other.getHeight() / 2) {
					this.breakUp(caller.getSelf());
				}
			}
		}

	}

	@Override
	public void render(final Graphics g) {
		g.setColor(Color.yellow);
		g.fillRect(pos.x() - size / 2, pos.y() - size / 2, size, size);

		renderDebug(g, size / 2, 0);
	}

	@Override
	public EntityDTO toDTO() {
		return new AsteroidDTO(super.toDTO(), this.size);
	}

	@Override
	public void updateFromDTO(final EntityDTO dto) {
		super.updateFromDTO(dto);
		this.size = ((AsteroidDTO) dto).getSize();
	}

	@Override
	protected void updateTick(final EntityFactory factory) {
		if (timerDirChange.isTimeleft())
			dir.set(new Vector2d(Math.random() - 0.5, Math.random() - 0.5));
	}
}
