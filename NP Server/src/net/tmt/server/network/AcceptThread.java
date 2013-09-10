package net.tmt.server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/*
 * zurückumbauen zu Accept- und ReceiveThread, protip: sockets weitergeben
 */

public class AcceptThread extends Thread {

	private ServerSocket	acceptSocket;
	private int				currClientId	= 0;
	private int				port			= 6789;

	@Override
	public void run() {
		try {
			this.acceptSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			System.err.println("error creating acceptSocket");
		}

		System.out.println("now accepting ...");

		while (true) {
			try {
				Socket connectionSocket = this.acceptSocket.accept();
				ClientData cd = new ClientData(this.currClientId++);
				ReceiveThread rt = new ReceiveThread(cd, connectionSocket);
				rt.start();
				NetworkManager.getInstance().addClient(cd);
			} catch (Exception e) {
				System.err.println("error accpeting client connection");
			}
		}
	}
}
