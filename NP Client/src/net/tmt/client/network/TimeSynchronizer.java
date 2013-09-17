package net.tmt.client.network;

import java.util.Arrays;

import net.tmt.common.network.dtos.TimeSyncroDTO;
import net.tmt.common.util.TimeUtil;

import org.apache.log4j.Logger;

import com.esotericsoftware.kryonet.Client;

public class TimeSynchronizer extends Thread {
	private static final int		PACKET_COUNT	= 100;
	private static Logger			logger			= Logger.getLogger(TimeSynchronizer.class);
	private static TimeSynchronizer	instance;


	private Client					kryoclient;
	private TimeSyncroDTO[]			dtos;
	private long[]					serverClientDelays;
	private long[]					pings;

	public TimeSynchronizer(final Client kryoclient) {
		this.kryoclient = kryoclient;
		dtos = new TimeSyncroDTO[PACKET_COUNT];
		pings = new long[PACKET_COUNT];
		serverClientDelays = new long[PACKET_COUNT];
		for (int i = 0; i < dtos.length; i++) {
			dtos[i] = new TimeSyncroDTO(i);
		}
	}

	@Override
	public void run() {
		logger.debug("start sending synchronizing packets");
		sendPings();
		long avg = calculateAverageDelay();

		logger.debug("Difference between server/client time: " + avg + " ms");
		TimeUtil.synchronize(-avg);
	}

	private long calculateAverageDelay() {
		long[] sortedArray = new long[pings.length];
		System.arraycopy(pings, 0, sortedArray, 0, pings.length);
		Arrays.sort(sortedArray);

		// find min and max outliers (Außreiser)
		long min = sortedArray[PACKET_COUNT / 10];
		long max = sortedArray[PACKET_COUNT - PACKET_COUNT / 10];

		long serverClientSum = 0;
		int counter = 0;
		for (int i = 0; i < PACKET_COUNT; i++) {
			if (pings[i] > max || pings[i] < min)// ignore outliner
				continue;
			serverClientSum += serverClientDelays[i];
			counter++;
		}

		if (counter == 0)
			return 0;
		return serverClientSum / counter;
	}

	public static void main(final String[] args) {
		instance = new TimeSynchronizer(null);

		for (int i = 0; i < PACKET_COUNT; i++) {
			instance.pings[i] = (long) (Math.random() * 100);
			instance.serverClientDelays[i] = (long) (Math.random() * 100);
		}
		System.out.println(instance.calculateAverageDelay());
	}

	private void sendPings() {
		for (int i = 0; i < dtos.length; i++) {
			dtos[i].setClientTimestamp(System.currentTimeMillis());

			logger.trace("sending TimeSyncroDTO #" + i);
			kryoclient.sendUDP(dtos[i]);

			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
		}
		try {
			// Wait some time so most packets should be received by now
			Thread.sleep(150);
		} catch (InterruptedException e) {
		}
	}

	public static TimeSynchronizer init(final Client client) {
		instance = new TimeSynchronizer(client);
		return instance;
	}

	public void put(final TimeSyncroDTO object) {
		long timespend = System.currentTimeMillis() - object.getClientTimestamp();
		pings[object.getId()] = timespend;

		long serverClientDelay = object.getServerTimestamp() - (object.getClientTimestamp() + timespend / 2);
		serverClientDelays[object.getId()] = serverClientDelay;
	}

	public static TimeSynchronizer getInstance() {
		return instance;
	}
}
