package net.tmt.client.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import net.tmt.client.game.Game;
import net.tmt.common.util.StringFormatter;

@SuppressWarnings("serial")
public class GameEngine extends Canvas {
	public static final int	DELTA_TARGET	= 15;

	private float			cpuWorkload;
	private String			cpuWorkloadText	= "";


	public GameEngine() {
		// FIXME: (maybe Windows specific problem): somehow the Canvas is 10
		// pixel to big. No idea why
		Dimension dim = new Dimension(Game.WIDTH - 10, Game.HEIGHT - 10);

		this.setPreferredSize(dim);
		this.setMinimumSize(dim);
		this.setMaximumSize(dim);

		setBackground(Color.DARK_GRAY);
	}

	public void start() {
		final int DELTA_TARGET_NANOS = DELTA_TARGET * 1000 * 1000;

		while (true) {
			long timeStart = System.nanoTime();

			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {
				createBufferStrategy(3);
				continue;
			}

			// ##### tick and render #####
			tick();
			render(bs.getDrawGraphics());

			if (bs != null)
				bs.show();

			regulateFPS(timeStart, DELTA_TARGET_NANOS);
		}
	}

	/**
	 * The tick method, all calculation here
	 */
	private void tick() {
	}

	/**
	 * The render method, only display elements (use Objects "read-only")
	 * 
	 * @param g
	 */
	private void render(Graphics g) {
		g.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);


		g.setColor(Color.yellow);
		g.setFont(getFont());
		g.drawString(cpuWorkloadText, Game.WIDTH - 30, 15);


		g.dispose();
		Toolkit.getDefaultToolkit().sync();
	}

	private void regulateFPS(long timeStart, long DELTA_TARGET_NANOS) {
		long timePassed = System.nanoTime() - timeStart;
		if (timePassed < DELTA_TARGET_NANOS) {
			long sleepTime = DELTA_TARGET_NANOS - timePassed;

			long millis = sleepTime / (1000 * 1000);
			int nano = (int) (sleepTime % (1000 * 1000));

			try {
				Thread.sleep(millis, nano);
			} catch (InterruptedException e) {
			}
		}

		cpuWorkload = (float) timePassed / DELTA_TARGET_NANOS;
		cpuWorkloadText = StringFormatter.format(cpuWorkload);
	}
}
