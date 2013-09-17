package net.tmt.common.network.dtos;

public class TimeSyncroDTO {
	private long	id;

	private long	clientTimestamp;
	private long	serverTimestamp;

	public TimeSyncroDTO(final long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(final long id) {
		this.id = id;
	}

	public long getClientTimestamp() {
		return clientTimestamp;
	}

	public void setClientTimestamp(final long clientTimestamp) {
		this.clientTimestamp = clientTimestamp;
	}

	public long getServerTimestamp() {
		return serverTimestamp;
	}

	public void setServerTimestamp(final long serverTimestamp) {
		this.serverTimestamp = serverTimestamp;
	}
}
