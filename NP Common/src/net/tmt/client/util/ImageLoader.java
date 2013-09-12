package net.tmt.client.util;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.tmt.Constants;

public class ImageLoader {
	public static BufferedImage	point_green;
	public static BufferedImage	point_blue;
	public static BufferedImage	point_red;

	public static void init() {
		point_green = loadImage("point_green.png");
		point_blue = loadImage("point_blue.png");
		point_red = loadImage("point_red.png");
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
