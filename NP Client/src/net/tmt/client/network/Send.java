package net.tmt.client.network;

import java.io.ObjectOutputStream;
import java.net.Socket;

import net.tmt.common.network.ClientInitDTO;

public class Send {

	public static ObjectOutputStream	out;

	public static void registerRequest(final String hostname) {
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					/* send init request */
					ClientInitDTO ci_reqest = new ClientInitDTO(-1);
					Socket clientSocket = new Socket(hostname, 6789);
					Send.out = new ObjectOutputStream(clientSocket.getOutputStream());
					out.writeObject(ci_reqest);
					out.flush();
					ReceiveThread rt = new ReceiveThread(clientSocket);
					rt.start();
				} catch (Exception e) {
					System.err.println("unable to send init request: " + e);
				}
			}
		};
		t.start();
	}

}
