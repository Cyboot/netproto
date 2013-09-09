package net.tmt.server.game;

import net.tmt.common.util.StringFormatter;

public class GameLoop extends Thread {
	private static final int	DELTA_TARGET	= 15;

	private float				cpuWorkload;

	@Override
	public void run() {
		final int DELTA_TARGET_NANOS = DELTA_TARGET * 1000 * 1000;

		while (true) {
			long timeStart = System.nanoTime();

			// ##### tick #####
			tick();

			regulateFPS(timeStart, DELTA_TARGET_NANOS);
		}
	}

	/**
	 * tick Method of the server
	 */
	private void tick() {
		// TODO game logic here (NPC, Player, other entities, Scores...)

		// TODO updates Clients (test how often)
		System.out.println("I'm the Boss (" + StringFormatter.format(cpuWorkload) + ")");
	}

	private void regulateFPS(final long timeStart, final long DELTA_TARGET_NANOS) {
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
