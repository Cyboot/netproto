package net.tmt.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.tmt.common.network.ClientInitDTO;
import net.tmt.common.network.DTO;

public class NetworkManager implements NetworkSend {

	private static NetworkManager	instance;
	private long					myClientId;
	private String					hostname;

	public static NetworkManager getInstance() {
		if (instance == null)
			instance = new NetworkManager();
		return instance;
	}

	public void setMyClientId(final long id) {
		this.myClientId = id;
	}

	public void registerWithServer(final String hostname) {
		this.hostname = hostname;
		Thread t = new Thread() {
			@Override
			public void run() {
				try {
					/* send init request */
					ClientInitDTO ci_reqest = new ClientInitDTO(-1);
					Socket clientSocket = new Socket(hostname, 6789);
					ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
					out.writeObject(ci_reqest);
					out.flush();

					/* receive init reply */
					ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
					ClientInitDTO ci_reply = (ClientInitDTO) in.readObject();
					System.out.println("successfully registered, got client id " + ci_reply.getClientId());
					NetworkManager.getInstance().setMyClientId(ci_reply.getClientId());
					clientSocket.close();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		t.start();

	}

	@Override
	public void sendUpdatedEntity(final DTO entity) {
		try {
			Socket s = new Socket(this.hostname, 6789);

			/* send DTO */
			ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
			out.writeObject(entity);
			out.flush();

			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void sendNewEntity(final DTO entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void sendNow() {
		// TODO Auto-generated method stub

	}

}
