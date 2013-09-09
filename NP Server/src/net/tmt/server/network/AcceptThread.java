package net.tmt.server.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class AcceptThread extends Thread {

	private ServerSocket	acceptSocket;
	private int				currClientId	= 0;

	@Override
	public void run() {
		try {
			this.acceptSocket = new ServerSocket(6789);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("now accepting ...");

		while (true) {
			try {
				Socket connectionSocket = this.acceptSocket.accept();
				InetAddress clientIp = connectionSocket.getInetAddress();
				System.out.println("new client: " + clientIp.toString());
				ClientData currCd = new ClientData(this.currClientId, clientIp);
				NetworkManager.getInstance().addClient(currCd);

				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(
						connectionSocket.getInputStream()));
				DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

				String clientMSG = inFromClient.readLine();
				if (clientMSG.equals("init")) {
					outToClient.write(this.currClientId++);;
					outToClient.flush();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
