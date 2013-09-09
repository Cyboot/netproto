package net.tmt.client.network;

import java.util.List;

import net.tmt.common.network.DTO;

public interface NetworkReceive {

	/**
	 * @return true if there are unread server entities
	 */
	public boolean hasNewServerEntites();

	public List<DTO> getServerEntities();
}
