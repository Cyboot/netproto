package net.tmt.client;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.tmt.client.engine.GameEngine;
import net.tmt.client.network.NetworkManagerClient;


public class ClientMain {

	public static void main(final String[] args) {
		NetworkManagerClient nm = NetworkManagerClient.getInstance();
		nm.registerWithServer("127.0.0.1");


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