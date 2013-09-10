package net.tmt.client.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.tmt.Constants;
import net.tmt.common.network.ClientInitDTO;
import net.tmt.common.network.DTO;

public class NetworkManagerClient implements NetworkSend, NetworkReceive {
	private static NetworkManagerClient	instance;

	private long						myClientId;
	private String						hostname;

	private List<DTO>					dtoToSend	= new ArrayList<>();
	private ReceiveThread				rt;

	public static NetworkManagerClient getInstance() {
		if (instance == null)
			instance = new NetworkManagerClient();
		return instance;
	}

	public void setMyClientId(final long id) {
		this.myClientId = id;
	}

	public void registerWithServer(final String hostname) {
		this.hostname = hostname;
		new Thread() {

			@Override
			public void run() {
				try {
					/* send init request */
					ClientInitDTO ci_reqest = new ClientInitDTO(-1);
					Socket clientSocket = new Socket(hostname, Constants.SERVER_PORT);
					rt = new ReceiveThread(clientSocket);
					rt.start();
					// ObjectOutputStream out = new
					// ObjectOutputStream(clientSocket.getOutputStream());
					// out.writeObject(ci_reqest);
					// out.flush();
				} catch (Exception e) {
					System.err.println("unable to send init request: " + e);
				}
			}
		}.start();
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

	@Override
	public boolean hasNewServerEntites() {
		return rt.hasNewServerEntites();
	}

	@Override
	public List<DTO> getServerEntities() {
		return rt.getUnreadDTOs();
	}

}
