package net.tmt.client.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
		Send.registerRequest(hostname);
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
