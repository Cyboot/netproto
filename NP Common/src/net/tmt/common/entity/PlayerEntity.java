package net.tmt.common.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import net.tmt.Constants;
import net.tmt.client.engine.Controls;
import net.tmt.client.util.ImageLoader;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.network.dtos.PlayerDTO;
import net.tmt.common.util.Vector2d;

public class PlayerEntity extends Entity {
	private static final double		accl	= 0.15;
	private static final double		deaccl	= 0.98;
	private static final int		RADIUS	= 16;
	private static final Controls	input	= Controls.getInstance();

	private Color					color;

	public PlayerEntity(final Vector2d pos, final Vector2d dir) {
		super(pos, dir);
		color = Color.green;
	}

	@Override
	public void tick() {
		super.tick();

		dir.x *= deaccl;
		dir.y *= deaccl;

		if (pos.x > Constants.WIDTH - RADIUS) {
			dir.x = 0;
			pos.x = Constants.WIDTH - RADIUS;
		}
		if (pos.y > Constants.HEIGHT - RADIUS) {
			dir.y = 0;
			pos.y = Constants.HEIGHT - RADIUS;
		}
		if (pos.x < RADIUS) {
			dir.x = 0;
			pos.x = RADIUS;
		}
		if (pos.y < RADIUS) {
			dir.y = 0;
			pos.y = RADIUS;
		}
	}

	@Override
	public void updateTick() {
		if (input.isKeyDown(KeyEvent.VK_UP) || input.isKeyDown(KeyEvent.VK_W)) {
			dir.y += -accl;
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN) || input.isKeyDown(KeyEvent.VK_S)) {
			dir.y += accl;
		}
		if (input.isKeyDown(KeyEvent.VK_LEFT) || input.isKeyDown(KeyEvent.VK_A)) {
			dir.x += -accl;
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT) || input.isKeyDown(KeyEvent.VK_D)) {
			dir.x += accl;
		}
	}

	@Override
	public void render(final Graphics g) {
		g.drawImage(ImageLoader.point_red, pos.x() - RADIUS, pos.y() - RADIUS, null);

		renderDebug(g, RADIUS, 0);
	}

	@Override
	public EntityDTO toDTO() {
		return new PlayerDTO(super.toDTO(), color);
	}

}
