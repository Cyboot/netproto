package net.tmt.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.tmt.common.network.ClientInitDTO;
import net.tmt.common.network.DTO;

public class NetworkManager implements NetworkSend {
	private static NetworkManager	instance;

	private List<DTO>				dtoToSend	= new ArrayList<>();
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
					System.err.println("unable to register with server: " + e);
				}
			}
		};
		t.start();

	}

	@Override
	public void sendDTO(final DTO dto) {
		dtoToSend.add(dto);
	}


	@Override
	public void sendNow() {
		new Thread() {
			@Override
			public void run() {
				try {
					Socket s = new Socket(hostname, 6789);

					/* send DTO */
					ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());

					for (DTO dto : dtoToSend)
						out.writeObject(dto);
					out.flush();

					s.close();
					dtoToSend.clear();
				} catch (IOException e) {
					System.err.println("unable to send DTO: " + e);
				}

			};
		}.start();
	}

}
