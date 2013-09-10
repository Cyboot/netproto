package net.tmt.common.util;

import java.io.Serializable;

public class Vector2d implements Serializable {
	private static final long	serialVersionUID	= -6889125305964874168L;

	public double				x;
	public double				y;

	public Vector2d() {
	}

	public Vector2d(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	public Vector2d copy() {
		return new Vector2d(x, y);
	}

	public Vector2d difference(final Vector2d other) {
		return new Vector2d(x - other.x, y - other.y);
	}

	public Vector2d sum(final Vector2d other) {
		return new Vector2d(x + other.x, y + other.y);
	}

	public double distance(final Vector2d other) {
		double dx = other.x - x;
		double dy = other.y - y;

		return Math.sqrt(dx * dx + dy * dy);
	}

	public Vector2d add(final Vector2d other) {
		this.x += other.x;
		this.y += other.y;
		return this;
	}

	public Vector2d set(final Vector2d other) {
		this.x = other.x;
		this.y = other.y;
		return this;
	}

	public int x() {
		return (int) x;
	}

	public int y() {
		return (int) y;
	}


	@Override
	public String toString() {
		return "(" + StringFormatter.format(x, 4, 2) + " : " + StringFormatter.format(y, 4, 2) + ")";
	}
}
