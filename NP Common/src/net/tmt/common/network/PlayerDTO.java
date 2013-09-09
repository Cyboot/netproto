package net.tmt.common.network;

import java.awt.Color;

import net.tmt.common.util.Vector2d;

public class PlayerDTO extends EntityDTO {
	private static final long	serialVersionUID	= 6886101631260300325L;
	private Color				color;

	public PlayerDTO(final Vector2d pos, final Vector2d dir, final Color color) {
		super(pos, dir);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

}
