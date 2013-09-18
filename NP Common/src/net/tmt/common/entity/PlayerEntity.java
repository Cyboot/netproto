package net.tmt.common.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.Map;

import net.tmt.Constants;
import net.tmt.client.engine.Controls;
import net.tmt.client.util.ImageLoader;
import net.tmt.client.util.ImageUtils;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.network.dtos.PlayerDTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.Vector2d;

public class PlayerEntity extends Entity {
	private static final int		COLOR_RED			= 0;
	private static final int		COLOR_GREEN			= 1;
	private static final int		COLOR_BLUE			= 2;
	private static final int		BULLET_INTERVALL	= 250;

	private static final double		accl				= 0.05;
	private static final double		deaccl				= 0.99;
	private static final int		RADIUS				= 32;
	private static final double		ROTATION_SPEED		= 0.05;
	private static final Controls	input				= Controls.getInstance();

	private CountdownTimer			timerBullet			= CountdownTimer.createManuelResetTimer(BULLET_INTERVALL);
	private int						colorID;
	private double					speed;
	private double					rotationAngle		= Math.PI / 2;

	private boolean					engineMain			= false;
	private boolean					engineLeft			= false;
	private boolean					engineRight			= false;

	protected PlayerEntity(final Vector2d pos, final Vector2d dir) {
		super(pos, dir);

		switch ((int) (Math.random() * 3)) {
		case COLOR_RED:
			colorID = COLOR_RED;
			img = ImageLoader.ship_red;
			break;
		case COLOR_GREEN:
			colorID = COLOR_GREEN;
			img = ImageLoader.ship_green;
			break;
		case COLOR_BLUE:
			colorID = COLOR_BLUE;
			img = ImageLoader.ship_blue;
			break;
		}
	}

	@Override
	public int getWidth() {
		return PlayerEntity.RADIUS * 2;
	}

	@Override
	public int getHeight() {
		return PlayerEntity.RADIUS * 2;
	}

	@Override
	public void tick(final Map<Long, Entity> others, final EntityFactory factory) {
		super.tick(others, factory);

		double dx = Math.sin(rotationAngle) * speed;
		double dy = -Math.cos(rotationAngle) * speed;

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
	public void updateTick(final EntityFactory factory) {
		engineMain = false;
		engineRight = false;
		engineLeft = false;
		if (input.isKeyDown(KeyEvent.VK_UP) || input.isKeyDown(KeyEvent.VK_W)) {
			speed += accl;
			engineMain = true;
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
		if (timerBullet.isTimeleft() && input.isKeyDown(KeyEvent.VK_SPACE)) {
			timerBullet.reset();
			factory.addLater().createBullet(getPos().copy(), Vector2d.createByAngle(rotationAngle));
		}
	}

	@Override
	public void render(final Graphics g) {
		Image drawImage = ImageUtils.rotateImage(img, Math.toDegrees(rotationAngle));
		int width = drawImage.getWidth(null);

		if (engineLeft)
			g.drawImage(ImageUtils.rotateImage(ImageLoader.ship_engine_left, Math.toDegrees(rotationAngle)), pos.x()
					- width / 2, pos.y() - width / 2, null);
		if (engineRight)
			g.drawImage(ImageUtils.rotateImage(ImageLoader.ship_engine_right, Math.toDegrees(rotationAngle)), pos.x()
					- width / 2, pos.y() - width / 2, null);
		if (engineMain)
			g.drawImage(ImageUtils.rotateImage(ImageLoader.ship_engine_main, Math.toDegrees(rotationAngle)), pos.x()
					- width / 2, pos.y() - width / 2, null);

		g.drawImage(drawImage, pos.x() - width / 2, pos.y() - width / 2, null);

		renderDebug(g, RADIUS, 0);
	}

	@Override
	public void updateFromDTO(final EntityDTO dto) {
		super.updateFromDTO(dto);


		PlayerDTO pDTO = (PlayerDTO) dto;
		this.speed = pDTO.getSpeed();
		this.colorID = pDTO.getColorID();
		this.engineLeft = pDTO.isEngineLeft();
		this.engineRight = pDTO.isEngineRight();
		this.engineMain = pDTO.isEngineMain();
		this.rotationAngle = pDTO.getRotationAngle();

		if (colorID == COLOR_RED)
			img = ImageLoader.ship_red;
		if (colorID == COLOR_GREEN)
			img = ImageLoader.ship_green;
		if (colorID == COLOR_BLUE)
			img = ImageLoader.ship_blue;
	}

	@Override
	public EntityDTO toDTO() {
		return new PlayerDTO(super.toDTO(), speed, rotationAngle, colorID, engineMain, engineLeft, engineRight);
	}

}
