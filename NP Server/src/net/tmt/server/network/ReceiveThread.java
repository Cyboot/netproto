package net.tmt.server.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import net.tmt.common.network.ClientInitDTO;

/*
 * Allgemeiner und alleiniger Thread für eingehende Netzwerkverbindungen,
 * damit Servermit einem einzigen Port zum ansprechen auskommt.
 * Eventuelle verteile Verarbeitung auf weitere Threads mit Queues möglich. 
 */

public class ReceiveThread extends Thread {

	private ServerSocket	receiveSocket;
	private int				currClientId	= 0;
	private int				port			= 6789;

	@Override
	public void run() {
		try {
			this.receiveSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("now accepting ...");

		while (true) {
			try {
				Socket connectionSocket = this.receiveSocket.accept();
				ObjectInputStream in = new ObjectInputStream(connectionSocket.getInputStream());
				ClientInitDTO request = (ClientInitDTO) in.readObject();

				if (request.getClientId() == -1) { /* init request */
					ClientInitDTO ci_reply = new ClientInitDTO(this.currClientId++);
					ObjectOutputStream out = new ObjectOutputStream(connectionSocket.getOutputStream());
					out.writeObject(ci_reply);
					out.flush();

					InetAddress clientIp = connectionSocket.getInetAddress();
					System.out.println("new client: " + clientIp.toString());
					ClientData currCd = new ClientData(this.currClientId, clientIp);
					NetworkManager.getInstance().addClient(currCd);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
