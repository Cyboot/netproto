package net.tmt.common.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Map;

import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.util.Vector2d;

import org.apache.log4j.Logger;

public abstract class Entity {
	private static long		CURRENT_ENTITY_ID	= 0;
	private static long		OWNER_ID;
	protected static Logger	logger				= Logger.getLogger(Entity.class);

	protected Image			img;
	private boolean			isAlive				= true;
	protected Vector2d		pos;
	protected Vector2d		dir;

	private long			entityID;
	private long			clientId;
	private int				width				= 0;
	private int				height				= 0;

	public Entity(final Vector2d pos, final Vector2d dir) {
		this.pos = pos;
		this.dir = dir;
		clientId = OWNER_ID;
		entityID = CURRENT_ENTITY_ID++;
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
	 */
	public void tick(final Map<Long, Entity> others) {
		pos.add(dir);

		if (isOwner())
			updateTick();
	}

	/**
	 * called from tick() if the caller is also the owner.
	 */
	protected abstract void updateTick();

	public void kill() {
		isAlive = false;
	}

	protected void renderDebug(final Graphics g, final int offsetX, final int offsetY) {
		g.setColor(Color.white);
		g.drawString("#" + getEntityID(), pos.x() + offsetX, pos.y() + offsetY);
		g.setColor(Color.white);
		g.drawString("$" + getClientId() + "", pos.x() + offsetX, pos.y() + offsetY + 10);
	}


	public boolean isAlive() {
		return isAlive;
	}

	public abstract void render(Graphics g);

	public EntityDTO toDTO() {
		return new EntityDTO(entityID, clientId, System.currentTimeMillis(), pos, dir);
	}

	public void updateFromDTO(final EntityDTO dto) {
		entityID = dto.getEntityID();
		pos.set(dto.getPos());
		dir.set(dto.getDir());
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

	public static void setOWNER_ID(final long newClientID) {
		OWNER_ID = newClientID;
	}

	public static long getOWNER_ID() {
		return OWNER_ID;
	}

	public static void setCURRENT_ENTITY_ID(final long cURRENT_ENTITY_ID) {
		CURRENT_ENTITY_ID = cURRENT_ENTITY_ID;
	}

	public static long getCURRENT_ENTITY_ID() {
		return CURRENT_ENTITY_ID;
	}

	public boolean isOwner() {
		return clientId == OWNER_ID;
	}
}
