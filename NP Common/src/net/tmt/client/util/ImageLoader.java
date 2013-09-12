package net.tmt.client.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.tmt.Constants;

public class ImageLoader {
	public static BufferedImage	point_green;
	public static BufferedImage	point_blue;
	public static BufferedImage	point_red;

	public static BufferedImage	ship_red;
	public static BufferedImage	ship_blue;
	public static BufferedImage	ship_green;
	public static BufferedImage	ship_engine_left;
	public static BufferedImage	ship_engine_right;
	public static BufferedImage	ship_engine_main;

	public static void init() {
		point_green = loadImage("point_green.png");
		point_blue = loadImage("point_blue.png");
		point_red = loadImage("point_red.png");

		ship_red = loadImage("ship_red.png");
		ship_blue = loadImage("ship_blue.png");
		ship_green = loadImage("ship_green.png");

		ship_engine_main = loadImage("ship_engine_main.png");
		ship_engine_right = loadImage("ship_engine_right.png");
		ship_engine_left = loadImage("ship_engine_left.png");
	}

	private static BufferedImage loadImage(final String img) {
		try {
			return ImageIO.read(Constants.class.getResource("/" + img));
		} catch (IOException e) {
			throw new IllegalArgumentException("The Image you tried to load was not found!", e);
		}
	}

	public static BufferedImage getSubImage(final BufferedImage img, final int x, final int y, final int width) {
		return img.getSubimage(x * width, y * width, width, width);
	}

	public static BufferedImage getCutImage(final BufferedImage img, final int width) {
		return img.getSubimage(0, 0, width, img.getHeight());
	}
}
