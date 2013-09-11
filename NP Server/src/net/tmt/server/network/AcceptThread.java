package net.tmt.server.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import net.tmt.Constants;

/*
 * zurückumbauen zu Accept- und ReceiveThread, protip: sockets weitergeben
 */

public class AcceptThread extends Thread {
	private ServerSocket	acceptSocket;
	private long			currClientId	= 100;

	@Override
	public void run() {
		try {
			this.acceptSocket = new ServerSocket(Constants.SERVER_PORT);
		} catch (IOException e) {
			System.err.println("error creating acceptSocket");
		}

		System.out.println("now accepting ...");

		while (true) {
			try {
				Socket connectionSocket = acceptSocket.accept();
				ClientData cd = new ClientData(currClientId++, connectionSocket);
				ReceiveThread rt = new ReceiveThread(connectionSocket.getInputStream());
				rt.start();
				NetworkManagerServer.getInstance().addClient(cd);
			} catch (Exception e) {
				System.err.println("error accpeting client connection");
			}
		}
	}
}
