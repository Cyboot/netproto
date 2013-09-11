package net.tmt.server.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import net.tmt.Constants;
import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.PackageDTO;

public class ReceiveThread extends Thread {
	private NetworkManagerServer	manager		= NetworkManagerServer.getInstance();
	private List<DTO>				dtoReceived	= new ArrayList<>();
	private ObjectInputStream		in;

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
				addReceivedDTO(receivedDTO);
			} catch (ClassNotFoundException | IOException e) {
				// DEBUG exit if error from client (disconnect)
				System.exit(-1);
				System.out.println("error while receiving Object: " + e);
			}
		}
	}

	private synchronized void addReceivedDTO(final PackageDTO receivedDTO) {
		if (receivedDTO.getClientId() == Constants.CLIENT_ID_UNREGISTERED) {
			receivedDTO.setDtos(new ArrayList<DTO>());
		}

		manager.putReceivedDTOs(receivedDTO);
	}

}
