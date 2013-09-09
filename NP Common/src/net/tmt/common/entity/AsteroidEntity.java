package net.tmt.common.entity;

import java.awt.Graphics;

import net.tmt.common.network.AsteroidDTO;
import net.tmt.common.network.DTO;
import net.tmt.common.util.Vector2d;

public class AsteroidEntity extends Entity {

	public AsteroidEntity(final Vector2d pos, final Vector2d dir) {
		super(pos, dir);
	}

	@Override
	public void render(final Graphics g) {
		g.drawRect(pos.x(), pos.y(), 10, 10);
	}

	@Override
	public DTO toDTO() {
		return new AsteroidDTO(pos, dir);
	}

}
