package net.tmt.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.tmt.Constants;
import net.tmt.common.entity.AsteroidEntity;
import net.tmt.common.entity.Entity;
import net.tmt.common.entity.PlayerEntity;
import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.network.dtos.PlayerDTO;
import net.tmt.common.network.dtos.RegisterEntityDTO;
import net.tmt.common.network.dtos.ServerInfoDTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.StringFormatter;
import net.tmt.common.util.Vector2d;
import net.tmt.server.network.NetworkManagerServer;

public class GameLoop extends Thread {
	public static final int			DELTA_TARGET		= 15;
	private static final int		DELTA_TARGET_NANOS	= DELTA_TARGET * 1000 * 1000;

	private float					cpuWorkload;
	private NetworkManagerServer	network				= NetworkManagerServer.getInstance();
	private CountdownTimer			timerSend			= new CountdownTimer(Constants.SERVER_UPDATE_DELTA);
	private Map<Long, Entity>		entityMap			= new HashMap<Long, Entity>();

	private CountdownTimer			timerAddAsteroids	= new CountdownTimer(5000);

	public GameLoop() {
		AsteroidEntity entity = new AsteroidEntity(new Vector2d(Constants.WIDTH / 2, Constants.HEIGHT / 2),
				new Vector2d());
		addEntity(entity);
	}

	private void tick() {
		if (network.hasUnreadDTOs())
			synchronizeEntities(network.getUnreadDTOs());

		if (network.hasClientDisconnected())
			removeDisconnectedClientEntities(network.getClientDisconnectedID());

		for (Entry<Long, Entity> e : entityMap.entrySet()) {
			e.getValue().tick();
		}

		// DEBUG syso player position
		// for (Entry<Long, Entity> entry : entityMap.entrySet()) {
		// if (entry.getValue() instanceof PlayerEntity)
		// System.out.println("Player #" + entry.getKey() + " " +
		// entry.getValue().getPos());
		// }

		// add new asteroid from time to time
		if (timerAddAsteroids.isTimeleft()) {
			AsteroidEntity entity = new AsteroidEntity(new Vector2d(Constants.WIDTH / 2, Constants.HEIGHT / 2),
					new Vector2d());
			addEntity(entity);
		}


		if (timerSend.isTimeleft()) {
			network.sendDTO(new ServerInfoDTO(StringFormatter.format(cpuWorkload)));
			for (Entry<Long, Entity> e : entityMap.entrySet()) {
				network.sendDTO(e.getValue().toDTO());
			}
			network.sendNow();
		}
	}

	private void removeDisconnectedClientEntities(final long clientDisconnectedID) {
		List<Entity> removedEntities = new ArrayList<>();

		for (Entity e : entityMap.values()) {
			if (e.getClientId() == clientDisconnectedID)
				removedEntities.add(e);
		}
		entityMap.values().removeAll(removedEntities);
		System.out.println("removed " + removedEntities.size() + " Entities from former Client #"
				+ clientDisconnectedID);
	}

	private void addEntity(final Entity entity) {
		entityMap.put(entity.getEntityID(), entity);
	}

	/**
	 * synchronize the newly received clientDTOs with own DTOs
	 * 
	 * @param clientEntities
	 */
	private void synchronizeEntities(final List<DTO> clientEntities) {
		for (DTO d : clientEntities) {
			if (d instanceof RegisterEntityDTO) {
				RegisterEntityDTO regDto = (RegisterEntityDTO) d;
				EntityDTO entityDTO = regDto.getDto();
				long clientId = entityDTO.getClientId();

				if (entityDTO instanceof PlayerDTO) {
					PlayerEntity player = new PlayerEntity(entityDTO.getPos(), entityDTO.getDir());
					player.setClientId(clientId);

					long newID = player.getEntityID();
					NetworkManagerServer.getInstance().addRemappedEntity(clientId, player.toDTO(),
							entityDTO.getEntityID());
					System.out.println("adding new Player with EntityID= " + entityDTO.getEntityID());
					entityMap.put(newID, player);
				}
			}

			if (d instanceof EntityDTO == false)
				continue;

			EntityDTO dto = (EntityDTO) d;
			long id = dto.getEntityID();

			if (dto instanceof PlayerDTO) {
				if (entityMap.containsKey(id)) {
					entityMap.get(id).updateFromDTO(dto);
				}
			}
		}
	}

	@Override
	public void run() {
		Entity.setOWNER_ID(Constants.SERVER_ID);
		while (true) {
			long timeStart = System.nanoTime();

			// ##### tick #####
			tick();

			regulateFPS(timeStart);
		}
	}

	private void regulateFPS(final long timeStart) {
		long timePassed = System.nanoTime() - timeStart;
		if (timePassed < DELTA_TARGET_NANOS) {
			long sleepTime = DELTA_TARGET_NANOS - timePassed;

			long millis = sleepTime / (1000 * 1000);
			int nano = (int) (sleepTime % (1000 * 1000));

			try {
				Thread.sleep(millis, nano);
			} catch (InterruptedException e) {
			}
		}
		cpuWorkload = (float) timePassed / DELTA_TARGET_NANOS;
	}
}