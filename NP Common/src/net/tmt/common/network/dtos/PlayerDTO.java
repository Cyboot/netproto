package net.tmt.common.network.dtos;


public class PlayerDTO extends EntityDTO {
	private static final long	serialVersionUID	= 6886101631260300325L;

	private boolean				engineMain			= false;
	private boolean				engineLeft			= false;
	private boolean				engineRight			= false;
	private double				speed;
	private double				rotationAngle;
	private int					colorRBG;

	public PlayerDTO() {
	}

	public PlayerDTO(final EntityDTO dto, final double speed, final double rotationAngle, final int colorRGB,
			final boolean engineMain, final boolean engineLeft, final boolean engineRight) {
		super(dto);
		this.speed = speed;
		this.rotationAngle = rotationAngle;
		this.colorRBG = colorRGB;
		this.engineMain = engineMain;
		this.engineLeft = engineLeft;
		this.engineRight = engineRight;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(final double speed) {
		this.speed = speed;
	}

	public int getColorRBG() {
		return colorRBG;
	}

	public void setColorRBG(final int colorRBG) {
		this.colorRBG = colorRBG;
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
