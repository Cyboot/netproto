package net.tmt.client.network;

import net.tmt.common.network.dtos.TimeSyncroDTO;

import com.esotericsoftware.kryonet.Client;

public class TimeSynchronizer extends Thread {
	private static TimeSynchronizer	instance;

	private Client					kryoclient;
	private TimeSyncroDTO[]			dtos;

	public TimeSynchronizer(final Client kryoclient) {
		this.kryoclient = kryoclient;

		dtos = new TimeSyncroDTO[10];
		for (int i = 0; i < dtos.length; i++) {
			dtos[i] = new TimeSyncroDTO(i);
		}
	}

	@Override
	public void run() {
		for (int i = 0; i < dtos.length; i++) {
			dtos[i].setClientTimestamp(System.currentTimeMillis());
			kryoclient.sendUDP(dtos[i]);

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}

	}

	public static TimeSynchronizer getInstance() {
		return instance;
	}

	public void put(final TimeSyncroDTO object) {
		// TODO Auto-generated method stub
		calculate time spend / halftime
	}
}
