package net.tmt.common.network.dtos;

import java.awt.Color;

public class PlayerDTO extends EntityDTO {
	private static final long	serialVersionUID	= 6886101631260300325L;
	private Color				color;

	private boolean				engineMain			= false;
	private boolean				engineLeft			= false;
	private boolean				engineRight			= false;
	private double				rotationAngle;

	public PlayerDTO(final EntityDTO dto, final double rotationAngle, final Color color, final boolean engineMain,
			final boolean engineLeft, final boolean engineRight) {
		super(dto);
		this.color = color;
		this.engineMain = engineMain;
		this.engineLeft = engineLeft;
		this.engineRight = engineRight;
		this.rotationAngle = rotationAngle;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

	public boolean isEngineMain() {
		return engineMain;
	}

	public void setEngineMain(final boolean engineMain) {
		this.engineMain = engineMain;
	}

	public boolean isEngineLeft() {
		return engineLeft;
	}

	public void setEngineLeft(final boolean engineLeft) {
		this.engineLeft = engineLeft;
	}

	public boolean isEngineRight() {
		return engineRight;
	}

	public void setEngineRight(final boolean engineRight) {
		this.engineRight = engineRight;
	}

	public double getRotationAngle() {
		return rotationAngle;
	}

	public void setRotationAngle(final double rotationAngle) {
		this.rotationAngle = rotationAngle;
	}

}
