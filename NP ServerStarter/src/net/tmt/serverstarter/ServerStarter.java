package net.tmt.serverstarter;

import net.tmt.server.ServerMain;
import net.tmt.server.network.NetworkManagerServer;

public class ServerStarter {

	public static void startServer(final String[] args) {
		new Thread() {
			@Override
			public void run() {
				ServerMain.main(args);
				super.run();
			}
		}.start();
	}

	public static boolean isServerOnline() {
		NetworkManagerServer server = NetworkManagerServer.getInstance();
		return server != null && NetworkManagerServer.getInstance().isOnline();
	}
}
