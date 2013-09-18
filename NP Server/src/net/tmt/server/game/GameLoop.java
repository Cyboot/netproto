package net.tmt.server.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.tmt.Constants;
import net.tmt.common.entity.AsteroidEntity;
import net.tmt.common.entity.Entity;
import net.tmt.common.entity.EntityFactory;
import net.tmt.common.entity.PlayerEntity;
import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.network.dtos.PlayerDTO;
import net.tmt.common.network.dtos.ServerInfoDTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.StringFormatter;
import net.tmt.common.util.Vector2d;
import net.tmt.server.network.NetworkManagerServer;

import org.apache.log4j.Logger;

public class GameLoop extends Thread {
	private static final int		DELTA_TARGET_NANOS	= Constants.DELTA_TARGET * 1000 * 1000;
	private static Logger			logger				= Logger.getLogger(GameLoop.class);

	private float					cpuWorkload;
	private NetworkManagerServer	network				= NetworkManagerServer.getInstance();
	private CountdownTimer			timerSend			= new CountdownTimer(Constants.SERVER_UPDATE_DELTA);
	private Map<Long, Entity>		entityMap			= new HashMap<Long, Entity>();
	private EntityFactory			entityFactory		= EntityFactory.getServerFactory();

	private CountdownTimer			timerAddAsteroids	= new CountdownTimer(500000);

	public GameLoop() {
	}

	public Map<Long, Entity> getEntities() {
		return this.entityMap;
	}

	private void tick() {
		if (network.hasUnreadDTOs())
			synchronizeEntities(network.getUnreadDTOs());

		if (network.hasClientDisconnected())
			removeDisconnectedClientEntities(network.getClientDisconnectedId());

		List<Long> deletedEntities = new ArrayList<>();
		for (Entity e : entityMap.values()) {
			e.tick(this.entityMap);
			if (e.isDeleted())
				deletedEntities.add(e.getEntityID());
		}
		entityMap.keySet().removeAll(deletedEntities);

		// add new asteroid from time to time
		if (timerAddAsteroids.isTimeleft()) {
			AsteroidEntity entity = entityFactory.createAsteroid(
					new Vector2d(Constants.WIDTH / 2, Constants.HEIGHT / 2), new Vector2d(), AsteroidEntity.INIT_SIZE,
					Constants.SERVER_ID);
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
		for (Entity e : entityMap.values()) {
			if (e.getClientId() == clientDisconnectedID)
				e.setDeleteTimeleft(500);
		}
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
			if (d instanceof EntityDTO == false)
				continue;

			EntityDTO dto = (EntityDTO) d;
			long id = dto.getEntityID();

			if (!entityMap.containsKey(id)) {
				if (d instanceof PlayerDTO) {
					PlayerEntity player = entityFactory.createPlayer(dto.getPos(), dto.getClientId());
					player.setClientId(dto.getClientId());
					player.setEntityID(dto.getEntityID());

					logger.debug("adding new Player with EntityID= " + dto.getEntityID());
					entityMap.put(player.getEntityID(), player);
				}
			}

			if (dto instanceof PlayerDTO) {
				if (entityMap.containsKey(id)) {
					entityMap.get(id).updateFromDTO(dto);
				}
			}
		}
	}

	@Override
	public void run() {
		entityFactory.setOWNER_ID(Constants.SERVER_ID);
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