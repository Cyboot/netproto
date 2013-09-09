package net.tmt.client.network;

import net.tmt.common.network.DTO;

public interface NetworkSend {

	public void sendUpdatedEntity(DTO entity);

	public void sendNewEntity(DTO entity);

	public void sendNow();
}
