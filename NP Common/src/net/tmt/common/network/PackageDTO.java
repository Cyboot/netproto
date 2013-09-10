package net.tmt.common.network;

import java.util.List;

public class PackageDTO extends DTO {
	private static final long	serialVersionUID	= 7496614138963116438L;

	private List<DTO>			dtos;

	public PackageDTO(final List<DTO> dtos) {
		super();
		this.dtos = dtos;
	}

	public List<DTO> getDtos() {
		return dtos;
	}

	public void setDtos(final List<DTO> dtos) {
		this.dtos = dtos;
	}
}
