package net.tmt.common.network;

import net.tmt.common.network.dtos.DTO;


public interface DTOSender {

	/**
	 * adds the given DTO to a list to be send later
	 * 
	 * @param dto
	 *            to collect for sending with <em> sendNow() <em>
	 */
	public void sendDTO(DTO dto);

	/**
	 * sends all added DTOs inside a PackageDTO to the given location
	 * (server/client)
	 */
	public void sendNow();
}
