package net.tmt.common.network;

import net.tmt.common.util.Vector2d;

public abstract class EntityDTO extends DTO {
	private static final long	serialVersionUID	= 2890375851920871553L;

	private Vector2d			pos;
	private Vector2d			dir;


	public EntityDTO(final Vector2d pos, final Vector2d dir) {
		super();
		this.pos = pos;
		this.dir = dir;
	}

	public Vector2d getPos() {
		return pos;
	}

	public void setPos(final Vector2d pos) {
		this.pos = pos;
	}

	public Vector2d getDir() {
		return dir;
	}

	public void setDir(final Vector2d dir) {
		this.dir = dir;
	}
}
