package net.tmt.server.network;

import net.tmt.common.network.dtos.PacketDTO;
import net.tmt.common.network.dtos.TimeSyncroDTO;
import net.tmt.common.util.TimeUtil;

import org.apache.log4j.Logger;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class NetworkListener extends Listener {
	private static Logger			logger	= Logger.getLogger(NetworkListener.class);

	private NetworkManagerServer	manager	= NetworkManagerServer.getInstance();

	@Override
	public void received(final Connection connection, final Object object) {
		if (object instanceof PacketDTO) {
			logger.trace("from client $" + connection.getID() + " #" + ((PacketDTO) object).getPacketNr());
			manager.putReceivedDTOs((PacketDTO) object);
		}
		if (object instanceof TimeSyncroDTO) {
			logger.trace("TimeSyncroDTO");
			((TimeSyncroDTO) object).setServerTimestamp(TimeUtil.getSynchroTimestamp());
			connection.sendUDP(object);
		}
	}

	@Override
	public void connected(final Connection connection) {
		logger.info("new Client $" + connection.getID());
	}

	@Override
	public void disconnected(final Connection connection) {
		logger.info("Client $" + connection.getID() + " disconnected");
		manager.disconnectedClient(connection.getID());
	}


}
