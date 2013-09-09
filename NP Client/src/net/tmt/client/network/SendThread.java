package net.tmt.client.network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class SendThread extends Thread {

	@Override
	public void run() {
		try {
			Socket clientSocket = new Socket("127.0.0.1", 6789);
			DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
			BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			outToServer.writeBytes("init\n");

			int myClientId = inFromServer.read();
			System.out.println("successfully registered, got client id " + myClientId);
			clientSocket.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
