package net.tmt.client.network;

import java.io.ObjectInputStream;
import java.net.Socket;

import net.tmt.common.network.ClientInitDTO;
import net.tmt.common.network.DTO;

public class ReceiveThread extends Thread {

	private Socket				connectionSocket;
	private ObjectInputStream	in;

	public ReceiveThread(final Socket connectionSocket) {
		this.connectionSocket = connectionSocket;
		try {
			this.in = new ObjectInputStream(connectionSocket.getInputStream());
		} catch (Exception e) {
			System.err.println("error creating ReceiveThread");
		}
	}

	@Override
	public void run() {
		/* receive init reply */
		ClientInitDTO ci_reply;
		try {
			ci_reply = (ClientInitDTO) in.readObject();
			System.out.println("successfully registered, got client id " + ci_reply.getClientId());
			NetworkManager.getInstance().setMyClientId(ci_reply.getClientId());
			while (true) {
				DTO request = (DTO) this.in.readObject();
				// TODO: handle further client requests
			}
		} catch (Exception e) {
			System.err.println("error in ReceiveThread");
		}
	}
}
