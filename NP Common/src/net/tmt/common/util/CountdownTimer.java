package net.tmt.common.util;

public class CountdownTimer {
	private static int	DELTA_TARGET;

	private boolean		autoReset	= true;
	private int			timeleftIntervall;
	private int			timer;

	public CountdownTimer(final int timeleftIntervall, final int starttimeleft) {
		this.timeleftIntervall = timeleftIntervall;
		this.timer = starttimeleft;
	}


	public CountdownTimer(final int timeleftIntervall) {
		this(timeleftIntervall, timeleftIntervall);
	}


	public boolean isTimeleft() {
		timer -= DELTA_TARGET;

		if (timer <= 0) {
			if (autoReset)
				timer += timeleftIntervall;
			return true;
		}
		return false;
	}

	public void reset() {
		timer = timeleftIntervall;
	}

	public static void setDELTA_TARGET(final int dELTA_TARGET) {
		DELTA_TARGET = dELTA_TARGET;
	}

	public static CountdownTimer createManuelResetTimer(final int timeleftIntervall) {
		CountdownTimer result = new CountdownTimer(timeleftIntervall);
		result.autoReset = false;
		return result;
	}
}