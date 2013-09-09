package net.tmt.server;

import net.tmt.common.util.CountdownTimer;
import net.tmt.server.game.GameLoop;

public class ServerMain {

	public static void main(final String[] args) {
		// TODO command line arguments
		// TODO read configs

		CountdownTimer.setDELTA_TARGET(GameLoop.DELTA_TARGET);
		GameLoop loop = new GameLoop();
		loop.start();
	}

}