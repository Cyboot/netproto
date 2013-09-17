package net.tmt.client.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.tmt.Constants;
import net.tmt.client.engine.GameEngine;
import net.tmt.client.network.NetworkManagerClient;
import net.tmt.common.entity.Entity;
import net.tmt.common.entity.EntityFactory;
import net.tmt.common.entity.PlayerEntity;
import net.tmt.common.network.dtos.AsteroidDTO;
import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.network.dtos.PlayerDTO;
import net.tmt.common.network.dtos.ServerInfoDTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.Vector2d;
import net.tmt.serverstarter.ServerStarter;

import org.apache.log4j.Logger;

public class Game {
	public static final int			WIDTH			= Constants.WIDTH;
	public static final int			HEIGHT			= Constants.HEIGHT;

	private static Logger			logger			= Logger.getLogger(Game.class);
	private static Game				instance;

	private CountdownTimer			timerSend		= new CountdownTimer(Constants.CLIENT_UPDATE_DELTA, 0);
	private NetworkManagerClient	network			= NetworkManagerClient.getInstance();
	private EntityFactory			entityFactory	= EntityFactory.getClientFactory();

	private Map<Long, Entity>		entityMap		= new HashMap<Long, Entity>();
	private PlayerEntity			player;
	private String					serverWorkLoad;

	// used to determind if EntiyIDs are up to date with server
	private boolean					hasUpdatedEntityID;


	public Game() {
		player = entityFactory.createPlayer(new Vector2d(WIDTH / 3, HEIGHT / 3));
		entityMap.put(player.getEntityID(), player);
	}

	public void tick() {
		if (network.hasUnreadDTOs()) {
			synchronizeEntities(network.getUnreadDTOs());
		}
		if (!hasUpdatedEntityID && network.getRegisteredClientId() != Constants.CLIENT_ID_UNREGISTERED) {
			hasUpdatedEntityID = true;
			updateClientID(network.getRegisteredClientId(), Constants.CLIENT_ID_UNREGISTERED);
		}


		for (Entity e : entityMap.values()) {
			e.tick(this.entityMap);
		}

		if (timerSend.isTimeleft()) {
			for (Entity e : entityMap.values()) {
				if (e.isOwner()) {
					network.sendDTO(e.toDTO());
				}
			}
			network.sendNow();
		}
	}

	private void synchronizeEntities(final List<DTO> serverEntities) {
		for (DTO d : serverEntities) {
			if (d instanceof ServerInfoDTO) {
				serverWorkLoad = ((ServerInfoDTO) d).getCpuWorkLoad();
			}

			if (d instanceof EntityDTO == false)
				continue;

			EntityDTO dto = (EntityDTO) d;
			long id = dto.getEntityID();
			long clientId = dto.getClientId();


			if (entityMap.containsKey(id)) {
				// don't update owned entities
				if (!entityMap.get(id).isOwner())
					entityMap.get(id).updateFromDTO(dto);
			} else {
				Entity entity = null;
				if (dto instanceof AsteroidDTO) {
					entity = entityFactory.createAsteroid(dto.getPos(), dto.getDir(), ((AsteroidDTO) dto).getSize(),
							clientId);
				}
				if (dto instanceof PlayerDTO) {
					entity = entityFactory.createPlayer(dto.getPos(), clientId);
				}
				entity.setEntityID(id);
				entityMap.put(id, entity);
				System.out.println("adding new Asteroid from server #" + dto.getEntityID());
			}
		}
	}

	public void render(final Graphics g) {
		for (Entry<Long, Entity> entry : entityMap.entrySet())
			entry.getValue().render(g);

		g.setColor(Color.yellow);
		g.drawString("Server: " + serverWorkLoad, WIDTH - 70, 30);
	}

	public static Game getInstance() {
		return instance;
	}

	public static void init() {
		instance = new Game();
	}

	/**
	 * updates all Entities to the new clientID (updates EntityID as well as
	 * ClientID)
	 * 
	 * @param newClientID
	 * @param oldClientID
	 */
	private void updateClientID(final long newClientID, final long oldClientID) {
		logger.info("registered ClientID is $" + newClientID);

		String titlePreFix = "";
		if (ServerStarter.isClientServer()) {
			titlePreFix = "#### SERVER #### ";
		}

		GameEngine.getInstance().setTitle(titlePreFix + "netproto Client $" + newClientID);
		long offsetEntityId = -((long) Integer.MIN_VALUE);
		entityFactory.setCURRENT_ENTITY_ID(newClientID * 10 * 1000);
		entityFactory.setOWNER_ID(newClientID);

		List<Long> removedIDs = new ArrayList<>();
		Map<Long, Entity> remappedEntities = new HashMap<>();
		for (Entry<Long, Entity> entry : entityMap.entrySet()) {
			Entity e = entry.getValue();
			long key = entry.getKey();

			if (e.getClientId() == oldClientID) {
				e.setClientId(newClientID);
				e.setEntityID(e.getEntityID() + offsetEntityId + entityFactory.getCURRENT_ENTITY_ID());
				entityFactory.setCURRENT_ENTITY_ID(entityFactory.getCURRENT_ENTITY_ID() + 1);

				removedIDs.add(key);
				remappedEntities.put(e.getEntityID(), e);
			}
		}
		entityMap.keySet().removeAll(removedIDs);
		entityMap.putAll(remappedEntities);
	}
}
