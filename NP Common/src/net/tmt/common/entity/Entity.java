package net.tmt.common.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import net.tmt.Constants;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.util.TimeUtil;
import net.tmt.common.util.Vector2d;

import org.apache.log4j.Logger;

public abstract class Entity {
	protected static Logger	logger			= Logger.getLogger(Entity.class);

	protected Image			img;
	private int				deleteTimeleft	= -Integer.MIN_VALUE;
	protected Vector2d		pos;
	protected Vector2d		dir;

	private long			entityID;
	private long			clientId;
	private int				width			= 0;
	private int				height			= 0;

	private boolean			owned;

	protected Entity(final Vector2d pos, final Vector2d dir) {
		this.pos = pos;
		this.dir = dir;
	}

	// we could implement a separate spatiallyExtendedEntity class but ...
	// laziness
	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	/**
	 * updates the Entity, must be called from server and client regardless of
	 * the owner! it will call updateTick() if the caller is the owner of the
	 * entity (additional computations can take place there)
	 * 
	 * since tick has to be able to be called from both the client's Game class
	 * as well as the servers GameLoop class therefore the ugly parameter —
	 * sorry ^^
	 * 
	 * @param entitesToCreate
	 *            TODO
	 * 
	 */
	public void tick(final EntityHandler caller) {
		if (isAlive()) {
			pos.add(dir);

			if (isOwner())
				updateTick(caller.getFactory());
		} else {
			deleteTimeleft -= Constants.DELTA_TARGET;
		}

	}

	/**
	 * called from tick() if the caller is also the owner.
	 */
	protected abstract void updateTick(final EntityFactory factory);

	public void kill() {
		deleteTimeleft = 1000;
	}

	public void setDeleteTimeleft(final int deleteTimeleft) {
		this.deleteTimeleft = deleteTimeleft;
	}

	protected void renderDebug(final Graphics g, final int offsetX, final int offsetY) {
		g.setColor(Color.white);
		g.drawString("#" + getEntityID(), pos.x() + offsetX, pos.y() + offsetY);
		g.setColor(Color.white);
		g.drawString("$" + getClientId() + "", pos.x() + offsetX, pos.y() + offsetY + 10);
	}

	public boolean isDeleted() {
		return deleteTimeleft < 0 && deleteTimeleft != Integer.MIN_VALUE;
	}

	public boolean isAlive() {
		return deleteTimeleft == Integer.MIN_VALUE;
	}

	public abstract void render(Graphics g);

	public EntityDTO toDTO() {
		return new EntityDTO(entityID, clientId, TimeUtil.getSynchroTimestamp(), pos, dir, deleteTimeleft);
	}

	public void updateFromDTO(final EntityDTO dto) {
		entityID = dto.getEntityID();
		pos.set(dto.getPos());
		dir.set(dto.getDir());
		deleteTimeleft = dto.getDeleteTimeleft();
	}

	public long getEntityID() {
		return entityID;
	}

	public void setEntityID(final long entityID) {
		this.entityID = entityID;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(final long clientId) {
		this.clientId = clientId;
	}

	public Vector2d getPos() {
		return pos;
	}

	public boolean isOwner() {
		return owned;
	}

	protected void setOwned(final boolean b) {
		owned = b;
	}
}
