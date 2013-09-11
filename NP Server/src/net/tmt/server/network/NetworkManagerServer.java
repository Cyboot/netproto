package net.tmt.server.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public static NetworkManagerServer getInstance() {
		if (instance == null)
			instance = new NetworkManagerServer();
		return instance;
	}

	public void startServer() {
		AcceptThread at = new AcceptThread();
		at.start();
	}

	public void addClient(final ClientData cd) {
		clientDataList.add(cd);
	}


	@Override
	public void sendDTO(final DTO dto) {
		dtoToSend.add(dto);
	}

	@Override
	public void sendNow() {
		new Thread() {
			@Override
			public void run() {
				final PackageDTO packetDTO = new PackageDTO(dtoToSend);
				packetDTO.setTimestamp(System.currentTimeMillis());

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
			};
		}.start();
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
