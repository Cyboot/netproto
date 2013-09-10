package net.tmt.client.game;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import net.tmt.Constants;
import net.tmt.client.network.NetworkManager;
import net.tmt.client.network.NetworkReceive;
import net.tmt.client.network.NetworkSend;
import net.tmt.common.entity.AsteroidEntity;
import net.tmt.common.entity.Entity;
import net.tmt.common.entity.PlayerEntity;
import net.tmt.common.network.DTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.Vector2d;

public class Game {
	public static final int	WIDTH			= Constants.WIDTH;
	public static final int	HEIGHT			= Constants.HEIGHT;

	private static Game		instance;

	private CountdownTimer	timerSend		= new CountdownTimer(1000);
	private NetworkSend		networkSend		= NetworkManager.getInstance();
	private NetworkReceive	networkReceive;

	private List<Entity>	entities		= new ArrayList<>();
	private List<Entity>	killedEntities	= new ArrayList<>();
	private PlayerEntity	player;

	public Game() {
		for (int i = 0; i < 100000; i++)
			entities.add(new AsteroidEntity(new Vector2d(WIDTH / 2, HEIGHT / 2), new Vector2d(Math.random() - 0.5, Math
					.random() - 0.5)));
		player = new PlayerEntity(new Vector2d(WIDTH / 2, HEIGHT / 2));
	}

	public void tick() {
		// if (networkReceive.hasNewServerEntites()) {
		// synchronizeEntities(networkReceive.getServerEntities());
		// }

		for (Entity e : entities) {
			e.tick();
			if (!e.isAlive())
				killedEntities.add(e);
		}
		// remove all dead entities
		entities.removeAll(killedEntities);
		killedEntities.clear();

		player.tick();

		if (timerSend.isTimeleft()) {
			networkSend.sendDTO(player.toDTO());
			networkSend.sendNow();
		}
	}

	private void synchronizeEntities(final List<DTO> serverEntities) {
		// TODO synchronize Entities
	}

	public void render(final Graphics g) {
		for (Entity e : entities)
			e.render(g);
		player.render(g);
	}

	public static Game getInstance() {
		if (instance == null)
			instance = new Game();
		return instance;
	}
}
