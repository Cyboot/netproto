package net.tmt.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.tmt.common.network.DTO;
import net.tmt.common.network.PackageDTO;

public class ReceiveThread extends Thread {
	private List<DTO>			dtoReceived	= new ArrayList<>();
	private ObjectInputStream	in;

	public ReceiveThread(final Socket connectionSocket) {
		try {
			this.in = new ObjectInputStream(connectionSocket.getInputStream());
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

		/* receive init reply */
		// ClientInitDTO ci_reply;
		// try {
		// ci_reply = (ClientInitDTO) in.readObject();
		// System.out.println("successfully registered, got client id " +
		// ci_reply.getClientId());
		// NetworkManagerClient.getInstance().setMyClientId(ci_reply.getClientId());
		// while (true) {
		// DTO request = (DTO) this.in.readObject();
		// // TODO: handle further client requests
		// System.out.println("received: " + request);
		// }
		// } catch (Exception e) {
		// System.err.println("error in ReceiveThread");
		// }
	}

	public synchronized boolean hasNewServerEntites() {
		return !dtoReceived.isEmpty();
	}


	private synchronized void addReceivedDTO(final PackageDTO packageDTO) {
		System.out.println("received: id=" + packageDTO.getId() + ", size=" + packageDTO.getDtos().size());
		// System.out.println(packageDTO.getDtos().get(0).getTimestamp());
		dtoReceived.addAll(packageDTO.getDtos());
	}

	public synchronized List<DTO> getUnreadDTOs() {
		List<DTO> result = new ArrayList<>(dtoReceived);
		dtoReceived.clear();
		return result;
	}
}
