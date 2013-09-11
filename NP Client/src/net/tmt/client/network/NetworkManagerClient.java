package net.tmt.client.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import net.tmt.Constants;
import net.tmt.client.game.Game;
import net.tmt.common.network.DTOReceiver;
import net.tmt.common.network.DTOSender;
import net.tmt.common.network.dtos.DTO;
import net.tmt.common.network.dtos.PackageDTO;

public class NetworkManagerClient implements DTOSender, DTOReceiver {
	private static NetworkManagerClient	instance;
	private long						registeredClientId	= Constants.CLIENT_ID_UNREGISTERED;

	private String						hostname;

	private List<DTO>					dtoToSend			= new ArrayList<>();

	private Socket						clientSocket;
	private ReceiveThread				rt;
	private ObjectOutputStream			out;

	public static NetworkManagerClient getInstance() {
		if (instance == null)
			instance = new NetworkManagerClient();
		return instance;
	}

	public void setRegisteredClientID(final long registeredClientID) {
		if (registeredClientID != this.registeredClientId) {
			Game.getInstance().updateClientID(registeredClientID, this.registeredClientId);
			this.registeredClientId = registeredClientID;
		}
	}


	public void registerWithServer(final String hostname) {
		this.hostname = hostname;
		try {
			clientSocket = new Socket(hostname, Constants.SERVER_PORT);
			rt = new ReceiveThread(clientSocket.getInputStream());
			rt.start();
			out = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (Exception e) {
			System.err.println("unable to send init request: " + e);
		}
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
				packageDTO.setClientId(registeredClientId);

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
	public boolean hasUnreadDTOs() {
		return rt.hasNewServerDTOs();
	}

	@Override
	public List<DTO> getUnreadDTOs() {
		return rt.getUnreadDTOs();
	}

	public long getRegisteredClientId() {
		return registeredClientId;
	}

}
