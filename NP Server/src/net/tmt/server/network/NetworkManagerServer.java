package net.tmt.server.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tmt.Constants;
import net.tmt.common.network.DTOReceiver;
import net.tmt.common.network.DTOSender;
import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.EntityDTO;
import net.tmt.common.network.dtos.PackageDTO;
import net.tmt.common.network.dtos.RemappedEntityDTO;


public class NetworkManagerServer implements DTOSender, DTOReceiver {
	private static NetworkManagerServer		instance;

	private List<DTO>						dtoToSend				= new ArrayList<>();
	private Map<Long, PackageDTO>			dtoPackageReceivedMap	= new HashMap<Long, PackageDTO>();
	private Map<Long, RemappedEntityDTO>	remappedDTOMap			= new HashMap<>();
	private List<ClientData>				clientDataList			= new ArrayList<ClientData>();

	private long							clientDisconnectedID	= Constants.CLIENT_ID_UNREGISTERED;

	public static NetworkManagerServer getInstance() {
		if (instance == null)
			instance = new NetworkManagerServer();
		return instance;
	}

	public void startServer() {
		AcceptThread at = new AcceptThread();
		at.start();
	}

	public synchronized void addClient(final ClientData cd) {
		clientDataList.add(cd);
	}

	public synchronized void disconnectClient(final ClientData cd) {
		System.out.println("client $" + cd.getId() + " disconnected");

		// only disconnect one time
		boolean contained = clientDataList.remove(cd);
		if (contained)
			clientDisconnectedID = cd.getId();
	}

	public long getClientDisconnectedID() {
		long result = clientDisconnectedID;
		clientDisconnectedID = Constants.CLIENT_ID_UNREGISTERED;
		return result;
	}

	/**
	 * @return true first time called when a client disconnects
	 */
	public synchronized boolean hasClientDisconnected() {
		return clientDisconnectedID != Constants.CLIENT_ID_UNREGISTERED;
	}

	@Override
	public void sendDTO(final DTO dto) {
		dtoToSend.add(dto);
	}

	@Override
	public synchronized void sendNow() {
		try {
			// TODO check if sendNow should be in seperate Thread (was before,
			// problems with ConcurrentModification on clientDataList)
			// new Thread() {
			// @Override
			// public void run() {
			final PackageDTO packetDTO = new PackageDTO(dtoToSend);
			packetDTO.setTimestamp(System.currentTimeMillis());

			// FIXME ConcurrentModification here on Client disconnect
			for (ClientData c : clientDataList) {
				if (remappedDTOMap.containsKey(c.getId())) {
					// there are remapped IDs for that client --> send
					// modified copy of packageDTO to it
					List<DTO> modifiedDtoToSend = new ArrayList<>(dtoToSend);
					RemappedEntityDTO remappedEntityDTO = remappedDTOMap.get(c.getId());
					modifiedDtoToSend.add(remappedEntityDTO);
					System.out.println("sending remapped: " + remappedEntityDTO);
					PackageDTO modifiedDTO = new PackageDTO(modifiedDtoToSend);
					c.send(modifiedDTO);
				} else {
					// send packet normally
					c.send(packetDTO);
				}
			}
			dtoToSend.clear();
			remappedDTOMap.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// };
		// }.start();
	}

	/**
	 * called from every ReceiveThread
	 * 
	 * @param receivedDTO
	 *            the last package received from the client
	 */
	public synchronized void putReceivedDTOs(final PackageDTO receivedDTO) {
		dtoPackageReceivedMap.put(receivedDTO.getClientId(), receivedDTO);
	}

	@Override
	public synchronized boolean hasUnreadDTOs() {
		return !dtoPackageReceivedMap.isEmpty();
	}

	@Override
	public synchronized List<DTO> getUnreadDTOs() {
		List<DTO> dtos = new ArrayList<>();

		for (PackageDTO dto : dtoPackageReceivedMap.values()) {
			dtos.addAll(dto.getDtos());
		}

		dtoPackageReceivedMap.clear();

		return dtos;
	}


	public synchronized void addRemappedEntity(final long clientId, final EntityDTO entityDTO, final long oldID) {
		RemappedEntityDTO dto = new RemappedEntityDTO(oldID, entityDTO);
		dto.setClientId(clientId);
		remappedDTOMap.put(clientId, dto);
	}


}
