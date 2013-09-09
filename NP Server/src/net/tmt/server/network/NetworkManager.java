package net.tmt.server.network;

import java.util.ArrayList;
import java.util.List;


public class NetworkManager {

	private static NetworkManager	instance;
	private List<ClientData>		clientDataList	= new ArrayList<ClientData>();

	public static NetworkManager getInstance() {
		if (instance == null)
			instance = new NetworkManager();
		return instance;
	}

	public void acceptClients() {
		ReceiveThread at = new ReceiveThread();
		at.start();
	}

	public void addClient(final ClientData cd) {
		this.clientDataList.add(cd);
	}

}
