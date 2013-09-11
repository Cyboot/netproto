package net.tmt.common.network;

import java.awt.Color;

public class PlayerDTO extends EntityDTO {
	private static final long	serialVersionUID	= 6886101631260300325L;
	private Color				color;

	public PlayerDTO(final EntityDTO dto, final Color color) {
		super(dto);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(final Color color) {
		this.color = color;
	}

}
