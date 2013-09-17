package net.tmt.common.entity;

import net.tmt.common.util.Vector2d;

public class EntityFactory {
	private static EntityFactory	server	= new EntityFactory();
	private static EntityFactory	client	= new EntityFactory();

	private long					ownerId;
	private long					currentEntityId;

	private EntityFactory() {

	}


	public AsteroidEntity createAsteroid(final Vector2d pos, final Vector2d dir, final int size, final long clientId) {
		return updateIDs(new AsteroidEntity(pos, dir, size), clientId);
	}

	/**
	 * creates Player with ownerID (isOwned by caller)
	 * 
	 * @param pos
	 * @return
	 */
	public PlayerEntity createPlayer(final Vector2d pos) {
		return createPlayer(pos, ownerId);
	}

	public PlayerEntity createPlayer(final Vector2d pos, final long clientId) {
		return updateIDs(new PlayerEntity(pos, new Vector2d()), clientId);
	}

	private <T extends Entity> T updateIDs(final T entity, final long clientId) {
		entity.setClientId(clientId);
		entity.setEntityID(currentEntityId++);
		entity.setOwned(clientId == ownerId);
		return entity;
	}


	public static EntityFactory getServerFactory() {
		return server;
	}

	public static EntityFactory getClientFactory() {
		return client;
	}

	public void setOWNER_ID(final long newClientID) {
		this.ownerId = newClientID;
	}

	public void setCURRENT_ENTITY_ID(final long l) {
		this.currentEntityId = l;
	}

	public long getCURRENT_ENTITY_ID() {
		return currentEntityId;
	}
}
