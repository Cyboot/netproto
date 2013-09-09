package net.tmt.server.network;

import java.util.List;

import net.tmt.common.network.DTO;

public interface NetworkReceive {

	/**
	 * 
	 * @return List of unread Entities from all clients. If there are none it
	 *         returns an empty List
	 */
	public List<DTO> getClientEntities();
}
