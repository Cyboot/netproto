package net.tmt.client.game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.tmt.Constants;
import net.tmt.client.network.NetworkManagerClient;
import net.tmt.common.entity.AsteroidEntity;
import net.tmt.common.entity.Entity;
import net.tmt.common.entity.PlayerEntity;
import net.tmt.common.network.DTOReceiver;
import net.tmt.common.network.DTOSender;
import net.tmt.common.network.dtos.AsteroidDTO;
import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.network.dtos.PlayerDTO;
import net.tmt.common.network.dtos.RegisterEntityDTO;
import net.tmt.common.network.dtos.RemappedEntityDTO;
import net.tmt.common.network.dtos.ServerInfoDTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.Vector2d;

public class Game {
	public static final int		WIDTH			= Constants.WIDTH;
	public static final int		HEIGHT			= Constants.HEIGHT;

	private static Game			instance;

	private CountdownTimer		timerSend		= new CountdownTimer(Constants.CLIENT_UPDATE_DELTA, 0);
	private DTOSender			networkSend		= NetworkManagerClient.getInstance();
	private DTOReceiver			networkReceive	= NetworkManagerClient.getInstance();

	private Map<Long, Entity>	entityMap		= new HashMap<Long, Entity>();
	private PlayerEntity		player;
	private String				serverWorkLoad;


	public Game() {
		player = new PlayerEntity(new Vector2d(WIDTH / 2, HEIGHT / 2), new Vector2d());
		player.setClientId(NetworkManagerClient.getInstance().getRegisteredClientId());
		entityMap.put(player.getEntityID(), player);
	}

	public void tick() {
		if (networkReceive.hasUnreadDTOs()) {
			synchronizeEntities(networkReceive.getUnreadDTOs());
		}

		for (Entry<Long, Entity> entry : entityMap.entrySet()) {
			entry.getValue().tick();
		}

		if (timerSend.isTimeleft()) {
			for (Entry<Long, Entity> entry : entityMap.entrySet()) {
				Entity entity = entry.getValue();
				if (entity.getClientId() == NetworkManagerClient.getInstance().getRegisteredClientId()) {

					// if newly created Entity --> RegisterDTO to server
					if (!entity.wasSendToServer()) {
						entity.sendToServer();
						networkSend.sendDTO(new RegisterEntityDTO(entry.getValue().toDTO()));
					} else
						networkSend.sendDTO(entry.getValue().toDTO());

				}
			}
			networkSend.sendNow();
		}
	}

	private void synchronizeEntities(final List<DTO> serverEntities) {
		for (DTO d : serverEntities) {
			if (d instanceof ServerInfoDTO) {
				serverWorkLoad = ((ServerInfoDTO) d).getCpuWorkLoad();
			}

			if (d instanceof RemappedEntityDTO) {
				long oldID = ((RemappedEntityDTO) d).getOldID();
				EntityDTO dto = ((RemappedEntityDTO) d).getDto();

				Entity remapped = entityMap.remove(oldID);
				if (remapped == null)
					continue;

				remapped.updateFromDTO(dto);
				entityMap.put(dto.getEntityID(), remapped);
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
					entity = new AsteroidEntity(dto.getPos(), dto.getDir());
				}
				if (dto instanceof PlayerDTO) {
					entity = new PlayerEntity(dto.getPos(), dto.getDir());
				}
				entity.setClientId(clientId);
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
		if (instance == null)
			instance = new Game();
		return instance;
	}

	public void updateClientID(final long newClientID, final long oldClientID) {
		// client had no valid ID before --> update Entities to the registeredID
		System.out.println("got registered ClientID: " + newClientID);
		Entity.setOWNER_ID(newClientID);
		for (Entry<Long, Entity> entry : entityMap.entrySet()) {
			if (entry.getValue().getClientId() == oldClientID)
				entry.getValue().setClientId(newClientID);
		}
	}
}
