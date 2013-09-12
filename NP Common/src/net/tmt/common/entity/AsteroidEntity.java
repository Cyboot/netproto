package net.tmt.common.entity;

import java.awt.Color;
import java.awt.Graphics;

import net.tmt.Constants;
import net.tmt.common.network.dtos.AsteroidDTO;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.Vector2d;

public class AsteroidEntity extends Entity {
	private static final int	SEIZE			= 10;
	private CountdownTimer		timerDirChange	= new CountdownTimer(5000, 0);

	public AsteroidEntity(final Vector2d pos, final Vector2d dir) {
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
		g.fillRect(pos.x() - SEIZE / 2, pos.y() - SEIZE / 2, SEIZE, SEIZE);

		renderDebug(g, SEIZE / 2, 0);
	}

	@Override
	public EntityDTO toDTO() {
		return new AsteroidDTO(super.toDTO());
	}

	@Override
	protected void updateTick() {
		if (timerDirChange.isTimeleft())
			dir.set(new Vector2d(Math.random() - 0.5, Math.random() - 0.5));
	}
}
