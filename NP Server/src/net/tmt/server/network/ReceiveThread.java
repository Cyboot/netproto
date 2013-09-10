package net.tmt.server.network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.tmt.common.network.ClientInitDTO;
import net.tmt.common.network.DTO;

public class ReceiveThread extends Thread {

	private ClientData			clientData;
	private Socket				connectionSocket;
	private ObjectInputStream	in;
	private ObjectOutputStream	out;				// FIXME: ReceiveThread is
													// not for sending, relocate
													// all sending activity to
													// Send

	public ReceiveThread(final ClientData cd, final Socket connectionSocket) {
		this.clientData = cd;
		this.connectionSocket = connectionSocket;
		try {
			this.in = new ObjectInputStream(connectionSocket.getInputStream());
			this.out = new ObjectOutputStream(connectionSocket.getOutputStream());
		} catch (Exception e) {
			System.err.println("error in ReceiveThread for Client #" + this.clientData.id + ": " + e);
		}
	}

	@Override
	public void run() {
		try {
			/* init */
			ClientInitDTO ci_request = (ClientInitDTO) this.in.readObject();
			if (ci_request.getClientId() == -1) {
				ClientInitDTO ci_reply = new ClientInitDTO(this.clientData.id);
				this.out.writeObject(ci_reply);
				this.out.flush();

				System.out.println("new client. id: " + this.clientData.id);
				while (true) {
					DTO request = (DTO) this.in.readObject();
					// TODO: handle further client requests
				}
			} else {
				System.err.println("misbehaving client, closing socket");
				this.connectionSocket.close();
			}
		} catch (Exception e) {
			System.err.println("error in ReceiveThread for Client #" + this.clientData.id + ": " + e);
			e.printStackTrace();
		}
	}

}
