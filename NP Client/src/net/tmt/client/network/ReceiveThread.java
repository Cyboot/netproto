package net.tmt.client.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import net.tmt.common.network.DTO;
import net.tmt.common.network.PackageDTO;

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
				addReceivedDTO(receivedDTO);
			} catch (ClassNotFoundException | IOException e) {
				System.out.println("error while receiving Object: " + e);
			}
		}
	}

	public synchronized boolean hasNewServerEntites() {
		return !dtoReceived.isEmpty();
	}


	private synchronized void addReceivedDTO(final PackageDTO packageDTO) {
		dtoReceived.addAll(packageDTO.getDtos());
	}

	public synchronized List<DTO> getUnreadDTOs() {
		List<DTO> result = new ArrayList<>(dtoReceived);
		dtoReceived.clear();
		return result;
	}
}
