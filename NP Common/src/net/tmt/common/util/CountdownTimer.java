package net.tmt.common.util;

public class CountdownTimer {
	private static int	DELTA_TARGET;

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
			timer += timeleftIntervall;
			return true;
		}

		return false;
	}

	public static void setDELTA_TARGET(final int dELTA_TARGET) {
		DELTA_TARGET = dELTA_TARGET;
	}
}