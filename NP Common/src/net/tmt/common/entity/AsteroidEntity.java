package net.tmt.common.entity;

import java.awt.Color;
import java.awt.Graphics;

import net.tmt.Constants;
import net.tmt.common.network.dtos.AsteroidDTO;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.Vector2d;

public class AsteroidEntity extends Entity {
	public static int		INIT_SIZE		= 20;
	private int				size			= AsteroidEntity.INIT_SIZE;
	private CountdownTimer	timerDirChange	= new CountdownTimer(5000, 0);

	public void halfSize() {
		this.size -= 1;
		if (this.size == 0)
			this.kill();
	}

	public void setSize(final int s) {
		this.size = s;
	}

	public int getSize() {
		return this.size;
	}

	@Override
	public void kill() {
		super.kill();
		// TODO: spawn (two?) new, smaller asteroids
	}

	public boolean breakableBy(final Entity e) {
		if (e instanceof AsteroidEntity || e instanceof PlayerEntity)
			return true;
		return false;
	}

	public AsteroidEntity(final Vector2d pos, final Vector2d dir, final int size) {
		super(pos, dir);
	}

	@Override
	public void tick() {
		super.tick();

		if (pos.x < 0 || pos.x > Constants.WIDTH)
			kill();
		if (pos.y < 0 || pos.y > Constants.HEIGHT)
			kill();
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
	protected void updateTick() {
		if (timerDirChange.isTimeleft())
			dir.set(new Vector2d(Math.random() - 0.5, Math.random() - 0.5));
	}
}
