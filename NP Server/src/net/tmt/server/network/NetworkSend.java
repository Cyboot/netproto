package net.tmt.server.network;

import net.tmt.common.network.DTO;

public interface NetworkSend {

	public void sendDTO(DTO dto);

	public void sendNow();
}
