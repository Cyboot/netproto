package net.tmt.server;

import net.tmt.common.util.CountdownTimer;
import net.tmt.server.game.GameLoop;
import net.tmt.server.network.NetworkManagerServer;

import org.apache.log4j.PropertyConfigurator;

public class ServerMain {

	public static void main(final String[] args) {
		PropertyConfigurator.configureAndWatch("cfg/log4j.properties", 10 * 1000);
		// TODO command line arguments
		// TODO read config

		NetworkManagerServer nm = NetworkManagerServer.getInstance();
		nm.startServer();

		CountdownTimer.setDELTA_TARGET(GameLoop.DELTA_TARGET);
		GameLoop loop = new GameLoop();
		loop.start();
	}

}
