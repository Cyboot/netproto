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
import net.tmt.common.network.DTO;
import net.tmt.common.network.EntityDTO;
import net.tmt.common.network.PlayerDTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.Vector2d;
import net.tmt.server.network.NetworkManagerServer;
import net.tmt.server.network.NetworkReceive;
import net.tmt.server.network.NetworkSend;

public class GameLoop extends Thread {
	public static final int		DELTA_TARGET		= 15;
	private static final int	DELTA_TARGET_NANOS	= DELTA_TARGET * 1000 * 1000;

	private float				cpuWorkload;
	private NetworkSend			networkSend			= NetworkManagerServer.getInstance();
	private NetworkReceive		networkReceive		= NetworkManagerServer.getInstance();
	private CountdownTimer		timerSend			= new CountdownTimer(1000);
	private List<Entity>		serverEntities		= new ArrayList<>();
	private Map<Long, Entity>	clientEntityMap		= new HashMap<Long, Entity>();

	private long				currentEntityID		= 0;
	private CountdownTimer		timerAddAsteroids	= new CountdownTimer(500);

	public GameLoop() {
	}

	private void tick() {
		// TODO game logic here (NPC, Player, other entities, Scores...)
		synchronizeEntities(networkReceive.getClientEntities());

		for (Entity e : serverEntities) {
			e.tick();
		}
		for (Entry<Long, Entity> entry : clientEntityMap.entrySet()) {
			System.out.println("Player #" + entry.getKey() + " " + entry.getValue().getPos());
		}

		if (timerAddAsteroids.isTimeleft()) {
			AsteroidEntity entity = new AsteroidEntity(new Vector2d(Constants.WIDTH / 2, Constants.HEIGHT / 2),
					new Vector2d());
			entity.setId(currentEntityID++);

			serverEntities.add(entity);
		}


		if (timerSend.isTimeleft()) {
			for (Entity e : serverEntities) {
				networkSend.sendDTO(e.toDTO());
			}
			networkSend.sendNow();
		}
	}

	private void synchronizeEntities(final List<DTO> clientEntities) {
		for (DTO dto : clientEntities) {
			long id = dto.getId();
			long clientId = dto.getClientId();

			if (dto instanceof PlayerDTO) {
				if (clientEntityMap.containsKey(id)) {
					clientEntityMap.get(id).updateFromDTO((EntityDTO) dto);
				} else {
					PlayerEntity player = new PlayerEntity(((PlayerDTO) dto).getPos());
					player.setClientId(clientId);
					clientEntityMap.put(id, player);
					System.out.println("adding new Player #" + dto.getId());
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