package net.tmt.server.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import net.tmt.common.network.DTO;
import net.tmt.common.network.PackageDTO;

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
				System.out.println("error while receiving Object: " + e);
			}
		}
	}

	private synchronized void addReceivedDTO(final PackageDTO receivedDTO) {
		manager.putReceivedDTOs(receivedDTO);
	}

	// @Override
	// public ReceiveThread(final ClientData cd, final Socket connectionSocket)
	// {
	// this.clientData = cd;
	// this.connectionSocket = connectionSocket;
	// try {
	// this.in = new ObjectInputStream(connectionSocket.getInputStream());
	// this.out = new ObjectOutputStream(connectionSocket.getOutputStream());
	// } catch (Exception e) {
	// System.err.println("error in ReceiveThread for Client #" +
	// this.clientData.getId() + ": " + e);
	// }
	// }
	// public void run() {
	// try {
	// /* init */
	// ClientInitDTO ci_request = (ClientInitDTO) this.in.readObject();
	// if (ci_request.getClientId() == -1) {
	// ClientInitDTO ci_reply = new ClientInitDTO(this.clientData.getId());
	// this.out.writeObject(ci_reply);
	// this.out.flush();
	//
	// System.out.println("new client. id: " + this.clientData.getId());
	// while (true) {
	// DTO request = (DTO) this.in.readObject();
	// }
	// } else {
	// System.err.println("misbehaving client, closing socket");
	// this.connectionSocket.close();
	// }
	// } catch (Exception e) {
	// System.err.println("error in ReceiveThread for Client #" +
	// this.clientData.getId() + ": " + e);
	// e.printStackTrace();
	// }
	// }

}
