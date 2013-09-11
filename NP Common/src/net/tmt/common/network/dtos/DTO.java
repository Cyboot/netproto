package net.tmt.common.network.dtos;

import java.io.Serializable;

public class DTO implements Serializable {
	private static final long	serialVersionUID	= -7136711308429782840L;

	private long				clientId;
	private long				timestamp;

	public DTO() {
	}

	public DTO(final long clientId, final long timestamp) {
		this.clientId = clientId;
		this.timestamp = timestamp;
	}

	public long getClientId() {
		return clientId;
	}

	public void setClientId(final long clientId) {
		this.clientId = clientId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	public void setDTOValues(final DTO dto) {
		this.clientId = dto.clientId;
		this.timestamp = dto.timestamp;
	}

	@Override
	public String toString() {
		return "c#" + clientId;
	}
}
