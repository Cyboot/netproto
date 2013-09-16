package net.tmt.common.network.dtos;

public class ServerInfoDTO extends DTO {
	private static final long	serialVersionUID	= 1259160510968892066L;

	private String				cpuWorkLoad;

	public ServerInfoDTO() {
	}

	public ServerInfoDTO(final String cpuWorkLoad) {
		super();
		this.cpuWorkLoad = cpuWorkLoad;
	}

	public String getCpuWorkLoad() {
		return cpuWorkLoad;
	}

	public void setCpuWorkLoad(final String cpuWorkLoad) {
		this.cpuWorkLoad = cpuWorkLoad;
	}
}
