package net.tmt.common.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;

import net.tmt.Constants;
import net.tmt.client.engine.Controls;
import net.tmt.client.util.ImageLoader;
import net.tmt.client.util.ImageUtils;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.network.dtos.PlayerDTO;
import net.tmt.common.util.Vector2d;

public class PlayerEntity extends Entity {
	private static final double		accl			= 0.15;
	private static final double		deaccl			= 0.98;
	private static final int		RADIUS			= 32;
	private static final double		ROTATION_SPEED	= 0.05;
	private static final double		HALT_SPEED		= 0.1;
	private static final Controls	input			= Controls.getInstance();

	private double					speed;
	private Color					color;
	private double					rotationAngle	= 0;

	private boolean					engineMain		= false;
	private boolean					engineLeft		= false;
	private boolean					engineRight		= false;

	public PlayerEntity(final Vector2d pos, final Vector2d dir) {
		super(pos, dir);

		switch ((int) (Math.random() * 3)) {
		case 0:
			color = Color.red;
			img = ImageLoader.ship_red;
			break;
		case 1:
			color = Color.green;
			img = ImageLoader.ship_green;
			break;
		case 2:
			color = Color.blue;
			img = ImageLoader.ship_blue;
			break;
		}
	}

	@Override
	public void tick() {
		super.tick();

		double dx = Math.cos(rotationAngle) * speed;
		double dy = Math.sin(rotationAngle) * speed;

		if (rotationAngle > 2 * Math.PI)
			rotationAngle -= 2 * Math.PI;
		if (rotationAngle < 0)
			rotationAngle += 2 * Math.PI;

		dir.x = dx;
		dir.y = dy;

		speed *= deaccl;

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
		engineMain = false;
		engineRight = false;
		engineLeft = false;
		if (input.isKeyDown(KeyEvent.VK_UP) || input.isKeyDown(KeyEvent.VK_W)) {
			speed += accl;
			engineMain = true;
		}
		if (input.isKeyDown(KeyEvent.VK_DOWN) || input.isKeyDown(KeyEvent.VK_S)) {
			speed -= accl;
		}

		if (input.isKeyDown(KeyEvent.VK_LEFT) || input.isKeyDown(KeyEvent.VK_A)) {
			rotationAngle -= ROTATION_SPEED;
			speed += accl * 0.4;
			engineRight = true;
		}
		if (input.isKeyDown(KeyEvent.VK_RIGHT) || input.isKeyDown(KeyEvent.VK_D)) {
			rotationAngle += ROTATION_SPEED;
			speed += accl * 0.4;
			engineLeft = true;
		}
	}

	@Override
	public void render(final Graphics g) {
		Image drawImage = ImageUtils.rotateImage(img, Math.toDegrees(rotationAngle) + 90);
		int width = drawImage.getWidth(null);

		if (engineLeft)
			g.drawImage(ImageUtils.rotateImage(ImageLoader.ship_engine_left, Math.toDegrees(rotationAngle) + 90),
					pos.x() - width / 2, pos.y() - width / 2, null);
		if (engineRight)
			g.drawImage(ImageUtils.rotateImage(ImageLoader.ship_engine_right, Math.toDegrees(rotationAngle) + 90),
					pos.x() - width / 2, pos.y() - width / 2, null);
		if (engineMain)
			g.drawImage(ImageUtils.rotateImage(ImageLoader.ship_engine_main, Math.toDegrees(rotationAngle) + 90),
					pos.x() - width / 2, pos.y() - width / 2, null);

		g.drawImage(drawImage, pos.x() - width / 2, pos.y() - width / 2, null);

		renderDebug(g, RADIUS, 0);
	}

	@Override
	public void updateFromDTO(final EntityDTO dto) {
		super.updateFromDTO(dto);

		PlayerDTO pDTO = (PlayerDTO) dto;
		this.color = pDTO.getColor();
		this.engineLeft = pDTO.isEngineLeft();
		this.engineRight = pDTO.isEngineRight();
		this.engineMain = pDTO.isEngineMain();
		this.rotationAngle = pDTO.getRotationAngle();

		if (color == Color.green)
			img = ImageLoader.ship_green;
		if (color == Color.red)
			img = ImageLoader.ship_red;
		if (color == Color.blue)
			img = ImageLoader.ship_blue;
	}

	@Override
	public EntityDTO toDTO() {
		return new PlayerDTO(super.toDTO(), rotationAngle, color, engineMain, engineLeft, engineRight);
	}

}
