package net.tmt.common.network.dtos;

public class RemappedEntityDTO extends DTO {
	private static final long	serialVersionUID	= -4945379651873377811L;

	private long				oldID;
	private EntityDTO			dto;

	public RemappedEntityDTO(final long oldID, final EntityDTO dto) {
		this.oldID = oldID;
		this.dto = dto;
	}

	public EntityDTO getDto() {
		return dto;
	}

	public void setDto(final EntityDTO dto) {
		this.dto = dto;
	}

	public long getOldID() {
		return oldID;
	}

	public void setOldID(final long oldID) {
		this.oldID = oldID;
	}

	@Override
	public String toString() {
		return "RemappedEntDTO: " + super.toString() + " oldID=" + oldID + " dto=[" + dto + "]";
	}

}
