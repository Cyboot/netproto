package net.tmt.common.entity;

import java.awt.Color;
import java.awt.Graphics;

import net.tmt.common.network.dtos.BulletDTO;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.util.Vector2d;

public class BulletEntity extends Entity {
	private static final int	RADIUS	= 5;
	private static final double	SPEED	= 7.5;

	protected BulletEntity(final Vector2d pos, final Vector2d dir) {
		super(pos, dir);

		dir.multiply(SPEED);
	}

	@Override
	public int getWidth() {
		return BulletEntity.RADIUS * 2;
	}

	@Override
	public int getHeight() {
		return BulletEntity.RADIUS * 2;
	}

	@Override
	protected void updateTick(final EntityFactory factory) {
	}

	@Override
	public void render(final Graphics g) {
		g.setColor(Color.orange);
		g.fillOval(pos.x() - RADIUS, pos.y() - RADIUS, RADIUS * 2, RADIUS * 2);

		renderDebug(g, RADIUS, RADIUS);
	}

	@Override
	public EntityDTO toDTO() {
		return new BulletDTO(super.toDTO());
	}
}
