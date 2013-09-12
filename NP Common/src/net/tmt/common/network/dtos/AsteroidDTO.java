package net.tmt.common.network.dtos;


public class AsteroidDTO extends EntityDTO {
	private static final long	serialVersionUID	= -27308302764675589L;

	private int					size;

	public AsteroidDTO(final EntityDTO dto, final int size) {
		super(dto);
		this.size = size;
	}

	public int getSize() {
		return this.size;
	}
}
