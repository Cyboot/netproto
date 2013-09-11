package net.tmt.client.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.PackageDTO;
import net.tmt.common.network.dtos.RemappedEntityDTO;

public class ReceiveThread extends Thread {
	private List<DTO>			dtoReceived	= new ArrayList<>();
	private ObjectInputStream	in;

	public ReceiveThread(final InputStream inputStream) {
		try {
			this.in = new ObjectInputStream(inputStream);
		} catch (Exception e) {
			System.err.println("error creating ReceiveThread");
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				PackageDTO receivedDTO = (PackageDTO) in.readObject();
				NetworkManagerClient.getInstance().setRegisteredClientID(receivedDTO.getRegisterClientID());

				// adds the content of the packet to the received DTO-list
				addReceivedDTO(receivedDTO);
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("error while receiving Object: " + e);
			}
		}
	}

	public synchronized boolean hasNewServerDTOs() {
		return !dtoReceived.isEmpty();
	}


	private synchronized void addReceivedDTO(final PackageDTO packageDTO) {
		// DEBUG test received RemappedDTO
		for (DTO d : packageDTO.getDtos())
			if (d instanceof RemappedEntityDTO)
				System.out.println("remapped: " + d);

		dtoReceived.addAll(packageDTO.getDtos());
	}

	/**
	 * get all unread DTOs, will return an empty list until it receives new DTOs
	 * from the server
	 * 
	 * @return
	 */
	public synchronized List<DTO> getUnreadDTOs() {
		List<DTO> result = new ArrayList<>(dtoReceived);
		dtoReceived.clear();
		return result;
	}
}
