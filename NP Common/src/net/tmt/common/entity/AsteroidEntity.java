package net.tmt.common.entity;

import java.awt.Color;
import java.awt.Graphics;

import net.tmt.Constants;
import net.tmt.common.network.AsteroidDTO;
import net.tmt.common.network.DTO;
import net.tmt.common.util.Vector2d;

public class AsteroidEntity extends Entity {

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
		g.fillRect(pos.x(), pos.y(), 10, 10);
	}

	@Override
	public DTO toDTO() {
		return new AsteroidDTO(pos, dir);
	}
}
