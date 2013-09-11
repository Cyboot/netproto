package net.tmt.common.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import net.tmt.Constants;
import net.tmt.client.engine.Controls;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.network.dtos.PlayerDTO;
import net.tmt.common.util.Vector2d;

public class PlayerEntity extends Entity {
	private static final double		accl	= 0.15;
	private static final double		deaccl	= 0.98;
	private static final int		SEIZE	= 16;
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

		if (pos.x > Constants.WIDTH - SEIZE / 2) {
			dir.x = 0;
			pos.x = Constants.WIDTH - SEIZE / 2;
		}
		if (pos.y > Constants.HEIGHT - SEIZE / 2) {
			dir.y = 0;
			pos.y = Constants.HEIGHT - SEIZE / 2;
		}
		if (pos.x < SEIZE / 2) {
			dir.x = 0;
			pos.x = SEIZE / 2;
		}
		if (pos.y < SEIZE / 2) {
			dir.y = 0;
			pos.y = SEIZE / 2;
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
		g.setColor(color);
		g.fillOval(pos.x() - SEIZE / 2, pos.y() - SEIZE / 2, SEIZE, SEIZE);

		g.setColor(Color.white);
		g.drawString(getEntityID() + "", pos.x(), pos.y());
	}

	@Override
	public EntityDTO toDTO() {
		return new PlayerDTO(super.toDTO(), color);
	}

}
