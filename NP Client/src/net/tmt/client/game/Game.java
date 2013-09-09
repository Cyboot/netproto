package net.tmt.client.game;

import java.awt.Graphics;
import java.util.List;

import net.tmt.client.network.NetworkReceive;
import net.tmt.client.network.NetworkSend;
import net.tmt.common.network.DTO;
import net.tmt.common.util.CountdownTimer;

public class Game {
	public static final int	WIDTH		= 1024;
	public static final int	HEIGHT		= 640;

	private static Game		instance;

	private CountdownTimer	timerSend	= new CountdownTimer(1000);
	private NetworkSend		networkSend;
	private NetworkReceive	networkReceive;


	public void tick() {
		if (networkReceive.hasNewServerEntites()) {
			synchronizeEntities(networkReceive.getServerEntities());
		}

		if (timerSend.isTimeleft()) {
			networkSend.sendUpdatedEntity(null);
			networkSend.sendNewEntity(null);
			networkSend.sendNow();
		}
	}

	private void synchronizeEntities(final List<DTO> serverEntities) {
		// TODO synchronize Entities
	}

	public void render(final Graphics g) {

	}

	public static Game getInstance() {
		if (instance == null)
			instance = new Game();
		return instance;
	}
}
