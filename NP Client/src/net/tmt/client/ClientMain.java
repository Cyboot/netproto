package net.tmt.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.tmt.Constants;
import net.tmt.client.engine.GameEngine;
import net.tmt.client.game.Game;
import net.tmt.client.network.NetworkManagerClient;
import net.tmt.client.util.ImageLoader;
import net.tmt.common.entity.Entity;


public class ClientMain {

	public static void main(final String[] args) {
		Entity.setCURRENT_ENTITY_ID(Long.MIN_VALUE);
		Entity.setOWNER_ID(Constants.CLIENT_ID_UNREGISTERED);
		// init things
		ImageLoader.init();
		Game.init();


		NetworkManagerClient nm = NetworkManagerClient.getInstance();
		nm.registerWithServer(Constants.SERVER_IP);


		GameEngine engine = new GameEngine();
		JFrame frame = new JFrame("NetProto");
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