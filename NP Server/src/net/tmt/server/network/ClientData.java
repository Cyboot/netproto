package net.tmt.server.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import net.tmt.common.network.DTO;


public class ClientData {
	private int					id;
	private ObjectOutputStream	out;
	private Socket				socket;

	public ClientData(final int id, final Socket clientSocket) {
		this.setId(id);
		this.socket = clientSocket;
		try {
			this.out = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(final DTO dto) {
		try {
			out.writeObject(dto);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}
}