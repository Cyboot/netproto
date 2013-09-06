package net.tmt.server;

import net.tmt.server.game.GameLoop;

public class ServerMain {

	public static void main(String[] args) {
		// TODO command line arguments
		// TODO read configs

		GameLoop loop = new GameLoop();
		loop.start();
	}

}
