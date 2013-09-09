package net.tmt.common.network;

import net.tmt.common.util.Vector2d;

public class AsteroidDTO extends EntityDTO {
	private static final long	serialVersionUID	= 1727308302764675589L;

	public AsteroidDTO(final Vector2d pos, final Vector2d dir) {
		super(pos, dir);
	}
}
