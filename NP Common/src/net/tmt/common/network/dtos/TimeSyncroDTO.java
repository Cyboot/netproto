package net.tmt.common.network.dtos;

public class TimeSyncroDTO {
	private int		id;

	private long	clientTimestamp;
	private long	serverTimestamp;

	public TimeSyncroDTO() {
	}

	public TimeSyncroDTO(final int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
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
