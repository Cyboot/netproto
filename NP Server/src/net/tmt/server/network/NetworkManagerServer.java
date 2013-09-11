package net.tmt.server.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tmt.common.network.DTO;
import net.tmt.common.network.PackageDTO;


public class NetworkManagerServer implements NetworkSend, NetworkReceive {
	private static NetworkManagerServer	instance;

	private List<DTO>					dtoToSend				= new ArrayList<>();
	private Map<Long, PackageDTO>		dtoPackageReceivedMap	= new HashMap<Long, PackageDTO>();
	private List<ClientData>			clientDataList			= new ArrayList<ClientData>();

	public static NetworkManagerServer getInstance() {
		if (instance == null)
			instance = new NetworkManagerServer();
		return instance;
	}

	public void acceptClients() {
		AcceptThread at = new AcceptThread();
		at.start();
	}

	public void addClient(final ClientData cd) {
		this.clientDataList.add(cd);
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
				packetDTO.setId((long) (Math.random() * 100));
				packetDTO.setTimestamp(System.currentTimeMillis());

				// System.out.println("sending: id=" + packetDTO.getId() +
				// ", size=" + packetDTO.getDtos().size());
				for (ClientData c : clientDataList) {
					c.send(packetDTO);
				}
				dtoToSend.clear();
			};
		}.start();
	}

	@Override
	public synchronized List<DTO> getClientEntities() {
		List<DTO> dtos = new ArrayList<>();

		for (PackageDTO dto : dtoPackageReceivedMap.values()) {
			dtos.addAll(dto.getDtos());
		}

		return dtos;
	}

	public synchronized void putReceivedDTOs(final PackageDTO receivedDTO) {
		// System.out.println("from #" + receivedDTO.getClientId() + " size:" +
		// receivedDTO.getDtos().size());
		dtoPackageReceivedMap.put(receivedDTO.getClientId(), receivedDTO);
	}
}
