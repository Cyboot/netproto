package net.tmt.client.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.tmt.Constants;
import net.tmt.common.network.DTO;
import net.tmt.common.network.PackageDTO;

public class NetworkManagerClient implements NetworkSend, NetworkReceive {
	private static NetworkManagerClient	instance;

	private long						myClientId;
	private String						hostname;

	private List<DTO>					dtoToSend	= new ArrayList<>();

	private Socket						clientSocket;
	private ReceiveThread				rt;
	private ObjectOutputStream			out;

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
					clientSocket = new Socket(hostname, Constants.SERVER_PORT);
					rt = new ReceiveThread(clientSocket.getInputStream());
					rt.start();
					out = new ObjectOutputStream(clientSocket.getOutputStream());
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
				PackageDTO packageDTO = new PackageDTO(dtoToSend);
				packageDTO.setClientId(Constants.CLIENT_ID);

				try {
					out.writeObject(packageDTO);
					out.flush();
					out.reset();

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
