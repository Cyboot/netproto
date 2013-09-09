package net.tmt.server;

import net.tmt.server.game.GameLoop;
import net.tmt.server.network.AcceptThread;

public class ServerMain {

	public static void main(final String[] args) {
		// TODO command line arguments
		// TODO read configs

		AcceptThread at = new AcceptThread();
		at.start();

		GameLoop loop = new GameLoop();
		loop.start();
	}

}