package net.tmt.common.entity;

import java.awt.Graphics;

import net.tmt.common.network.EntityDTO;
import net.tmt.common.util.Vector2d;

public abstract class Entity {
	private boolean		isAlive	= true;
	protected Vector2d	pos;
	protected Vector2d	dir;

	private long		id;
	private long		clientId;

	public Entity(final Vector2d pos, final Vector2d dir) {
		this.pos = pos;
		this.dir = dir;
	}

	public void tick() {
		pos.add(dir);
	}

	public void kill() {
		isAlive = false;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public abstract void render(Graphics g);

	public EntityDTO toDTO() {
		return new EntityDTO(id, 0L, System.currentTimeMillis(), pos, dir);
	}

	public void updateFromDTO(final EntityDTO dto) {
		pos.set(dto.getPos());
		dir.set(dto.getDir());
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(final long clientId) {
		this.clientId = clientId;
	}
}
