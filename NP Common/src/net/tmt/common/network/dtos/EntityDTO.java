package net.tmt.common.network.dtos;

import net.tmt.common.util.Vector2d;

public class EntityDTO extends DTO {
	private static final long	serialVersionUID	= 2890375851920871553L;

	private int					deleteTimeleft;

	private long				entityID;
	private Vector2d			pos;
	private Vector2d			dir;

	public EntityDTO() {
	}

	public EntityDTO(final long entityID, final long clientId, final long timestamp, final Vector2d pos,
			final Vector2d dir, final int deleteTimeleft) {
		super(clientId, timestamp);
		this.entityID = entityID;
		this.pos = pos;
		this.dir = dir;
		this.deleteTimeleft = deleteTimeleft;
	}

	public EntityDTO(final EntityDTO dto, final Vector2d pos, final Vector2d dir) {
		this(dto.getEntityID(), dto.getClientId(), dto.getTimestamp(), pos, dir, -Integer.MIN_VALUE);
	}

	public EntityDTO(final EntityDTO dto) {
		this(dto.getEntityID(), dto.getClientId(), dto.getTimestamp(), dto.getPos(), dto.getDir(), dto
				.getDeleteTimeleft());
	}

	public int getDeleteTimeleft() {
		return deleteTimeleft;
	}

	public void setDeleteTimeleft(final int deleteTimeleft) {
		this.deleteTimeleft = deleteTimeleft;
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

	public long getEntityID() {
		return entityID;
	}

	public void setEntityID(final long entityID) {
		this.entityID = entityID;
	}

	@Override
	public String toString() {
		return super.toString() + " ent#=" + entityID + " pos=" + pos;
	}
}
