package net.tmt.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.tmt.Constants;
import net.tmt.client.engine.GameEngine;
import net.tmt.client.game.Game;
import net.tmt.client.network.NetworkManagerClient;
import net.tmt.client.util.ImageLoader;
import net.tmt.common.entity.EntityFactory;
import net.tmt.serverstarter.ServerStarter;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class ClientMain {
	private static Logger	logger	= Logger.getLogger(ClientMain.class);

	public static void main(final String[] args) {
		// log4j
		PropertyConfigurator.configureAndWatch("cfg/log4j.properties", 10 * 1000);

		// Default IDs
		EntityFactory.getClientFactory().setCURRENT_ENTITY_ID(Integer.MIN_VALUE);
		EntityFactory.getClientFactory().setOWNER_ID(Constants.CLIENT_ID_UNREGISTERED);

		// load images
		ImageLoader.init();
		// init game
		Game.init();

		// init network & connect with server
		NetworkManagerClient nm = NetworkManagerClient.getInstance();
		boolean connected = nm.registerWithServer(Constants.SERVER_IP);

		if (!connected) {
			ServerStarter.startServer(null);
			for (int i = 0; i < 50; i++) {
				if (ServerStarter.isServerOnline()) {
					logger.info("successfull started integrated server!");
					break;
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			}
			nm.registerWithServer(Constants.SERVER_IP);
		}


		JFrame frame = new JFrame("NetProto");
		GameEngine.init(frame);
		GameEngine engine = GameEngine.getInstance();
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(engine);
		frame.setContentPane(panel);
		frame.pack();
		frame.setResizable(false);

		// TODO client config frame.setLocationRelativeTo(null); //
		// frame.setLocation(-1200, 50);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		engine.start();

	}
}