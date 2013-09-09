package net.tmt.server.game;

import java.util.List;

import net.tmt.common.network.DTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.server.network.NetworkReceive;
import net.tmt.server.network.NetworkSend;

public class GameLoop extends Thread {
	public static final int		DELTA_TARGET		= 15;
	private static final int	DELTA_TARGET_NANOS	= DELTA_TARGET * 1000 * 1000;

	private float				cpuWorkload;
	private NetworkSend			networkSend;
	private NetworkReceive		networkReceive;
	private CountdownTimer		timerSend;

	/**
	 * tick Method of the server
	 */
	private void tick() {
		// TODO game logic here (NPC, Player, other entities, Scores...)
		synchronizeEntitis(networkReceive.getClientEntities());


		if (timerSend.isTimeleft()) {
			networkSend.sendUpdatedEntity(null);
			networkSend.sendNewEntity(null);
			networkSend.sendNow();
		}
	}

	private void synchronizeEntitis(final List<DTO> clientEntities) {

	}

	@Override
	public void run() {

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
