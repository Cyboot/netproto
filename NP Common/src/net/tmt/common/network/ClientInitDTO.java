package net.tmt.common.network;

public class ClientInitDTO extends DTO {

	private static final long	serialVersionUID	= 429038167125420705L;

	public ClientInitDTO(final long id) {
		this.setClientId(id);
		this.setId(0);
		this.setTimestamp(System.currentTimeMillis());
	}

}
