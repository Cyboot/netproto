package net.tmt.server.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.tmt.common.network.dtos.PackageDTO;


public class ClientData {
	private long				registeredID;
	private ObjectOutputStream	out;
	private Socket				socket;

	public ClientData(final long currClientId, final Socket clientSocket) {
		this.setId(currClientId);
		this.socket = clientSocket;
		try {
			this.out = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void send(final PackageDTO dto) {
		dto.setRegisterClientID(registeredID);
		try {
			out.writeObject(dto);
			out.flush();
			out.reset();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long getId() {
		return registeredID;
	}

	public void setId(final long id) {
		this.registeredID = id;
	}
}