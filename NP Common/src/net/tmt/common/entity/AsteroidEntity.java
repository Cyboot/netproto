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
	private static final Color	COLOR					= new Color(120, 40, 40);
	public static int			INIT_SIZE				= 20;
	private int					size					= AsteroidEntity.INIT_SIZE;

	private CountdownTimer		timerBreakNoCollision	= CountdownTimer.createManuelResetTimer(500);

	protected AsteroidEntity(final Vector2d pos, final Vector2d dir, final int size) {
		super(pos, dir);
		this.size = size;
		dir.set(new Vector2d(Math.random() - 0.5, Math.random() - 0.5));
	}

	public void setSize(final int s) {
		this.size = s;
	}

	public int getSize() {
		return this.size;
	}

	private void breakUp(final EntityHandler handler) {
		this.kill();

		// smallest asteroids don't break anymore
		if (this.size <= 2)
			return;
		// if EntityLimit is reached no more new asteroids
		if (!handler.isUnderEntityLimit())
			return;

		// FIXME: causes game to freeze
		handler.getFactory().addLater().createAsteroid(this.pos.copy(), new Vector2d(), this.size / 2, getClientId());
		handler.getFactory().addLater().createAsteroid(this.pos.copy(), new Vector2d(), this.size / 2, getClientId());

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

	}

	@Override
	public void render(final Graphics g) {
		if (!isAlive())
			return;

		g.setColor(COLOR);
		g.fillOval(pos.x() - size / 2, pos.y() - size / 2, size, size);

		// renderDebug(g, size / 2, 0);
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
	protected void updateTick(final EntityHandler caller) {
		Map<Long, Entity> others = caller.getEntityMap();

		if (pos.x < 0 || pos.x > Constants.WIDTH)
			kill();
		if (pos.y < 0 || pos.y > Constants.HEIGHT)
			kill();

		// do not check for collision if last collision was too short before
		if (!timerBreakNoCollision.isTimeleft())
			return;

		for (Entry<Long, Entity> e : others.entrySet()) {
			Entity other = e.getValue();
			/* check collision for all asteroids */
			if (other.equals(this))
				continue;
			if (this.breakableBy(other)) {
				if (pos.distance(other.getPos()) < this.size / 2 + other.getWidth() / 2) {
					logger.debug("#" + getEntityID() + " colliding with #" + other.getEntityID());
					this.breakUp(caller);
					timerBreakNoCollision.reset();
				}
			}
		}
	}
}
