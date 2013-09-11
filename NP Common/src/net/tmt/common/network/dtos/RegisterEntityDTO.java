package net.tmt.common.network.dtos;

public class RegisterEntityDTO extends DTO {
	private static final long	serialVersionUID	= 5600318989588633097L;

	private EntityDTO			dto;

	public EntityDTO getDto() {
		return dto;
	}

	public void setDto(final EntityDTO dto) {
		this.dto = dto;
	}

	public RegisterEntityDTO(final EntityDTO dto) {
		super();
		this.dto = dto;
	}

	@Override
	public String toString() {
		return "RegEntDTO: " + dto;
	}
}
