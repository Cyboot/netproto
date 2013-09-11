package net.tmt.server.game;

import java.util.ArrayList;
import java.util.List;

import net.tmt.Constants;
import net.tmt.common.entity.AsteroidEntity;
import net.tmt.common.entity.Entity;
import net.tmt.common.network.DTO;
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
	private NetworkReceive		networkReceive;
	private CountdownTimer		timerSend			= new CountdownTimer(1000);
	private List<Entity>		entities			= new ArrayList<>();

	private long				currentEntityID		= 0;
	private CountdownTimer		timerAddAsteroids	= new CountdownTimer(500);

	public GameLoop() {
		Entity e = new AsteroidEntity(new Vector2d(Constants.WIDTH / 2, Constants.HEIGHT / 2), new Vector2d());
		Entity e2 = new AsteroidEntity(new Vector2d(Constants.WIDTH / 2, Constants.HEIGHT / 2), new Vector2d());
		e.setId(currentEntityID++);
		entities.add(e);
		e2.setId(currentEntityID++);
		entities.add(e2);
	}

	private void tick() {
		// TODO game logic here (NPC, Player, other entities, Scores...)
		// synchronizeEntitis(networkReceive.getClientEntities());

		for (Entity e : entities) {
			e.tick();
		}

		if (timerAddAsteroids.isTimeleft()) {
			AsteroidEntity entity = new AsteroidEntity(new Vector2d(Constants.WIDTH / 2, Constants.HEIGHT / 2),
					new Vector2d());
			entity.setId(currentEntityID++);

			entities.add(entity);
		}


		if (timerSend.isTimeleft()) {
			for (Entity e : entities) {
				networkSend.sendDTO(e.toDTO());
			}
			networkSend.sendNow();
		}
	}

	private void synchronizeEntitis(final List<DTO> clientEntities) {

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