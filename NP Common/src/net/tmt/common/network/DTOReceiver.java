package net.tmt.common.network;

import java.util.List;

import net.tmt.common.network.dtos.DTO;


public interface DTOReceiver {

	/**
	 * @return true if there are unread DTOs
	 */
	public boolean hasUnreadDTOs();

	/**
	 * will return all unread DTOs and resets the buffer. So it will return an
	 * empty list until it receives new DTOs
	 * 
	 * @return unread DTOs
	 */
	public List<DTO> getUnreadDTOs();
}
