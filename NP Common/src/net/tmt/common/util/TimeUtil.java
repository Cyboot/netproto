package net.tmt.common.util;

public class TimeUtil {
	private static boolean	isSynchronized	= false;
	private static long		delay;


	public static boolean isSynchronized() {
		return isSynchronized;
	}

	/**
	 * 
	 * @return same timestamp as server
	 */
	public static long getSynchroTimestamp() {
		return System.currentTimeMillis() + delay;
	}


	public static void synchronize(final long delay) {
		TimeUtil.delay = delay;
		isSynchronized = true;
	}
}
