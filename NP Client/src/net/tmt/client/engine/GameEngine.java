package net.tmt.client.engine;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

import net.tmt.Constants;
import net.tmt.client.game.Game;
import net.tmt.common.util.CountdownTimer;
import net.tmt.common.util.StringFormatter;

@SuppressWarnings("serial")
public class GameEngine extends Canvas {
	private static GameEngine	instance;

	private static final int	TICK_RENDER_RATIO	= 2;

	private JFrame				frame;
	private Game				game;
	private float				cpuWorkload;
	private String				cpuWorkloadText		= "";


	private GameEngine(final JFrame frame) {
		this.frame = frame;
		// TODO: (maybe Windows specific problem): somehow the Canvas is 10
		// pixel to big. No idea why
		Dimension dim = new Dimension(Game.WIDTH - 10, Game.HEIGHT - 10);

		this.setPreferredSize(dim);
		this.setMinimumSize(dim);
		this.setMaximumSize(dim);

		addKeyListener(Controls.getInstance());
		setBackground(Color.DARK_GRAY);
	}

	public void start() {
		CountdownTimer.setDELTA_TARGET(Constants.DELTA_TARGET);
		game = Game.getInstance();

		final int DELTA_TARGET_NANOS = Constants.DELTA_TARGET * 1000 * 1000 * TICK_RENDER_RATIO;

		while (true) {
			long timeStart = System.nanoTime();

			BufferStrategy bs = getBufferStrategy();
			if (bs == null) {
				createBufferStrategy(3);
				continue;
			}

			// ##### tick and render #####
			for (int i = 0; i < TICK_RENDER_RATIO; i++)
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
		game.tick();
	}

	/**
	 * The render method, only display elements (use Objects "read-only")
	 * 
	 * @param g
	 */
	private void render(final Graphics g) {
		g.clearRect(0, 0, Game.WIDTH, Game.HEIGHT);

		game.render(g);

		g.setColor(Color.yellow);
		g.setFont(getFont());
		g.drawString("CPU: " + cpuWorkloadText, Game.WIDTH - 60, 15);


		g.dispose();
		Toolkit.getDefaultToolkit().sync();
	}

	private void regulateFPS(final long timeStart, final long DELTA_TARGET_NANOS) {
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

	public static void init(final JFrame frame) {
		instance = new GameEngine(frame);
	}

	public static GameEngine getInstance() {
		return instance;
	}

	public void setTitle(final String str) {
		frame.setTitle(str);
	}
}
