package net.tmt.server.network;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tmt.Constants;
import net.tmt.common.network.DTOReceiver;
import net.tmt.common.network.DTOSender;
import net.tmt.common.network.KryoInit;
import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.PacketDTO;
import net.tmt.common.util.TimeUtil;

import org.apache.log4j.Logger;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;


public class NetworkManagerServer implements DTOSender, DTOReceiver {
	private static Logger				logger					= Logger.getLogger(NetworkManagerServer.class);
	private static NetworkManagerServer	instance;

	private PacketDTO					packageDTO				= new PacketDTO(new ArrayList<DTO>());
	private Map<Long, PacketDTO>		dtoPackageReceivedMap	= new HashMap<Long, PacketDTO>();

	private Server						kryoServer;
	private NetworkListener				listener;
	private int							disconnectedClientID	= Constants.CLIENT_ID_UNREGISTERED;

	public static NetworkManagerServer getInstance() {
		if (instance == null)
			instance = new NetworkManagerServer();
		return instance;
	}

	public void startServer() {
		try {
			Log.set(Log.LEVEL_NONE);
			kryoServer = new Server(1024 * 256, 1024 * 128);
			KryoInit.init(kryoServer);

			kryoServer.start();
			kryoServer.bind(Constants.SERVER_PORT_TCP, Constants.SERVER_PORT_UDP);

			listener = new NetworkListener();
			kryoServer.addListener(listener);
			logger.info("started kryo server, accepting clients...");
		} catch (IOException e) {
			logger.warn("cannot start Server, Port already used? (" + e + ")");
		}

	}

	@Override
	public void sendDTO(final DTO dto) {
		packageDTO.getDtos().add(dto);
	}

	@Override
	public synchronized void sendNow() {
		packageDTO.setPacketNr(packageDTO.getPacketNr() + 1);
		packageDTO.setTimestamp(TimeUtil.getSynchroTimestamp());

		logger.trace("sending Paket #" + packageDTO.getPacketNr());

		for (Connection c : kryoServer.getConnections()) {
			if (c.isIdle())
				c.sendTCP(packageDTO);
		}
		// kryoServer.sendToAllUDP(packageDTO);

		packageDTO.getDtos().clear();
	}

	/**
	 * 
	 * @param receivedDTO
	 *            the last package received from the client
	 */
	public synchronized void putReceivedDTOs(final PacketDTO receivedDTO) {
		dtoPackageReceivedMap.put(receivedDTO.getClientId(), receivedDTO);
	}

	@Override
	public synchronized boolean hasUnreadDTOs() {
		return !dtoPackageReceivedMap.isEmpty();
	}

	@Override
	public synchronized List<DTO> getUnreadDTOs() {
		List<DTO> dtos = new ArrayList<>();

		for (PacketDTO dto : dtoPackageReceivedMap.values()) {
			dtos.addAll(dto.getDtos());
		}
		dtoPackageReceivedMap.clear();

		return dtos;
	}

	public long getClientDisconnectedId() {
		long result = disconnectedClientID;
		disconnectedClientID = Constants.CLIENT_ID_UNREGISTERED;
		return result;
	}

	public boolean hasClientDisconnected() {
		return disconnectedClientID != Constants.CLIENT_ID_UNREGISTERED;
	}

	public void disconnectedClient(final int id) {
		disconnectedClientID = id;
	}
}
