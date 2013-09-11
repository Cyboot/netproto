package net.tmt.common.network.dtos;

import java.util.List;

public class PackageDTO extends DTO {
	private static final long	serialVersionUID	= 7496614138963116438L;

	private long				registerClientID;
	private List<DTO>			dtos;

	public PackageDTO(final List<DTO> dtos) {
		super();
		this.dtos = dtos;
	}

	public long getRegisterClientID() {
		return registerClientID;
	}

	public void setRegisterClientID(final long registerClientID) {
		this.registerClientID = registerClientID;
	}

	public List<DTO> getDtos() {
		return dtos;
	}

	public void setDtos(final List<DTO> dtos) {
		this.dtos = dtos;
	}
}
