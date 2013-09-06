package net.tmt.common.network;

import java.io.Serializable;

public abstract class DTO implements Serializable {
	private static final long	serialVersionUID	= -7136711308429782840L;

	private long				id;
	private long				clientId;
	private long				timestamp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
}
