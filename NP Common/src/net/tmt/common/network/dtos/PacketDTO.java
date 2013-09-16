package net.tmt.common.network.dtos;

import java.util.List;

public class PacketDTO extends DTO {
	private static final long	serialVersionUID	= 7496614138963116438L;

	private long				packetNr;
	private List<DTO>			dtos;

	public PacketDTO() {
	}

	public PacketDTO(final List<DTO> dtos) {
		super();
		this.dtos = dtos;
	}

	public List<DTO> getDtos() {
		return dtos;
	}

	public void setDtos(final List<DTO> dtos) {
		this.dtos = dtos;
	}

	public long getPacketNr() {
		return packetNr;
	}

	public void setPacketNr(final long packetNr) {
		this.packetNr = packetNr;
	}

	@Override
	public String toString() {
		return "Paket " + super.toString() + " #Nr. " + packetNr + " size: " + dtos.size();
	}
}
