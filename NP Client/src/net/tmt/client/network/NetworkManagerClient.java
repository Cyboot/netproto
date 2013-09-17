package net.tmt.client.network;

import java.util.ArrayList;
import java.util.List;

import net.tmt.Constants;
import net.tmt.common.network.DTOReceiver;
import net.tmt.common.network.DTOSender;
import net.tmt.common.network.KryoInit;
import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.PacketDTO;
import net.tmt.common.util.TimeUtil;

import org.apache.log4j.Logger;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.minlog.Log;

public class NetworkManagerClient implements DTOSender, DTOReceiver {
	private static NetworkManagerClient	instance				= new NetworkManagerClient();
	private static Logger				logger					= Logger.getLogger(NetworkManagerClient.class);

	private long						registeredClientId		= Constants.CLIENT_ID_UNREGISTERED;
	private Client						kryoClient;

	private final PacketDTO				packageDTO				= new PacketDTO(new ArrayList<DTO>());
	private PacketDTO					lastReceivedPackageDTO	= new PacketDTO(new ArrayList<DTO>());

	private boolean						hasUnreadDTOs;
	private long						lastPacketNr;

	public static NetworkManagerClient getInstance() {
		return instance;
	}

	public boolean registerWithServer(final String hostname) {
		try {
			Log.set(Log.LEVEL_NONE);
			kryoClient = new Client(1024 * 256, 1024 * 128);
			KryoInit.init(kryoClient);

			kryoClient.start();
			kryoClient.connect(5000, hostname, Constants.SERVER_PORT_TCP, Constants.SERVER_PORT_UDP);

			kryoClient.addListener(new NetworkListener());
			TimeSynchronizer.init(kryoClient);
		} catch (Exception e) {
			logger.warn("unable to start kryo-client: " + e);
			return false;
		}
		return true;
	}

	@Override
	public void sendDTO(final DTO dto) {
		// do not send data until we have a valid clientID
		if (registeredClientId == Constants.CLIENT_ID_UNREGISTERED)
			return;
		packageDTO.getDtos().add(dto);
	}


	@Override
	public void sendNow() {
		// do not send data until we have a valid clientID
		if (registeredClientId == Constants.CLIENT_ID_UNREGISTERED)
			return;

		packageDTO.setTimestamp(TimeUtil.getSynchroTimestamp());

		logger.trace("sending #" + packageDTO);

		kryoClient.sendUDP(packageDTO);
		packageDTO.getDtos().clear();
	}

	@Override
	public synchronized boolean hasUnreadDTOs() {
		boolean result = hasUnreadDTOs;
		hasUnreadDTOs = false;
		return result;
	}

	@Override
	public synchronized List<DTO> getUnreadDTOs() {
		return lastReceivedPackageDTO.getDtos();
	}

	public synchronized void addReceivedPackageDTO(final PacketDTO dto) {
		if (dto.getPacketNr() > lastPacketNr) {
			hasUnreadDTOs = true;
			lastReceivedPackageDTO = dto;
			lastPacketNr = lastReceivedPackageDTO.getPacketNr();
		}
	}

	public long getRegisteredClientId() {
		return registeredClientId;
	}

	public void setRegisteredClientId(final long registeredClientId) {
		this.registeredClientId = registeredClientId;
		try {
			TimeSynchronizer.getInstance().start();
		} catch (Exception e) {
		}
	}
}
