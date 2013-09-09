package net.tmt.server.network;

import java.net.InetAddress;

public class ClientData {

	InetAddress	ipAddr;
	int			id;

	public ClientData(final int id, final InetAddress ipAddr) {
		this.id = id;
		this.ipAddr = ipAddr;
	}

}