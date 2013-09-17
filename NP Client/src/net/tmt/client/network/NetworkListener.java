package net.tmt.client.network;

import net.tmt.common.network.dtos.PacketDTO;
import net.tmt.common.network.dtos.TimeSyncroDTO;

import org.apache.log4j.Logger;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkListener extends Listener {
	private static Logger			logger	= Logger.getLogger(NetworkListener.class);
	private NetworkManagerClient	manager	= NetworkManagerClient.getInstance();


	@Override
	public void received(final Connection connection, final Object object) {
		manager.setRegisteredClientId(connection.getID());
		if (object instanceof PacketDTO) {
			PacketDTO dto = (PacketDTO) object;
			logger.trace("received package #" + dto.getPacketNr());
			manager.addReceivedPackageDTO(dto);
		} else if (object instanceof TimeSyncroDTO) {
			TimeSynchronizer.getInstance().put((TimeSyncroDTO) object);
		} else {
			logger.trace(object);
		}
	}

	@Override
	public void connected(final Connection connection) {
		// manager.registerClientID(connection.getID());
	}
}
