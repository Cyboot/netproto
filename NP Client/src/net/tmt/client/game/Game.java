package net.tmt.client.game;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.tmt.Constants;
import net.tmt.client.network.NetworkManagerClient;
import net.tmt.client.network.NetworkReceive;
import net.tmt.client.network.NetworkSend;
import net.tmt.common.entity.AsteroidEntity;
import net.tmt.common.entity.Entity;
import net.tmt.common.entity.PlayerEntity;
import net.tmt.common.network.AsteroidDTO;
import net.tmt.common.network.DTO;
import net.tmt.common.network.EntityDTO;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.Vector2d;

public class Game {
	public static final int		WIDTH			= Constants.WIDTH;
	public static final int		HEIGHT			= Constants.HEIGHT;

	private static Game			instance;

	private CountdownTimer		timerSend		= new CountdownTimer(1000);
	private NetworkSend			networkSend		= NetworkManagerClient.getInstance();
	private NetworkReceive		networkReceive	= NetworkManagerClient.getInstance();

	private Map<Long, Entity>	entityMap		= new HashMap<Long, Entity>();
	private List<Long>			killedEntities	= new ArrayList<>();
	private PlayerEntity		player;

	public Game() {
		player = new PlayerEntity(new Vector2d(WIDTH / 2, HEIGHT / 2));
		player.setClientId(Constants.CLIENT_ID);
	}

	public void tick() {
		if (networkReceive.hasNewServerEntites()) {
			synchronizeEntities(networkReceive.getServerEntities());
		}

		for (Entry<Long, Entity> entry : entityMap.entrySet()) {
			entry.getValue().tick();
			if (!entry.getValue().isAlive())
				killedEntities.add(entry.getKey());
		}
		// remove all dead entities
		// FIXME check if this is working
		killedEntities.clear();

		player.tick();

		// if (timerSend.isTimeleft()) {
		// networkSend.sendDTO(player.toDTO());
		// networkSend.sendNow();
		// }
	}

	private void synchronizeEntities(final List<DTO> serverEntities) {
		for (DTO dto : serverEntities) {
			long id = dto.getId();
			long clientId = dto.getClientId();

			if (dto instanceof AsteroidDTO) {
				if (entityMap.containsKey(id)) {
					entityMap.get(id).updateFromDTO((EntityDTO) dto);
				} else {
					AsteroidEntity entity = new AsteroidEntity(((AsteroidDTO) dto).getPos(),
							((AsteroidDTO) dto).getDir());
					entity.setClientId(clientId);
					entityMap.put(id, entity);
					System.out.println("adding new Asteroid from server #" + dto.getId());
				}
			}
		}
		System.out.println();
	}

	public void render(final Graphics g) {
		for (Entry<Long, Entity> entry : entityMap.entrySet())
			entry.getValue().render(g);

		player.render(g);
	}

	public static Game getInstance() {
		if (instance == null)
			instance = new Game();
		return instance;
	}
}
